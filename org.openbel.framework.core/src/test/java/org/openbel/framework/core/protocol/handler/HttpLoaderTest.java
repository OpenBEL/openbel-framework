/**
 *  Copyright 2013 OpenBEL Consortium
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openbel.framework.core.protocol.handler;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.core.protocol.handler.HttpProtocolHandler;

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

        port = getAvailablePort();

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
