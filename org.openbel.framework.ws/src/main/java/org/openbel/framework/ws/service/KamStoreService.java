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
package org.openbel.framework.ws.service;

import java.util.List;

import org.openbel.framework.ws.model.AnnotationType;
import org.openbel.framework.ws.model.BelDocument;
import org.openbel.framework.ws.model.BelStatement;
import org.openbel.framework.ws.model.BelTerm;
import org.openbel.framework.ws.model.Citation;
import org.openbel.framework.ws.model.CitationType;
import org.openbel.framework.ws.model.Kam;
import org.openbel.framework.ws.model.KamEdge;
import org.openbel.framework.ws.model.KamFilter;
import org.openbel.framework.ws.model.KamHandle;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.Namespace;
import org.openbel.framework.ws.model.Node;

/**
 * TODO Provide documentation
 */
public interface KamStoreService {

    public List<Kam> getCatalog() throws KamStoreServiceException;

    public List<Namespace> getNamespaces(final KamHandle kamHandle)
            throws KamStoreServiceException;

    public List<AnnotationType> getAnnotationTypes(final KamHandle kamHandle)
            throws KamStoreServiceException;

    public List<BelDocument> getBelDocuments(final KamHandle kamHandle)
            throws KamStoreServiceException;

    public List<Citation> getCitations(final KamHandle kamHandle,
            final CitationType citationType, final List<String> list,
            final BelDocument belDocument) throws KamStoreServiceException;

    public List<BelTerm> getSupportingTerms(final KamNode kamNode)
            throws KamStoreServiceException;

    public List<BelStatement> getSupportingEvidence(final KamEdge kamEdge,
            final KamFilter kamFilter) throws KamStoreServiceException;

    public List<KamNode> getKamNodes(final KamHandle kamHandle,
            final List<Node> nodes) throws KamStoreServiceException;
}
