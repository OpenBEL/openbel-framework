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
 * Represents components of the Biological Expression Language (BEL)
 * <p>
 * TODO move {@link BELObject} hierarchy to BEL project<br>
 * TODO Reconsider hierarchy: should BELObject be the base for a LanguageElement
 * and BELModelObject?
 * </p>
 *
 * @author Steve Ungerer
 */
public interface BELObject extends Cloneable, Serializable {

    /**
     * Serializes the {@link BELObject} to long form BEL syntax.
     *
     * @return
     */
    String toBELLongForm();

    /**
     * Serializes the {@link BELObject} to short form BEL syntax.
     *
     * @return
     */
    String toBELShortForm();

    /**
     * Creates a clone of this {@link BELObject BEL object} and its state.<br>
     * Implementations must ensure the clone is a <em>deep copy</em>.
     * <p>
     * This method should be used judiciously.
     * </p>
     *
     * @return {@link BELObject} clone, sharing no state with its source object
     */
    BELObject clone();
}
