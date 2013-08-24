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
package org.openbel.bel.model;

import java.util.List;
import java.util.Set;

public class BELDocument extends BELObject {
    private static final long serialVersionUID = 7238804208223030139L;
    private final BELDocumentHeader documentHeader;
    private final Set<BELAnnotationDefinition> annotationDefinitions;
    private final Set<BELNamespaceDefinition> namespaceDefinitions;
    private final List<BELStatementGroup> belStatementGroups;

    public BELDocument(
            final BELDocumentHeader documentHeader,
            final Set<BELAnnotationDefinition> annotationDefinitions,
            final Set<BELNamespaceDefinition> namespaceDefinitions,
            final List<BELStatementGroup> belStatementGroups) {
        if (documentHeader == null) {
            throw new IllegalArgumentException("document header must be set");
        }

        this.documentHeader = documentHeader;
        this.annotationDefinitions = annotationDefinitions;
        this.namespaceDefinitions = namespaceDefinitions;
        this.belStatementGroups = belStatementGroups;
    }

    /**
     * Returns the document header.
     *
     * @return the {@link BELDocumentHeader}, will not be null
     */
    public BELDocumentHeader getDocumentHeader() {
        return documentHeader;
    }

    /**
     * Returns the {@link Set} of {@link BELAnnotationDefinition} objects.
     *
     * @return the {@link Set} of {@link BELAnnotationDefinition} objects,
     * may be null
     */
    public Set<BELAnnotationDefinition> getAnnotationDefinitions() {
        return annotationDefinitions;
    }

    /**
     * Returns the {@link Set} of {@link BELNamespaceDefinition} objects.
     *
     * @return the {@link Set} of {@link BELNamespaceDefinition} objects,
     * may be null
     */
    public Set<BELNamespaceDefinition> getNamespaceDefinitions() {
        return namespaceDefinitions;
    }

    /**
     * Returns the {@link List} of {@link BELStatementGroup} objects.
     *
     * @return the {@link List} of {@link BELStatementGroup} objects,
     * may be null
     */
    public List<BELStatementGroup> getBelStatementGroups() {
        return belStatementGroups;
    }
}
