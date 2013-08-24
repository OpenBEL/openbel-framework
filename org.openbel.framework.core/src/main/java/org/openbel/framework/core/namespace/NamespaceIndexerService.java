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
package org.openbel.framework.core.namespace;

import java.io.File;

import org.openbel.framework.common.model.DataFileIndex;
import org.openbel.framework.core.indexer.IndexingFailure;

/**
 * NamespaceIndexerService defines a service to index namespace values to
 * provide an efficient method of looking up namespace vales for BEL document
 * syntax verification.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface NamespaceIndexerService {

    /**
     * Indexes namespace values, encoded in {@code rawNamespaceFile}, for the
     * namespace defined in {@code namespace}. This will generate a
     * {@link DataFileIndex}, including namespace header and namespace index,
     * that can be used for efficient lookups of namespace values and semantic
     * function flags.
     *
     * @param resourceLocation {@link String}, the namespace resource location,
     * which cannot be null
     * @param rawNamespaceFile {@link File}, the plain-text BEL namespace
     * document
     * @return {@link DataFileIndex}, the namespace index which consists of the
     * namespace header and index.
     * @throws IndexingFailure Thrown if there was an error parsing the
     * namespace header or building the namespace index
     */
    public DataFileIndex indexNamespace(final String resourceLocation,
            File rawNamespaceFile) throws IndexingFailure;
}
