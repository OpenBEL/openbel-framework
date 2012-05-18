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
package org.openbel.framework.core.kamstore.model;

import java.util.Collection;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.core.kamstore.data.jdbc.KAMCatalogDao.KamInfo;
import org.openbel.framework.core.kamstore.model.Kam;
import org.openbel.framework.core.kamstore.model.KamImpl;
import org.openbel.framework.core.kamstore.model.Kam.KamEdge;
import org.openbel.framework.core.kamstore.model.Kam.KamNode;

/**
 * KamBuilder defines a builder object used to assemble a {@link Kam}.  The
 * builder allows for:
 * <ul>
 * <li>adding existing (already kam-bound) {@link KamNode} and
 * {@link KamEdge} objects</li>
 * <li>creating new (not backed by kam) {@link KamNode} and {@link KamEdge}
 * objects</li>
 * <li>remove existing {@link KamNode} and {@link KamEdge} objects</li>
 * </ul>
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class KamBuilder {
    private final KamImpl targetKam;
    private int nodeIdCounter;
    private int edgeIdCounter;

    /**
     * Construct from the provided {@link KamInfo kamInfo} object to maintain
     * a reference to the source {@link Kam}.
     * 
     * <p>
     * The {@link KamInfo source kam reference} is used to construct the
     * {@link KamBuilder builder}'s {@link Kam}.  References back to the
     * KamStore data (i.e. supporting terms and evidence) are <em>not</em>
     * supported with this constructor.
     * </p>
     * 
     * @param kamInfo {@link KamInfo}, the reference to the source {@link Kam}
     */
    public KamBuilder(final KamInfo kamInfo) {
        if (kamInfo == null) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }

        nodeIdCounter = 0;
        edgeIdCounter = 0;
        targetKam = new KamImpl(kamInfo);
    }

    /**
     * Construct with the structure from the provided {@link Kam KAM}.
     * 
     * <p>
     * The topology from the {@link Kam sourceKam} is built into the
     * {@link KamBuilder builder}'s {@link Kam kam} instance.  The
     * {@link KamNode kam node} and {@link KamEdge kam edge} ids are preserved
     * to provide reference back to the KamStore data (i.e supporting terms
     * and evidence).
     * </p>
     * 
     * @param kam {@link KamInfo} the source kam, which cannot be null
     * @throws InvalidArgument Thrown if <tt>sourceKam</tt>, <tt>nodes</tt>,
     * or <tt>edges</tt> is null
     */
    public KamBuilder(final Kam kam) {
        if (kam == null) {
            throw new InvalidArgument("sourceKam", kam);
        }

        // prepopulate target kam with source kam nodes
        targetKam = new KamImpl(kam.getKamInfo());

        Collection<KamNode> nodecol = kam.getNodes();
        int maxNodeId = 0;
        for (final KamNode node : nodecol) {
            addExistingNode(node);
            maxNodeId = Math.max(node.getId(), maxNodeId);
        }
        nodeIdCounter = maxNodeId + 1;

        final Collection<KamEdge> edgecol = kam.getEdges();
        int maxEdgeId = 0;
        for (final KamEdge edge : edgecol) {
            final KamNode source =
                    targetKam.findNode(edge.getSourceNode().getId());
            final KamNode target =
                    targetKam.findNode(edge.getTargetNode().getId());
            addExistingEdge(edge.getId(), source, edge.getRelationshipType(),
                    target);
            maxEdgeId = Math.max(edge.getId(), maxEdgeId);
        }
        edgeIdCounter = maxEdgeId + 1;
    }

    /**
     * Adds an existing {@link KamNode} previously associated with the source
     * kam.  The kam node will maintain its node id which allows its associated
     * data to be retrieved from the resulting kam.
     * 
     * @param kamNode {@link KamNode} the existing kam node to be added to the
     * resulting kam, which cannot be null
     * @return the {@link KamNode} object added to the resulting kam which will
     * be a new instance populated from the <tt>kamNode</tt> parameter
     * @throws InvalidArgument Thrown if <tt>kamNode</tt> is null
     */
    public KamNode addExistingNode(final KamNode kamNode) {
        if (kamNode == null) {
            throw new InvalidArgument("kamNode", kamNode);
        }

        if (targetKam.contains(kamNode)) {
            return targetKam.findNode(kamNode.getId());
        }

        this.nodeIdCounter = Math.max(kamNode.getId() + 1, nodeIdCounter);

        return targetKam.createNode(kamNode.getId(), kamNode.getFunctionType(),
                kamNode.getLabel());
    }

    /**
     * Removes the <tt>kamNode</tt> {@link KamNode} object from the resulting
     * kam if it exists.
     * 
     * @param kamNode {@link KamNode} the existing kam node to remove, which
     * cannot be null
     * @throws InvalidArgument Thrown if <tt>kamNode</tt> is null
     */
    public void removeExistingNode(final KamNode kamNode) {
        if (kamNode == null) {
            throw new InvalidArgument("kamNode", kamNode);
        }

        if (targetKam.contains(kamNode)) {
            targetKam.removeNode(kamNode);
        }
    }

    /**
     * Creates a new {@link KamNode} in the resulting kam.  This new kam node
     * obtains a new node id that is not associated to a kam in the KAMStore.
     * This means that associated data cannot be retrieved for it.
     * 
     * @param function {@link FunctionEnum} the kam node's function, which
     * cannot be null
     * @param label {@link String} the kam node's labe, which cannot be null
     * @return the {@link KamNode} object added to the resulting kam
     */
    public KamNode createNewNode(final FunctionEnum function,
            final String label) {
        if (function == null) {
            throw new InvalidArgument("function", function);
        }

        if (label == null) {
            throw new InvalidArgument("label", label);
        }

        return targetKam.createNode(nextNewNodeId(), function, label);
    }

    public KamNode replaceNode(final KamNode kamNode,
            final FunctionEnum function, final String label) {
        return targetKam.replaceNode(kamNode, function, label);
    }

    /**
     * Adds an existing {@link KamEdge edge} to the target kam.  The existing
     * edge is identified by the provided <tt>edgeId</tt> to maintain reference
     * to the source kam's edge.
     * 
     * <p>
     * To add existing nodes for this <tt>kamEdge</tt> you must first call
     * {@link KamBuilder#createNewNode(FunctionEnum, String) createNewNode}.
     * The source and target {@link KamNode nodes} must exist in the target
     * {@link Kam kam} in order for this new {@link KamEdge edge} to be valid.
     * </p>
     * 
     * @param edgeId {@link Integer}, the existing {@link KamEdge edge} id in
     * the {@link Kam source kam}, which cannot be null
     * @param sourceNode {@link KamNode}, the source node from the
     * {@link Kam target kam}, which cannot be null
     * @param reltype {@link RelationshipType}, the existing
     * {@link KamEdge edge} relationship, which cannot be null
     * @param targetNode {@link KamNode}, the target node from the
     * {@link Kam target kam}, which cannot be null
     * @return the created {@link KamEdge edge} in the {@link Kam target}
     * @throws InvalidArgument - Thrown if any parameter is <tt>null</tt>
     */
    public KamEdge addExistingEdge(final Integer edgeId,
            final KamNode sourceNode,
            final RelationshipType reltype, final KamNode targetNode) {
        if (edgeId == null) {
            throw new InvalidArgument("edgeId", edgeId);
        }

        if (sourceNode == null) {
            throw new InvalidArgument("sourceNode", sourceNode);
        }

        if (reltype == null) {
            throw new InvalidArgument("reltype", reltype);
        }

        if (targetNode == null) {
            throw new InvalidArgument("targetNode", targetNode);
        }

        this.edgeIdCounter = Math.max(edgeId + 1, edgeIdCounter);

        return targetKam.createEdge(edgeId, sourceNode,
                reltype, targetNode);
    }

    /**
     * Removes the <tt>kamEdge</tt> {@link KamEdge} object from the resulting
     * kam if it exists.
     * 
     * @param kamEdge {@link KamEdge} the existing kam edge to remove, which
     * cannot be null
     * @throws InvalidArgument Thrown if <tt>kamEdge</tt> is null
     */
    public void removeExistingEdge(final KamEdge kamEdge) {
        if (targetKam.contains(kamEdge)) {
            targetKam.removeEdge(kamEdge);
        }
    }

    /**
     * Creates a new {@link KamEdge} in the resulting kam.  This new kam edge
     * obtains a new edge id that is not associated to a kam in the KAMStore.
     * This means that associated data cannot be retrieved for it.
     * 
     * @param sourceNode {@link KamNode} the source node for the edge, which
     * cannot be null
     * @param relationship {@link RelationshipType} the relationship type for
     * the edge, which cannot be null
     * @param targetNode {@link KamNode} the target node for the edge, which
     * cannot be null
     * @return the {@link KamEdge} object added to the resulting kam
     * @throws InvalidArgument Thrown if <tt>sourceNode</tt>,
     * <tt>relationship</tt>, or <tt>targetNode</tt> is null
     */
    public KamEdge createNewEdge(final KamNode sourceNode,
            final RelationshipType relationship, final KamNode targetNode) {
        if (sourceNode == null) {
            throw new InvalidArgument("sourceNode", sourceNode);
        }

        if (relationship == null) {
            throw new InvalidArgument("relationship", relationship);
        }

        if (targetNode == null) {
            throw new InvalidArgument("targetNode", targetNode);
        }

        return targetKam.createEdge(nextNewEdgeId(), sourceNode, relationship,
                targetNode);
    }

    public KamEdge replaceEdge(final KamEdge existingEdge,
            final FunctionEnum sourceFunction, final String sourceLabel,
            final RelationshipType relationship,
            final FunctionEnum targetFunction, final String targetLabel) {
        return targetKam.replaceEdge(existingEdge, sourceFunction, sourceLabel,
                relationship, targetFunction, targetLabel);
    }

    public KamEdge replaceEdge(final KamEdge existingEdge,
            final KamNode sourceNode,
            final RelationshipType relationship,
            final KamNode targetNode) {
        return targetKam.replaceEdge(existingEdge,
                sourceNode.getFunctionType(), sourceNode.getLabel(),
                relationship, targetNode.getFunctionType(),
                targetNode.getLabel());
    }

    /**
     * Returns the resulting {@link Kam} built up by this class.
     * 
     * @return the {@link Kam}
     */
    public Kam getKam() {
        return targetKam;
    }

    /**
     * Obtain the next new node id.  The node id domain is <tt>&lt;= -1</tt> to
     * differentiate it from positive ids backed by a {@link KAMStore}.
     * 
     * @return the next new node id <tt>int</tt>
     */
    private int nextNewNodeId() {
        return nodeIdCounter++;
    }

    /**
     * Obtain the next new edge id.  The edge id domain is <tt>&lt;= -1</tt> to
     * differentiate it from positive ids backed by a {@link KAMStore}.
     * 
     * @return the next new edge id <tt>int</tt>
     */
    private int nextNewEdgeId() {
        return edgeIdCounter++;
    }
}
