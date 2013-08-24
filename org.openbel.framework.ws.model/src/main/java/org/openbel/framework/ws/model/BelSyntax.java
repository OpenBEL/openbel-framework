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
package org.openbel.framework.ws.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for BelSyntax.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BelSyntax">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SHORT_FORM"/>
 *     &lt;enumeration value="LONG_FORM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "BelSyntax")
@XmlEnum
public enum BelSyntax {

    /**
     *
     *                         Represents short form BEL. e.g. the protein abundance of ABC
     *                         will be displayed as "p(ABC)".
     *
     *
     */
    SHORT_FORM,

    /**
     *
     *                         Represents long form BEL. e.g. the protein abundance of ABC
     *                         will be displayed as "proteinAbundance(ABC)".
     *
     *
     */
    LONG_FORM;

    public String value() {
        return name();
    }

    public static BelSyntax fromValue(String v) {
        return valueOf(v);
    }

}
