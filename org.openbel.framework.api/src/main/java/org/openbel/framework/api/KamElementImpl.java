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
package org.openbel.framework.api;


public abstract class KamElementImpl extends KamStoreObjectImpl implements
        KamElement {

    private final Kam kam;

    /**
     * Constructs the {@link KamElement}.
     *
     * @param kam the {@link Kam kam} this element is derived from
     * @param id the database {@link Integer id} that identifies this element
     * in the {@link Kam kam}
     */
    public KamElementImpl(Kam kam, Integer id) {
        super(id);
        this.kam = kam;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Kam getKam() {
        return kam;
    }
}
