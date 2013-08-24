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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import junit.framework.Assert;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.core.protocol.handler.HttpsProtocolHandler;

/**
 * {@link HttpsLoaderTest} tests the {@link HttpsProtocolHandler}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class HttpsLoaderTest extends AbstractProtocolTest {
    /**
     * Defines the port for the https server.
     */
    private int port;

    /**
     * Sets up the https server.
     */
    @Before
    public void startHttpServer() {
        Server server = new Server();
        server.setStopAtShutdown(true);

        port = getAvailablePort();

        SslSelectChannelConnector sslConnector =
                new SslSelectChannelConnector();
        String jettyHome =
                new File(
                        "src/test/resources/org/openbel/framework/namespace/service")
                        .getAbsolutePath();
        System.setProperty("jetty.home", jettyHome);
        sslConnector.setPort(port);
        sslConnector.setKeystore(jettyHome + "/localhost.keystore");
        sslConnector.setPassword("OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v");
        sslConnector.setKeyPassword("OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v");

        server.setConnectors(new Connector[] { sslConnector });
        server.setHandler(new FileResponseHandler());

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Tests the successful retrieval of a file over https.
     */
    @Test
    public void testHttpNamespaceLoader() {
        try {
            trustAllCertificates();
        } catch (KeyManagementException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        try {
            File downloadedNamespace =
                    new HttpsProtocolHandler().downloadResource(
                            "https://localhost:" + port + "/test.namespace",
                            "test.belns");
            tempFiles.add(downloadedNamespace);
            testFile(downloadedNamespace);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Error reading relative file path: " + e.getMessage());
        }
    }

    /**
     * Set up a {@link TrustManager} to trust all https certificates.
     *
     * @throws NoSuchAlgorithmException - Thrown if the SSL algorithm does
     * not exist.
     * @throws KeyManagementException - Thrown if an error occurred
     * initializing the key trust.
     */
    protected void trustAllCertificates() throws NoSuchAlgorithmException,
            KeyManagementException {
        TrustManager[] trustAll = new TrustManager[] { new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs,
                    String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs,
                    String authType) {
            }
        } };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAll, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}
