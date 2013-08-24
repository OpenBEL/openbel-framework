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
