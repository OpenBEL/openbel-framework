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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * FTPConnector implements the ftp client connector which is responsible for:<ul>
 * <li>authenticating with the ftp server</li>
 * <li>retrieves a file from the connected ftp server</li>
 * </ul>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class FTPConnector {
    /**
     * Defines the default {@link InputStream} to read the password from.
     */
    private static final InputStream DEFAULT_PWD_INPUT = System.in;

    /**
     * Defines the default username, <strong>{@value}</strong>,
     * when connecting to the ftp server.
     */
    private static final String DEFAULT_USER_NAME = "anonymous";

    /**
     * Defines the default port, <strong>{@value}</strong>, to use when
     * connecting to the ftp server.
     */
    private static final int DEFAULT_FTP_PORT = 21;

    /**
     * Defines the "commons net" ftp client implementation which actually
     * implements the FTP protocol.
     */
    private FTPClient ftpClient;

    /**
     * Defines the password {@link InputStream} to read the password from.
     *
     */
    private InputStream pwdInputStream;

    /**
     * Creates the ftp connector with the {@link #DEFAULT_PWD_INPUT} password
     * input stream.
     */
    public FTPConnector() {
        this.pwdInputStream = DEFAULT_PWD_INPUT;
    }

    /**
     * Creates the ftp connector with the {@code pwdInputStream}.
     *
     * @param pwdInputStream {@link InputStream}, the password input stream
     */
    public FTPConnector(InputStream pwdInputStream) {
        this.pwdInputStream = pwdInputStream;
    }

    /**
     * Sets the password input stream to read the password from.
     *
     * @param pwdInputStream {@link InputStream}, the password input stream
     * to read the password from
     */
    public void setPwdInputStream(InputStream pwdInputStream) {
        this.pwdInputStream = pwdInputStream;
    }

    /**
     * Connects and authenticates with the ftp server specified by the
     * {@code ftpUrl}.
     *
     * @param ftpUrl {@link URL}, the ftp server url
     * @throws ResourceDownloadError - Thrown if there was an ftp error
     * connecting or authenticating with the ftp server.
     */
    public void connectAndLogin(URL ftpUrl) throws ResourceDownloadError {
        if (!ftpUrl.getProtocol().equals("ftp")) {
            throw new InvalidArgument(
                    "The ftp connection does not support protocol '"
                            + ftpUrl.getProtocol() + "', only 'ftp'.");
        }

        //connect to ftp server
        String host = ftpUrl.getHost();
        int port = ftpUrl.getPort() == -1 ? DEFAULT_FTP_PORT : ftpUrl.getPort();
        ftpClient = new FTPClient();

        try {
            ftpClient.connect(host, port);
        } catch (Exception e) {
            final String url = ftpUrl.toString();
            final String msg = e.getMessage();
            throw new ResourceDownloadError(url, msg, e);
        }

        //login to user account
        String userInfo = ftpUrl.getUserInfo();
        String username = DEFAULT_USER_NAME;
        String password = "";
        if (userInfo != null) {
            if (userInfo.contains(":")) {
                //provided username & password so parse
                String[] userInfoTokens = userInfo.split("\\:");
                if (userInfoTokens.length == 2) {
                    username = userInfoTokens[0];
                    password = userInfoTokens[1];
                }
            } else {
                //provided only username
                username = userInfo;

                //prompt for password
                char[] pwd;
                try {
                    pwd =
                            PasswordPrompter.getPassword(pwdInputStream,
                                    "Connecting to '" + ftpUrl.toString()
                                            + "'.  Enter password for user '"
                                            + username + "': ");
                } catch (IOException e) {
                    final String name = ftpUrl.toString();
                    final String msg = e.getMessage();
                    throw new ResourceDownloadError(name, msg, e);
                }
                if (pwd == null) {
                    password = "";
                } else {
                    password = String.valueOf(pwd);
                }
            }
        }

        try {
            if (!ftpClient.login(username, password)) {
                final String name = ftpUrl.toString();
                final String msg = "Login error for username and password";
                throw new ResourceDownloadError(name, msg);
            }
        } catch (IOException e) {
            final String name = ftpUrl.toString();
            final String msg = "Login error for username and password";
            throw new ResourceDownloadError(name, msg, e);
        }

        try {
            ftpClient.pasv();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            final String url = ftpUrl.toString();
            final String msg = "Error setting passive mode or transfer type";
            throw new ResourceDownloadError(url, msg, e);
        }
    }

    /**
     * Retrieves a file, specified by {@code remotePath}, from the ftp server
     * and saves it locally to {@code localPath}.
     *
     * @param remotePath {@link String}, the remote path
     * @param localPath {@link String}, the local path
     * @throws FileNotFoundException - Thrown if the file exists but is a directory
     * rather than a regular file, does not exist but cannot be created,
     * or cannot be opened for any other reason.
     * @throws IOException - Thrown if an io error occurred saving the
     * {@code remotePath} to {@code localPath}.
     */
    public void retrieveFile(String remotePath, String localPath)
            throws FileNotFoundException, IOException {
        FileOutputStream localFileOutputStream =
                new FileOutputStream(localPath);
        ftpClient.retrieveFile(remotePath, localFileOutputStream);
        localFileOutputStream.close();
    }
}
