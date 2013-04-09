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
package org.openbel.framework.core.protocol.handler;

import static org.openbel.framework.common.BELUtilities.ephemeralPort;

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
        port = ephemeralPort();

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
