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

import static java.lang.String.format;
import static java.lang.System.*;

import java.net.*;
import javax.xml.namespace.*;

import org.openbel.framework.ws.client.*;

/**
 * Example demonstrating searching for equivalences.
 */
public class FindEquivalence {

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

        FindNamespaceEquivalenceRequest req;
        req = new FindNamespaceEquivalenceRequest();

        Namespace tgtNS = new Namespace();
        tgtNS.setPrefix("EG");
        tgtNS.setResourceLocation("http://resource.belframework.org/"
                + "belframework/1.0/namespace/entrez-gene-ids-hmr.belns");
        tgtNS.setId("m80Uzc2rk+ILu/P78PeOpg==");
        req.setTargetNamespace(tgtNS);
        Namespace srcNS = new Namespace();
        srcNS.setPrefix("HGNC");
        srcNS.setId("m80Uzc2rk+J7iWRI4xhYxA==");
        srcNS.setResourceLocation("http://resource.belframework.org/"
                + "belframework/1.0/namespace/hgnc-approved-symbols.belns");

        NamespaceValue srcValue = new NamespaceValue();
        srcValue.setNamespace(srcNS);
        srcValue.setValue("AKT1");

        req.setNamespaceValue(srcValue);
        FindNamespaceEquivalenceResponse resp;
        resp = WAPI.findNamespaceEquivalence(req);
        NamespaceValue nsValue = resp.getNamespaceValue();

        if (nsValue != null) {
            String prefix = srcNS.getPrefix();
            String value = srcValue.getValue();
            String src = format("%s:%s", prefix, value);

            Namespace ns = nsValue.getNamespace();
            prefix = ns.getPrefix();
            value = nsValue.getValue();
            String equiv = format("%s:%s", prefix, value);

            out.println(format("%s is equivalent to %s", src, equiv, args));
        }
    }
}
