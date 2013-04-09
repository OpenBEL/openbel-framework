/**
 * Copyright (C) 2012-2013 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
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
