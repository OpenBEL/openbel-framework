/**
 * Copyright (C) 2012 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
 */
package org.openbel.framework.ws.client;

import static java.lang.String.format;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

public class WebAPIFullRun {

    public void run(final String wsdlURL, final String kamName)
            throws MalformedURLException {
        final WebAPI api = connectWebService(wsdlURL);

        final Kam kam = new Kam();
        kam.setName(kamName);

        // LoadKam
        final KamHandle kh = loadKam(api, kam);

        // FindNodes
        final KamNode node = findNodes(api, kh);

        List<KamEdge> edges;

        // GetAdjacentEdges
        GetAdjacentKamEdgesRequest gaereq = getAdjacentKamEdges(api, node);

        // GetAdjacentEdges w/ filter
        edges = getAdjacentEdgesWithFilter(api, gaereq);

        // GetAdjacentNodes
        getAdjacentKamNodes(api, node);

        // GetSupportingEvidence
        getSupportingEvidence(api, edges);

        // GetSupportingTerms
        getSupportingTerms(api, node);

        // GetAnnotationTypes
        getAnnotationTypes(api, kh);

        // ReleaseKam
        releaseKam(api, kh);
    }

    protected void getAnnotationTypes(final WebAPI api, final KamHandle kh) {
        GetAnnotationTypesRequest gatreq = new GetAnnotationTypesRequest();
        gatreq.setHandle(kh);
        GetAnnotationTypesResponse gatres = api.getAnnotationTypes(gatreq);
        List<AnnotationType> ats = gatres.getAnnotationTypes();
        for (AnnotationType at : ats) {
            System.out.println("Found annotation type with id '" + at.getId()
                    + "':");
            System.out.println("  Name: " + at.getName());
            System.out.println("  Description: " + at.getDescription());
            System.out.println("  Usage: " + at.getUsage());
            System.out.println("  AnnotationDefinition: "
                    + at.getAnnotationDefinitionType());
        }
    }

    protected void getSupportingTerms(final WebAPI api, final KamNode node) {
        GetSupportingTermsRequest gstreq = new GetSupportingTermsRequest();
        gstreq.setKamNode(node);
        GetSupportingTermsResponse gstres = api.getSupportingTerms(gstreq);
        List<BelTerm> terms = gstres.getTerms();
        System.out.println("Found terms for node id '" + node.getId() + "':");
        for (BelTerm term : terms) {
            System.out.println("  " + term.getLabel() + " (id: " + term.getId()
                    + ")");
        }
    }

    private void getSupportingEvidence(final WebAPI api, List<KamEdge> edges) {
        GetSupportingEvidenceRequest gsereq =
                new GetSupportingEvidenceRequest();
        gsereq.setKamEdge(edges.get(0));
        GetSupportingEvidenceResponse gseres =
                api.getSupportingEvidence(gsereq);
        List<BelStatement> statements = gseres.getStatements();
        System.out.println("For edge with id '" + edges.get(0).getId()
                + "', statements:");
        for (BelStatement statement : statements) {
            System.out.println("  " + statement.getSubjectTerm().getLabel()
                    + " "
                    + statement.getRelationship() + " "
                    + statement.getObjectTerm().getLabel());
            if (statement.getCitation() == null) {
                System.out.println("  no citation");
            } else {
                System.out.println("  citation: "
                        + statement.getCitation().getName());
            }

            if (!statement.getAnnotations().isEmpty()) {
                System.out.println("  statement annotations:");

                for (Annotation annotation : statement.getAnnotations()) {
                    System.out.println("    annotation: ID - "
                            + annotation.getAnnotationType().getId()
                            + ", VALUE - " + annotation.getValue());
                }
            }
        }
    }

    private void releaseKam(final WebAPI api, final KamHandle kh) {
        final ReleaseKamRequest rkreq = new ReleaseKamRequest();
        rkreq.setKam(kh);
        api.releaseKam(rkreq);
    }

