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
package org.openbel.framework.common;

import org.openbel.framework.common.cfg.SystemConfiguration;

public class DownloadConstants {

    /**
     * Defines the socket connection timeout when downloading files.
     */
    public static final int CONNECTION_TIMEOUT = 10000;

    /**
     * Defines the download directory that contains downloaded files.  This
     * directory should be located inside
     * {@link SystemConfiguration#getWorkingDirectory() working directory}.
     * The value is {@value}.
     */
    public static final String DOWNLOAD_DIRECTORY = "download";

    /**
     * Private constructor to prevent initialization.
     */
    private DownloadConstants() {
    }
}
