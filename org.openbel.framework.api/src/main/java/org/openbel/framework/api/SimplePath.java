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
package org.openbel.framework.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;

/**
 * SimplePath represents a path within a {@link Kam} without cycles.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public final class SimplePath {
    private final Kam kam;
    private final KamNode source;
    private final KamNode target;
    private final List<KamEdge> edgeList;

    /**
     * Construct a new KAM Path.
     * 
     * @param kam the {@link Kam} the path was derived from
     * @param source the source {@link KamNode}, must not be <code>null</code>
     * @param target the target {@link KamNode}, may be <code>null</code>
     * @param edges a list of {@link KamEdge edges}; may be <code>empty</code>
     * @throws InvalidArgument Thrown if any of {@code kam}, {@code source}, or
     * {@code edges} is null, or if the resulting path does not make logical
     * sense (e.g., {@code kam} is not the {@code source}
     * {@link KamNode#getKam() node's KAM})
     */
    protected SimplePath(final Kam kam, final KamNode source,
            final KamNode target, final List<KamEdge> edges) {
        if (kam == null) {
            throw new InvalidArgument("kam", kam);
        }
        if (source == null) {
            throw new InvalidArgument("source", source);
        }
        if (edges == null) {
            throw new InvalidArgument("edges", edges);
        }
        if (!kam.equals(source.getKam())) {
            throw new InvalidArgument(
                    "Source node must be part of the Path Kam");
        }

        this.kam = kam;
        this.source = source;
        this.target = target;

        if (target != null && !kam.equals(target.getKam())) {
            throw new InvalidArgument(
                    "Target node must be part of the Path Kam");
        }

        if (target == null && !edges.isEmpty()) {
            throw new InvalidArgument(
                    "Cannot add edges to a Path with a null target");
        }

        List<KamEdge> kamEdges = new ArrayList<KamEdge>(edges.size());
        for (KamEdge e : edges) {
            if (!kam.equals(e.getKam())) {
                throw new InvalidArgument("Edge must be part of the Path Kam");
            }
            kamEdges.add(e);
        }
        this.edgeList = Collections.unmodifiableList(kamEdges);
    }

    public Kam getKam() {
        return kam;
    }

    public KamNode getSource() {
        return source;
    }

    public KamNode getTarget() {
        return target;
    }

    /**
     * @return an unmodifiable {@link List} of {@link KamEdge}s
     */
    public List<KamEdge> getEdges() {
        return edgeList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("Source:")
                .append(source.getLabel());
        if (target != null) {
            sb.append(" to Target:")
                    .append(target.getLabel());
        }
        if (edgeList != null && !edgeList.isEmpty()) {
            sb.append(" with edges ").append(edgeList);
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // TODO: replace hashcodebuilder
        return new HashCodeBuilder(1, 31)
                .append(kam)
                .append(source)
                .append(target)
                .append(edgeList)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        SimplePath other = (SimplePath) obj;
        // TODO: replace EqualsBuilder
        return new EqualsBuilder()
                .append(kam, other.kam)
                .append(source, other.source)
                .append(target, other.target)
                .append(edgeList, other.edgeList)
                .isEquals();
    }
}
