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

        port = ephemeralPort();

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
