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
package org.openbel.framework.ws.client.example;

import static org.openbel.framework.ws.client.ObjectFactory.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

import org.openbel.framework.ws.client.*;

/**
 * Example demonstrating mapping namespace data to {@link KamNode kam nodes}.
 */
public class MapData {

    private static WebAPI WAPI;
    private final static String URL_STR;
    private final static String NS;
    private final static String HGNC =
            "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns";
    static {
        URL_STR = "http://localhost:8080/openbel-ws/belframework.wsdl";
        NS = "http://belframework.org/ws/schemas";
    }

    public static void main(String... args) throws MalformedURLException {
        // must provide KAM name
        if (args.length != 1) {
            System.out.println("usage: KAM_NAME");
            System.exit(1);
        }

        // read first argument as KAM name
        final String kamName = args[0];

        // create Web API service
        final String local = "WebAPIService";
        final QName qn = new QName(NS, local);
        final WebAPIService ws = new WebAPIService(new URL(URL_STR), qn);
        WAPI = ws.getWebAPISoap11();

        // Load Kam
        final Kam kam = new Kam();
        kam.setName(kamName);
        final LoadKamRequest lkreq = createLoadKamRequest();
        lkreq.setKam(kam);

        LoadKamResponse lkres = null;
        KAMLoadStatus status = KAMLoadStatus.IN_PROCESS;
        while (KAMLoadStatus.IN_PROCESS.equals(status)) {
            lkres = WAPI.loadKam(lkreq);
            status = lkres.getLoadStatus();
        }
        if (KAMLoadStatus.FAILED.equals(status) || lkres == null) {
            System.out.println("Failed to load kam " + kamName);
            return;
        }

        // Create node filter
        final NodeFilter filter = createNodeFilter();
        final FunctionTypeFilterCriteria ft = new FunctionTypeFilterCriteria();
        ft.getValueSet().add(FunctionType.PROTEIN_ABUNDANCE);
        filter.getFunctionTypeCriteria().add(ft);

        // Map Data (single entrez gene id)
        final String symbol = "PPARG";
        final KamHandle kamHandle = lkres.getHandle();
        final FindKamNodesByNamespaceValuesRequest req =
                createFindKamNodesByNamespaceValuesRequest();
        req.setHandle(kamHandle);
        final Namespace hgncNs = createNamespace();
        hgncNs.setPrefix("HGNC");
        hgncNs.setResourceLocation(HGNC);

        final NamespaceValue nv = createNamespaceValue();
        nv.setNamespace(hgncNs);
        // req.setNodeFilter(filter);
        nv.setValue(symbol);
        req.getNamespaceValues().add(nv);
        final FindKamNodesByNamespaceValuesResponse res =
                WAPI.findKamNodesByNamespaceValues(req);
        final List<KamNode> nodes = res.getKamNodes();

        // report how many kam nodes were mapped to
        System.out.println(nodes.size() + " KAM nodes were found.");

        // report KAM node and Get Supporting Terms
        final GetSupportingTermsRequest gstreq =
                new GetSupportingTermsRequest();
        if (!nodes.isEmpty()) {
            for (final KamNode n : nodes) {
                System.out.println("KAM node label:\n    " + n.getLabel());

                gstreq.setKamNode(n);
                final GetSupportingTermsResponse gstres = WAPI
                        .getSupportingTerms(gstreq);
                final List<BelTerm> terms = gstres.getTerms();
                for (final BelTerm t : terms) {
                    System.out.println("    BEL term label:\n        "
                            + t.getLabel());
                }

                System.out.print("\n");
            }
        }
    }
}
