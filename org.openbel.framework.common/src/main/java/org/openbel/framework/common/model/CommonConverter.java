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
package org.openbel.framework.common.model;

/**
 * Interface for defining a converter between two reference types, where
 * {@code T1} is a {@link BELObject}.
 *
 * @param <T1> Generic type suitable for conversion to {@code <T2>}
 * @param <T2> Generic type suitable for conversion to {@code <T1>}
 */
public interface CommonConverter<T1 extends BELObject, T2> {

    /**
     * Converts {@code T2} to its {@link BELObject} representation.
     *
     * @param src {@code T2}
     * @return {@code T1}
     */
    public T1 convert(T2 src);

    /**
     * Converts the {@code T1} {@link BELObject} to its {@code T2}
     * representation.
     *
     * @param src {@code T1} {@link BELObject}
     * @return {@code T2}
     */
    public T2 convert(T1 src);

}
