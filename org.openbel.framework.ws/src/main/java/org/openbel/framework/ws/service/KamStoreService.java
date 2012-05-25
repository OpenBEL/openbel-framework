/**
 * Copyright (C) 2012 Selventa, Inc.
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
