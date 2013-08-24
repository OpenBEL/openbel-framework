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
package org.openbel.framework.core.protocol;

/**
 * {@link ProtocolHandlerConstants} defines protocol handler constants encapsulated for
 * documentation/organizational purposes.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface ProtocolHandlerConstants {

    /**
     * Defines the socket connection timeout when downloading files.
     */
    public static final int CONNECTION_TIMEOUT = 10000;

    /**
     * Defines the standard SSH server port to connect to.
     */
    public static final int DEFAULT_SSH_PORT = 22;

    /**
     * Defines the temporary file prefix for bel framework files.
     */
    public static final String BEL_FRAMEWORK_TMP_FILE_PREFIX = "BELFRAMEWORK";
}
