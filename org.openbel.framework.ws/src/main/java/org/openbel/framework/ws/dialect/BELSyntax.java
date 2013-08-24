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
package org.openbel.framework.ws.dialect;

/**
 * Defines the different syntaxes of BEL.
 *
 * @author Steve Ungerer
 */
public enum BELSyntax {
    /**
     * Short form BEL. Uses abbreviations for functions. E.g. protein abundance
     * of ABC will be displayed as p(ABC).
     */
    SHORT_FORM,
    /**
     * Long form BEL. Functions display full name. E.g. protein abundance of ABC
     * will be displayed as proteinAbundance(ABC).
     */
    LONG_FORM
}
