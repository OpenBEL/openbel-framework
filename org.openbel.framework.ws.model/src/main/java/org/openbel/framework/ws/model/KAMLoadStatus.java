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
 * <p>
 * Java class for KAMLoadStatus.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="KAMLoadStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="IN_PROCESS"/>
 *     &lt;enumeration value="COMPLETE"/>
 *     &lt;enumeration value="FAILED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "KAMLoadStatus")
@XmlEnum
public enum KAMLoadStatus {

    /**
     * Indicates a KAM that is being loaded.
     */
    IN_PROCESS,

    /**
     * Indicates a KAM that has been loaded.
     */
    COMPLETE,

    /**
     * Indicates a KAM that has failed to load.
     */
    FAILED;

    public String value() {
        return name();
    }

    public static KAMLoadStatus fromValue(String v) {
        return valueOf(v);
    }

}
