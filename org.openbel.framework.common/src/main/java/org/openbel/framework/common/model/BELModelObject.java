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

import java.io.Serializable;

/**
 * BEL common model base class.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface BELModelObject extends Serializable, Cloneable {

    /**
     * Creates a clone of this {@link BELModelObject BEL model object} and its
     * state.<br>
     * Implementations must ensure the clone is a <em>deep copy</em>.
     * <p>
     * This method should be used judiciously.
     * </p>
     *
     * @return {@link BELModelObject} clone, sharing no state with its source
     * object
     */
    BELModelObject clone();
}
