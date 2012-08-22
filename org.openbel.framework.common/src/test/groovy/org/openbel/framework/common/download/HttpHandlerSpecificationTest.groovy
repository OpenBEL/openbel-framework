package org.openbel.framework.common.download

import static org.openbel.framework.common.BELUtilities.copy;
import static org.openbel.framework.common.BELUtilities.ephemeralPort;

import java.util.Random;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbel.framework.common.SystemConfigurationFile;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.cfg.SystemConfigurationBasedTest;
import com.sun.corba.se.impl.protocol.SharedCDRClientRequestDispatcherImpl;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import spock.lang.Ignore;
import spock.lang.Shared;
import spock.lang.Specification;

/**
 * {@link HttpHandlerSpecificationTest} defines specifications for files
 * downloaded via the {@link SupportedProtocol#HTTP http protocol}.
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
class HttpHandlerSpecificationTest extends Specification {
    @Shared syscfg = new SystemConfigurationMock()
    @Shared downloadHandler = new HttpDownloadHandler()
    @Shared httpServer = new HttpServer()

    def setupSpec() {
        syscfg.setup()
        httpServer.start()
    }

    def "http download without checksum"() {
        when:
            def downloadFile = downloadHandler.download(new URL("http://localhost:" + httpServer.port + "/file-without-checksum.belns"))

        then:
            downloadFile.downloadedFile != null
            downloadFile.downloadedFile.exists()
            downloadFile.downloadedFile.canRead()
            downloadFile.checksum == null
    }

    def "http download with checksum"() {
        when:
            def downloadFile = downloadHandler.download(new URL("http://localhost:" + httpServer.port + "/file-with-checksum.belns"))

        then:
            downloadFile.downloadedFile != null
            downloadFile.downloadedFile.exists()
            downloadFile.downloadedFile.canRead()
            downloadFile.checksum == "e96530c04be684f1a468a98d8ab8174bba704503fb69e1ccb5376bfe3f8390c6"
    }

    def cleanupSpec() {
        syscfg.teardown()
    }

    @SystemConfigurationFile(path="src/test/resources/org/openbel/framework/common/download/belframework.cfg")
    class SystemConfigurationMock extends SystemConfigurationBasedTest {}

    class HttpServer {
        static int PORT_ATTEMPTS = 100
        private Server server
        final Integer port

        HttpServer() {
            port = ephemeralPort(PORT_ATTEMPTS);
        }

        public void start() {
            server = new Server()
            server.setStopAtShutdown(true)

            SelectChannelConnector httpConnector = new SelectChannelConnector()
            System.setProperty("jetty.home", new File("src/test/resources/org/openbel/framework/common/download").getAbsolutePath())
            httpConnector.setPort(port)
            httpConnector.setMaxIdleTime(30000)
            httpConnector.setRequestHeaderSize(8192)

            server.setHandler(new FileResponseHandler())
            server.setConnectors([httpConnector] as Connector[])

            try {
                server.start()
            } catch (Exception e) {
                e.printStackTrace()
                Assert.fail(e.getMessage())
            }
        }
    }

    class FileResponseHandler extends AbstractHandler {

       @Override
       public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
           String jettyHome = System.getProperty("jetty.home")

           response.setContentType("text/plain")
           response.setStatus(HttpServletResponse.SC_OK)
           baseRequest.setHandled(true)

           copy(new FileInputStream(new File(jettyHome + target)), response.getOutputStream())
       }
   }
}
