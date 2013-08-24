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
package org.openbel.framework.core.protonetwork;

import static org.openbel.framework.common.BELUtilities.nulls;

import java.io.File;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;

/**
 * TextProtoNetworkDescriptor represents the file descriptor for the
 * text-encoded proto network files.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class TextProtoNetworkDescriptor implements ProtoNetworkDescriptor {

    /**
     * Defines the {@link String base path} for the
     * {@link ProtoNetwork ProtoNetwork}.
     */
    private String basePath;

    /**
     * Defines {@link File all symbol table files}.
     */
    private File[] pnFiles;

    /**
     * Construct passing the {@link String base path} and
     * {@link File all symbol table files}.
     *
     * @param basePath the {@link String proto network base path}
     * @param pnfiles the {@link File all symbol table files}
     * @throws InvalidArgument Thrown if <tt>basePath</tt> is null or
     * <tt>pnfiles</tt> contains a null
     */
    public TextProtoNetworkDescriptor(final String basePath, File... pnfiles) {
        if (basePath == null) {
            throw new InvalidArgument("basePath", basePath);
        }

        if (nulls((Object[]) pnfiles)) {
            throw new InvalidArgument("Proto network files contain null(s)");
        }

        this.pnFiles = pnfiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBasePath() {
        return basePath;
    }

    /**
     * Return {@link File all symbol table files}.
     *
     * @return all symbol table files
     */
    public File[] getSymbolTables() {
        return pnFiles;
    }
}
