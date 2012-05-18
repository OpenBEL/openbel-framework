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

import static java.lang.String.*;
import static java.lang.System.*;
import static org.openbel.framework.ws.client.ObjectFactory.createFindKamEdgesRequest;
import static org.openbel.framework.ws.client.RelationshipType.*;

import java.net.*;
import java.util.*;
import javax.xml.namespace.*;

import org.openbel.framework.ws.client.*;

/**
 * Example demonstrating retrieval of causal edges for a KAM in the catalog.
 */
public class GetCausalEdges {

    private static WebAPI WAPI;
    private final static String URL_STR;
    private final static String NS;
    static {
        URL_STR = "http://localhost:8080/openbel-ws/belframework.wsdl";
        NS = "http://belframework.org/ws/schemas";
    }

    public static void main(String... args) throws MalformedURLException {
        final String local = "WebAPIService";
        QName qn = new QName(NS, local);
        WebAPIService ws = new WebAPIService(new URL(URL_STR), qn);
        WAPI = ws.getWebAPISoap11();

        GetCatalogResponse catalog = WAPI.getCatalog(null);
        Kam kam = catalog.getKams().get(0);
        LoadKamRequest lkreq = new LoadKamRequest();
        lkreq.setKam(kam);
        out.println("Loading KAM.");
        LoadKamResponse lkres = WAPI.loadKam(lkreq);
        KamHandle handle = lkres.getHandle();
        out.println("Loaded KAM (handle: " + handle.getHandle() + ")");

        EdgeFilter edgfltr = new EdgeFilter();
        RelationshipTypeFilterCriteria r = new RelationshipTypeFilterCriteria();
        List<RelationshipType> relationships = r.getValueSet();
        relationships.add(CAUSES_NO_CHANGE);
        relationships.add(DECREASES);
        relationships.add(DIRECTLY_DECREASES);
        relationships.add(DIRECTLY_INCREASES);
        relationships.add(INCREASES);
        edgfltr.getRelationshipCriteria().add(r);

        FindKamEdgesRequest fereq = createFindKamEdgesRequest();
        fereq.setFilter(edgfltr);
        fereq.setHandle(handle);

        out.println("Finding edges.");
        FindKamEdgesResponse feres = WAPI.findKamEdges(fereq);
        List<KamEdge> edges = feres.getKamEdges();
        out.println(format("Found %d edges.", edges.size()));

        out.println("Releasing KAM.");
        ReleaseKamRequest rkreq = new ReleaseKamRequest();
        rkreq.setKam(handle);
        WAPI.releaseKam(rkreq);
        out.println("KAM released.");
    }
}
