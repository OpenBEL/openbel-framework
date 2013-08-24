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
import java.io.IOException;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.core.protocol.handler.SftpProtocolHandler;
import org.openbel.framework.core.protocol.handler.SftpProtocolHandler.DefaultUserInfo;

/**
 * {@link SftpLoaderTest} tests the {@link SftpProtocolHandler}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class SftpLoaderTest extends AbstractProtocolTest {
    /**
     * Defines the ssh server daemon.
     */
    private SshServer sshd;

    /**
     * Defines the random available ephermal port to use for the ssh
     * server.
     */
    private int port;

    /**
     * Defines the local test file to retrieve to.
     */
    private File localTestFile;

    /**
     * Set up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void startSSHServer() {
        port = getAvailablePort();

        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(
                "src/test/resources/hostkey.ser"));
        sshd.setSubsystemFactories(Arrays
                .<NamedFactory<Command>> asList(new SftpSubsystem.Factory()));
        sshd.setCommandFactory(new ScpCommandFactory());
        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {

            @Override
            public boolean authenticate(String u, String p, ServerSession s) {
                return ("sftptest".equals(u) && "sftptest".equals(p));
            }
        });

        try {
            sshd.start();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        localTestFile = new File(System.getProperty("user.dir")
                + File.separator + TEST_FILE_PATH);
    }

    /**
     * Test cleanup - Stop the ssh server and delete test file.
     */
    @After
    public void deleteTempFile() {
        try {
            sshd.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        boolean deleted = new File("src/test/resources/hostkey.ser").delete();
        assert deleted;
    }

    /**
     * Test the successful retrieval of a file using username/password
     * authentication in the url.
     */
    @Test
    public void testUsernamePasswordSecureFtpFilePath() {
        try {
            SftpProtocolHandler sftp = new SftpProtocolHandler();
            File downloadedNamespace = sftp.downloadResource(
                    "sftp://sftptest:sftptest@localhost:" + port
                            + localTestFile.getAbsolutePath(), "test.belns");
            tempFiles.add(downloadedNamespace);
            testFile(downloadedNamespace);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Error reading relative file path: " + e.getMessage());
        }
    }

    /**
     * Test the successful retrieval of a file only a username and prompting
     * for the password using {@link TestUserInfo}.
     */
    @Test
    public void testUsernameOnlySecureFtpFilePath() {
        try {
            SftpProtocolHandler sftp = new SftpProtocolHandler();
            sftp.setUserInfo(new TestUserInfo());
            File downloadedNamespace = sftp.downloadResource(
                    "sftp://sftptest@localhost:" + port
                            + localTestFile.getAbsolutePath(), "test.belns");
            tempFiles.add(downloadedNamespace);
            testFile(downloadedNamespace);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Error reading relative file path: " + e.getMessage());
        }
    }

    /**
     * TestUserInfo provides a mocked {@link DefaultUserInfo} to prompt for
     * and immediately return the test password.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    protected class TestUserInfo extends DefaultUserInfo {

        /**
         * Creates the test user info.
         */
        public TestUserInfo() {
            new SftpProtocolHandler().super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean promptPassword(String message) {
            p = "sftptest";
            System.out.print(message + ": " + p);
            return true;
        }
    }
}
