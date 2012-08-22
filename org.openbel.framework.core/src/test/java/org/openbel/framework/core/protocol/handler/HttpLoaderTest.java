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
package org.openbel.framework.core.protocol.handler;

import static org.openbel.framework.common.BELUtilities.ephemeralPort;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link HttpLoaderTest} tests the {@link HttpProtocolHandler}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class HttpLoaderTest extends AbstractProtocolTest {
    /**
     * Defines the http port to use.
     */
    private int port;

    /**
     * Set up the http server using a random available ephemeral port.
     */
    @Before
    public void startHttpServer() {
        Server server = new Server();
        server.setStopAtShutdown(true);

        port = ephemeralPort();

        SelectChannelConnector httpConnector = new SelectChannelConnector();
        System.setProperty("jetty.home", new File(
                "src/test/resources/org/openbel/framework/namespace/service")
                .getAbsolutePath());
        httpConnector.setPort(port);
        httpConnector.setMaxIdleTime(30000);
        httpConnector.setRequestHeaderSize(8192);

        server.setHandler(new FileResponseHandler());
        server.setConnectors(new Connector[] { httpConnector });

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Tests the successful retrievel of a file over http.
     */
    @Test
    public void testHttpNamespaceLoader() {
        try {
            final String url = "http://localhost:" + port + "/test.namespace";
            File downloadedNamespace =
                    new HttpProtocolHandler().downloadResource(url,
                            "test.belns");
            tempFiles.add(downloadedNamespace);
            testFile(downloadedNamespace);
        } catch (Exception e) {
            Assert.fail("Error reading relative file path: " + e.getMessage());
        }
    }
}
