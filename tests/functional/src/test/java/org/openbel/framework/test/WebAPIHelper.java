/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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
package org.openbel.framework.test;

import static java.lang.String.format;
import static java.lang.System.getenv;
import static org.openbel.framework.common.BELUtilities.isNumeric;
import static org.openbel.framework.test.Constants.ENDPOINT_FORMAT;
import static org.openbel.framework.test.Constants.TOMCAT_HTTP_PORT_ENV_VAR;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.openbel.framework.ws.model.WebAPI;
import org.openbel.framework.ws.model.WebAPIService;

/**
 * WebAPIHelper provides initialization of the {@link WebAPI web api} for
 * testing purposes.
 *
 * @see Constants#ENDPOINT_FORMAT
 * @see Constants#TOMCAT_HTTP_PORT_ENV_VAR
 */
class WebAPIHelper {

    /**
     * Create the {@link WebAPI web api} using the
     * {@link Constants#TOMCAT_HTTP_PORT_ENV_VAR port environment variable}.
     *
     * @return {@link WebAPI web api}
     */
    public static WebAPI createWebAPI() {
        String port = getenv(TOMCAT_HTTP_PORT_ENV_VAR);
        if (!isNumeric(port)) {
            throw new IllegalStateException("TOMCAT_HTTP_PORT is invalid.");
        }
        String endpoint = format(ENDPOINT_FORMAT, Integer.parseInt(port));

        try {
            return new WebAPIService(new URL(endpoint), new QName(
                    "http://belframework.org/ws/schemas", "WebAPIService"))
                    .getWebAPISoap11();
        } catch (MalformedURLException e) {
            throw new IllegalStateException(format(
                    "endpoint URL %s is invalid.", endpoint));
        }
    }
}
