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
package org.openbel.framework.common.download;

import java.io.IOException;
import java.net.URL;

import org.openbel.framework.common.InvalidArgument;

/**
 * {@link DownloadHandler} defines a class responsible for downloading the
 * contents of a {@link URL url} and associated checksum if provided.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface DownloadHandler {

    /**
     * Retrieve the contents of the {@link URL url} and optional checksum.
     *
     * @param url the {@link URL url} to download contents from, which cannot
     * be {@code null}
     * @return the {@link DownloadFile download file} containing file contents
     * and an optional checksum
     * @throws IOException Thrown if an IO error occurred downloading the
     * {@link URL url}
     * @throws InvalidArgument Thrown if {@code url} is {@code null}
     */
    public DownloadFile download(final URL url) throws IOException;
}
