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
package org.openbel.framework.core.equivalence;

import java.io.IOException;
import java.util.List;

import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.protocol.ResourceDownloadError;

public interface ParameterEquivalencer {

    public void openAllEquivalences() throws ResourceDownloadError,
            IndexingFailure, IOException;

    public List<Parameter> findEquivalences(Parameter sourceParameter)
            throws ResourceDownloadError, IndexingFailure, IOException;

    public List<Parameter> findEquivalences(SkinnyUUID sourceUUID)
            throws ResourceDownloadError, IndexingFailure, IOException;

    public Parameter findEquivalence(Parameter sourceParameter,
            Namespace destinationNamespace) throws ResourceDownloadError,
            IndexingFailure, IOException;

    public Parameter findEquivalence(SkinnyUUID sourceUUID,
            Namespace destinationNamespace) throws ResourceDownloadError,
            IndexingFailure, IOException;

    public void closeAllEquivalences() throws ResourceDownloadError,
            IndexingFailure, IOException;

    public SkinnyUUID getUUID(Parameter param) throws ResourceDownloadError,
            IndexingFailure, IOException;
}
