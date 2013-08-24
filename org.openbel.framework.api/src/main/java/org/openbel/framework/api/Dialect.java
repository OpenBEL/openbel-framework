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

import org.openbel.framework.api.Kam.KamNode;


/**
 * A {@link Dialect} provides an API for generating alternate labels for a
 * {@link KamNode}.
 * <p>
 * If the label computation is costly it is recommended that implementations
 * provide a caching strategy.
 * </p>
 *
 * @author Steve Ungerer
 */
public interface Dialect {

    /**
     * Obtain a label for a given {@link KamNode}.
     *
     * @param kamNode
     *            The {@link KamNode} to generate a label for, never
     *            <code>null</code>.
     * @return A {@link Dialect} specific label, never <code>null</code>.
     */
    String getLabel(KamNode kamNode);
}
