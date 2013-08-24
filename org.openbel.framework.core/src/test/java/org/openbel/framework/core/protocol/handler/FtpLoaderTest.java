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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.UserManagerFactory;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.AbstractUserManager;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protocol.handler.FTPProtocolHandler;

/**
 * {@link FtpLoaderTest} tests the {@link FTPProtocolHandler}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class FtpLoaderTest extends AbstractProtocolTest {
    /**
     * Defines the test ftp username as {@value}.
     */
    private static final String TEST_USERNAME = "ftptest";

    /**
     * Defines the test ftp password as {@value}.
     */
    private static final String TEST_PASSWORD = "ftptest";

    /**
     * Defines the test ftp root as the "user.dir" system property.
     */
    private static final String TEST_USER_FTP_ROOT = System
            .getProperty("user.dir");

    /**
     * Defines the location file where the file will be saved.
     */
    private File localDestinationFile;

    /**
     * Defines the ftp server used as a test ftp daemon.
     */
    private FtpServer ftpServer;

    /**
     * Defines the port the test ftp server will run on.
     */
    private int port;

    /**
     * Set up the ftp test by choosing a random ftp server port.
     */
    @Before
    public void setupTest() {
        port = getAvailablePort();
    }

    /**
     * Tear down the test by stopping the ftp server.
     */
    @After
    public void teardownTest() {
        stopFtpServer();
    }

    /**
     * Test the successful retrieval of a file via ftp inline authentication.
     */
    @Test
    public void testFTPInlineAuthentication() {
        try {
            startRestrictedFtpServer();
        } catch (FtpException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        try {
            localDestinationFile =
                    new FTPProtocolHandler().downloadResource(
                            "ftp://ftptest:ftptest@localhost:" + port + "/"
                                    + TEST_FILE_PATH, "test.belns");
            tempFiles.add(localDestinationFile);
            testFile(localDestinationFile);
        } catch (ResourceDownloadError e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test the successful retrieval of a file via prompted password
     * authentication.
     */
    @Test
    public void testFTPPromptedPasswordAuthentication() {
        try {
            startRestrictedFtpServer();
        } catch (FtpException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        try {
            ByteArrayInputStream pwdStringInputStream =
                    new ByteArrayInputStream("ftptest".getBytes("US-ASCII"));
            localDestinationFile =
                    new FTPProtocolHandler(pwdStringInputStream)
                            .downloadResource("ftp://ftptest@localhost:" + port
                                    + "/" + TEST_FILE_PATH, "test.belns");
            tempFiles.add(localDestinationFile);
            testFile(localDestinationFile);
        } catch (ResourceDownloadError e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test the successful retrieval of a file using an anonymous ftp
     * account.
     */
    @Test
    public void testFTPAnonymousAuthenticationAllowed() {
        try {
            startUnrestrictedFtpServer();
        } catch (FtpException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        try {
            localDestinationFile =
                    new FTPProtocolHandler().downloadResource(
                            "ftp://localhost:" + port + "/" + TEST_FILE_PATH,
                            "test.belns");
            tempFiles.add(localDestinationFile);
            testFile(localDestinationFile);
        } catch (ResourceDownloadError e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test the expectation that we will receive a
     * {@link ResourceDownloadError} if the ftp server does not allow
     * anonymous account access.
     *
     * @throws ResourceDownloadError - Thrown if the ftp connection
     * could not be made because anonymous accounts are restricted.  This
     * is expected to be thrown and it is a testcase failure if it is not.
     */
    @Test(expected = ResourceDownloadError.class)
    public void testFTPAnonymousAuthenticationDenied()
            throws ResourceDownloadError {
        try {
            startRestrictedFtpServer();
        } catch (FtpException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        localDestinationFile =
                new FTPProtocolHandler().downloadResource("ftp://localhost:"
                        + port + "/" + TEST_FILE_PATH, "test.belns");
        tempFiles.add(localDestinationFile);
        try {
            testFile(localDestinationFile);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Start up the ftp daemon without anonymous account access.
     *
     * @throws FtpException - Thrown if an error occurred starting the
     * restricted ftp daemon.
     */
    protected void startRestrictedFtpServer() throws FtpException {
        FtpServerFactory serverFactory = new FtpServerFactory();

        ConnectionConfigFactory connectionConfigFactory =
                new ConnectionConfigFactory();
        connectionConfigFactory.setAnonymousLoginEnabled(false);

        serverFactory.setConnectionConfig(connectionConfigFactory
                .createConnectionConfig());
        serverFactory.setUserManager(new TestUserManagerFactory()
                .createUserManager());

        startFtpServer(serverFactory);
    }

    /**
     * Start up the ftp daemon with anonymous account access.
     *
     * @throws FtpException - Thrown if an error occurred starting the
     * unrestricted ftp daemon.
     */
    protected void startUnrestrictedFtpServer() throws FtpException {
        FtpServerFactory serverFactory = new FtpServerFactory();

        ConnectionConfigFactory connectionConfigFactory =
                new ConnectionConfigFactory();
        connectionConfigFactory.setAnonymousLoginEnabled(true);

        serverFactory.setConnectionConfig(connectionConfigFactory
                .createConnectionConfig());
        serverFactory.setUserManager(new TestUserManagerFactory()
                .createUserManager());

        startFtpServer(serverFactory);
    }

    /**
     * Start the ftp server.
     *
     * @param ftpServerFactory {@link FtpServerFactory}, the factory used
     * to configure the server with
     * @throws FtpException - Thrown if an error occurred starting the ftp
     * server.
     */
    protected void startFtpServer(FtpServerFactory ftpServerFactory)
            throws FtpException {
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(port);
        ftpServerFactory.addListener("default", factory.createListener());
        ftpServer = ftpServerFactory.createServer();
        ftpServer.start();
    }

    /**
     * Stop the ftp server.
     */
    protected void stopFtpServer() {
        if (ftpServer != null && !ftpServer.isStopped()) {
            ftpServer.stop();
        }
    }

    /**
     * TestUserManagerFactory defines a mock {@link UserManagerFactory} to
     * provide the ftp server admin account.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    private static class TestUserManagerFactory implements UserManagerFactory {

        /**
         * {@inheritDoc}
         */
        @Override
        public UserManager createUserManager() {
            return new TestUserManager("admin",
                    new ClearTextPasswordEncryptor());
        }
    }

    /**
     * TestUserManager defines a mock {@link AbstractUserManager} to establish
     * valid users for the ftp server.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    private static class TestUserManager extends AbstractUserManager {
        private BaseUser testUser;
        private BaseUser anonUser;

        /**
         * Creates the TestUserManager with the {@code adminName} and the
         * {@code passwordEncryptor}.
         *
         * @param adminName {@link String}, the admin name
         * @param passwordEncryptor {@link PasswordEncryptor}, the password
         * encryptor to use
         */
        public TestUserManager(String adminName,
                PasswordEncryptor passwordEncryptor) {
            super(adminName, passwordEncryptor);

            testUser = new BaseUser();
            testUser.setAuthorities(Arrays
                    .asList(new Authority[] { new ConcurrentLoginPermission(1,
                            1) }));
            testUser.setEnabled(true);
            testUser.setHomeDirectory(TEST_USER_FTP_ROOT);
            testUser.setMaxIdleTime(10000);
            testUser.setName(TEST_USERNAME);
            testUser.setPassword(TEST_PASSWORD);

            anonUser = new BaseUser(testUser);
            anonUser.setName("anonymous");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public User getUserByName(String username) throws FtpException {
            if (TEST_USERNAME.equals(username)) {
                return testUser;
            } else if (anonUser.getName().equals(username)) {
                return anonUser;
            }

            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String[] getAllUserNames() throws FtpException {
            return new String[] { TEST_USERNAME, anonUser.getName() };
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void delete(String username) throws FtpException {
            //no opt
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void save(User user) throws FtpException {
            //no opt
            System.out.println("save");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean doesExist(String username) throws FtpException {
            return (TEST_USERNAME.equals(username) || anonUser.getName()
                    .equals(username)) ? true : false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public User authenticate(Authentication authentication)
                throws AuthenticationFailedException {
            if (UsernamePasswordAuthentication.class
                    .isAssignableFrom(authentication.getClass())) {
                UsernamePasswordAuthentication upAuth =
                        (UsernamePasswordAuthentication) authentication;

                if (TEST_USERNAME.equals(upAuth.getUsername())
                        && TEST_PASSWORD.equals(upAuth.getPassword())) {
                    return testUser;
                }

                if (anonUser.getName().equals(upAuth.getUsername())) {
                    return anonUser;
                }
            } else if (AnonymousAuthentication.class
                    .isAssignableFrom(authentication.getClass())) {
                return anonUser;
            }

            return null;
        }
    }
}