    private void getAdjacentKamNodes(final WebAPI api, final KamNode node) {
        GetAdjacentKamNodesRequest gadreq = new GetAdjacentKamNodesRequest();
        gadreq.setDirection(EdgeDirectionType.BOTH);
        gadreq.setKamNode(node);
        GetAdjacentKamNodesResponse gadres = api.getAdjacentKamNodes(gadreq);
        List<KamNode> nodes = gadres.getKamNodes();
        for (KamNode an : nodes) {
            System.out.println("Found adjacent node id '" + an.getId()
                    + "' with label '" + an.getLabel() + "'.");
        }
    }

    private List<KamEdge> getAdjacentEdgesWithFilter(final WebAPI api,
            GetAdjacentKamEdgesRequest gaereq) {
        GetAdjacentKamEdgesResponse gaeres;
        List<KamEdge> edges;
        EdgeFilter filter = new EdgeFilter();
        RelationshipTypeFilterCriteria actsInCriteria =
                new RelationshipTypeFilterCriteria();
        actsInCriteria.setIsInclude(true);
        actsInCriteria.getValueSet().add(RelationshipType.ACTS_IN);
        filter.getRelationshipCriteria().add(actsInCriteria);
        gaereq.setFilter(filter);
        gaeres = api.getAdjacentKamEdges(gaereq);
        edges = gaeres.getKamEdges();
        for (KamEdge edge : edges) {
            System.out.println("Found filtered edge id '" + edge.getId()
                    + "' with triple:\n  " + edge.getSource().getLabel()
                    + " " + edge.getRelationship() + " "
                    + edge.getTarget().getLabel());
        }
        return edges;
    }

    private GetAdjacentKamEdgesRequest getAdjacentKamEdges(final WebAPI api,
            final KamNode node) {
        GetAdjacentKamEdgesRequest gaereq = new GetAdjacentKamEdgesRequest();
        gaereq.setDirection(EdgeDirectionType.BOTH);
        gaereq.setKamNode(node);

        GetAdjacentKamEdgesResponse gaeres = api.getAdjacentKamEdges(gaereq);
        List<KamEdge> edges = gaeres.getKamEdges();
        for (KamEdge edge : edges) {
            System.out.println("Found edge id '" + edge.getId()
                    + "' with triple:\n  " + edge.getSource().getLabel()
                    + " " + edge.getRelationship() + " "
                    + edge.getTarget().getLabel());
        }
        return gaereq;
    }

    private KamNode findNodes(final WebAPI api, final KamHandle kh) {
        final FindKamNodesByLabelsRequest fnreq =
                new FindKamNodesByLabelsRequest();
        fnreq.setHandle(kh);
        fnreq.getLabels().add("proteinAbundance(154)");

        final FindKamNodesByLabelsResponse fnres =
                api.findKamNodesByLabels(fnreq);
        final KamNode node = fnres.getKamNodes().get(0);
        System.out.println("Found node '" + node.getId() + "' with label '"
                + node.getLabel() + "'.");
        return node;
    }

    private KamHandle loadKam(final WebAPI api, final Kam kam) {
        final LoadKamRequest lkreq = new LoadKamRequest();
        lkreq.setKam(kam);

        LoadKamResponse lkres = api.loadKam(lkreq);
        while (lkres.getLoadStatus() == KAMLoadStatus.IN_PROCESS) {
            lkres = api.loadKam(lkreq);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Ignore it
            }
        }

        if (lkres.getLoadStatus() == KAMLoadStatus.FAILED) {
            final String fmt = "KAM \"%s\" failed to load";
            throw new RuntimeException(format(fmt, kam.getName()));
        }

        final KamHandle kh = lkres.handle;
        System.out.println("KAM Handle is: " + kh.getHandle());
        return kh;
    }

    protected WebAPI connectWebService(final String wsdlURL)
            throws MalformedURLException {
        WebAPIService ws =
                new WebAPIService(new URL(wsdlURL), new QName(
                        "http://belframework.org/ws/schemas", "WebAPIService"));
        return ws.getWebAPISoap11();
    }

    public static void main(String[] args) throws MalformedURLException {
        new WebAPIFullRun().run(args[0], args[1]);
    }
}
