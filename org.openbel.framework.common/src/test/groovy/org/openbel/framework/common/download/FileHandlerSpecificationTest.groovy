package org.openbel.framework.common.download

import org.openbel.framework.common.SystemConfigurationFile;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.cfg.SystemConfigurationBasedTest;

import spock.lang.Shared;
import spock.lang.Specification;

/**
 * {@link FileHandlerSpecificationTest} defines specifications for files
 * downloaded via the {@link SupportedProtocol#FILE file protocol}.
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
class FileHandlerSpecificationTest extends Specification {
    @Shared syscfg = new SystemConfigurationMock()
    @Shared downloadHandler = new FileDownloadHandler()

    def setupSpec() {
        syscfg.setup()
    }

    def "file download without checksum"() {
        when:
            def inputFile = FileHandlerSpecificationTest.class.getResource("file-without-checksum.belns")
            def downloadFile = downloadHandler.download(inputFile)

        then:
            downloadFile.downloadedFile != null
            downloadFile.downloadedFile.exists()
            downloadFile.downloadedFile.canRead()
            downloadFile.checksum == null
    }

    def "file download with checksum"() {
        when:
            def inputFile = FileHandlerSpecificationTest.class.getResource("file-with-checksum.belns")
            def downloadFile = downloadHandler.download(inputFile)

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
}
