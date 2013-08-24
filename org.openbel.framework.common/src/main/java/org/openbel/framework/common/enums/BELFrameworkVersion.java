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
package org.openbel.framework.common.enums;

/**
 * Declares the current version of the OpenBEL Framework.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class BELFrameworkVersion {

    /**
     * Defines the current version number of the OpenBEL Framework - {@value}
     */
    public static final String VERSION_NUMBER = "V2.0.1";

    /**
     * Defines the current version label of the OpenBEL Framework - {@value}
     */
    public static final String VERSION_LABEL = "OpenBEL Framework "
            + VERSION_NUMBER;

    /**
     * Prevent construction.
     */
    private BELFrameworkVersion() {

    }
}
