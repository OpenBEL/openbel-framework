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


/**
 * {@link KamElement} represents a part of a particular {@link Kam kam graph}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface KamElement extends KamStoreObject {

    /**
     * Retrieves the {@link Kam} this element is derived from.
     *
     * @return the {@link Kam kam}, which may be null
     */
    Kam getKam();

}
