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
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.openbel.framework.core.protocol.ProtocolHandlerConstants;
import org.openbel.framework.core.protocol.ResourceDownloadError;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * SftpProtocolHandler implements a {@link ProtocolHandler} that can read
 * from a <em><strong>sftp</strong></em> url.  This protocol does not
 * implement the
 * <a href="http://tools.ietf.org/html/draft-ietf-secsh-filexfer-13">IETF Version 6 draft</a>
 * since the work was never published.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class SftpProtocolHandler implements ProtocolHandler {
    private UserInfo ui;

    /**
     * Creates the sftp protocol handler using a default user info.
     *
     * @see DefaultUserInfo
     */
    public SftpProtocolHandler() {
        ui = new DefaultUserInfo();
    }

    /**
     * Sets the user info for the jsch ssh client connection.
     *
     * @param ui {@link UserInfo}, the user info object to request password
     * authentication
     */
    public void setUserInfo(UserInfo ui) {
        this.ui = ui;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File downloadResource(final String url, final String path)
            throws ResourceDownloadError {
        JSch jsch = new JSch();

        String[] sftpPath = url.substring(7).split("\\@");
        final String[] userCreds = sftpPath[0].split("\\:");

        try {
            String host;
            int port = ProtocolHandlerConstants.DEFAULT_SSH_PORT;
            String filePath = sftpPath[1].substring(sftpPath[1].indexOf('/'));

            String[] location = sftpPath[1].split("\\/");
            if (location[0].contains(":")) {
                String[] hostPort = location[0].split("\\:");

                host = hostPort[0];
                port = Integer.parseInt(hostPort[1]);
            } else {
                host = location[0];
            }

            if (userCreds == null || userCreds.length == 0) {
                throw new UnsupportedOperationException(
                        "Non-specified user in sftp URL not supported yet.");
            }

            Session session = jsch.getSession(userCreds[0], host, port);
            session.setConfig("StrictHostKeyChecking", "no"); //don't validate against a known_hosts file
            session.setConfig("PreferredAuthentications",
                    "password,gssapi-with-mic,publickey,keyboard-interactive");

            if (userCreds.length == 1) {
                session.setUserInfo(ui);
            } else {
                session.setPassword(userCreds[1]);
            }

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp c = (ChannelSftp) channel;

            File downloadFile = new File(path);
            FileOutputStream tempFileOutputStream =
                    new FileOutputStream(downloadFile);

            IOUtils.copy(c.get(filePath), tempFileOutputStream);
            channel.disconnect();
            session.disconnect();

            return downloadFile;
        } catch (Exception e) {
            final String msg = "Error downloading namespace";
            throw new ResourceDownloadError(url, msg, e);
        }
    }

    /**
     * DefaultUserInfo defines a jsch {@link UserInfo} object that requests
     * the user's password from {@link System#in}.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    public class DefaultUserInfo implements UserInfo {
        /**
         * Defines the field where the entered password is stored.
         */
        protected String p;

        /**
         * Return a {@code null} passphrase.
         *
         * @return null
         */
        @Override
        public String getPassphrase() {
            return null;
        }

        /**
         * Return the captured password.
         *
         * @return {@link String}, the captured password
         */
        @Override
        public String getPassword() {
            return p;
        }

        /**
         * Prompt a {@code message} to the user that solicits their password.
         *
         * @return {@link String}, the message to solicit the user's password
         */
        @Override
        public boolean promptPassword(String message) {
            System.out.print(message + ": ");

            try {
                char[] pb = PasswordPrompter.getPassword(System.in, message);
                p = new String(pb);
            } catch (IOException e) {
                p = null;
            }

            return true;
        }

        /**
         * Return false.
         *
         * @param message {@link String}, the message to solicit the user's
         * passphrase
         * @return false
         */
        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        /**
         * Return false.
         *
         * @param message {@link String}, the message to solicit if the user
         * wants to continue authentication
         * @return false
         */
        @Override
        public boolean promptYesNo(String message) {
            return false;
        }

        /**
         * Outputs an authentication message to {@link System#out}.
         *
         * @param message {@link String}, the authentication message to output
         */
        @Override
        public void showMessage(String message) {
            System.out.println(message);
        }
    }
}
