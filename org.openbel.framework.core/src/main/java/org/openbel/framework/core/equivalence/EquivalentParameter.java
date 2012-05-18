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
package org.openbel.framework.core.equivalence;

import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;

/**
 * Immutable specialization of a common model {@link Parameter parameter}
 * allowing for Java equivalence relations to function in tandem with framework
 * parameter equivalencing.
 * <p>
 * If a bucket is provided during time of construction, the equivalence relation
 * for these objects will be based solely on the bucket field. The effect of
 * this is that for two equivalence parameter objects:
 * 
 * <pre>
 * <code>
 * x: (NAMESPACE1:VALUE1)
 * y: (NAMESPACE2:VALUE2)
 * </code>
 * </pre>
 * 
 * If {@code x} and {@code y} have the same bucket, i.e., they both express the
 * same biological knowledge, the Java object representations are equal.
 * Therefore {@code x.equals(y)} and {@code y.equals(x)}. Note that for
 * parameters with no equivalences, these objects function identically to normal
 * {@link Parameter} objects with the exception of their immutable nature.
 */
public class EquivalentParameter extends Parameter {
    private static final long serialVersionUID = 6716687045414082293L;
    private final SkinnyUUID bucket;
    private final int hash;

    /**
     * Creates an equivalent parameter with the optional properties similar to
     * {@link Parameter#Parameter(Namespace, String) super} with the exception
     * of an additional bucket parameter.
     * 
     * @param ns {@link Namespace}
     * @param value Value
     * @param bucket Equivalence bucket
     */
    public EquivalentParameter(Namespace ns, String value, SkinnyUUID bucket) {
        super(ns, value);
        this.bucket = bucket;
        hash = hash();
    }

    private int hash() {
        if (bucket != null) {
            return bucket.hashCode();
        }
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EquivalentParameter)) return false;

        final EquivalentParameter ep = (EquivalentParameter) o;
        if (bucket != null) {
            return bucket.equals(ep.bucket);
        }
        return super.equals(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNamespace(Namespace namespace) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(String value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtain the equivalence bucket for this parameter
     * @return
     */
    public SkinnyUUID getBucket() {
        return bucket;
    }
}
