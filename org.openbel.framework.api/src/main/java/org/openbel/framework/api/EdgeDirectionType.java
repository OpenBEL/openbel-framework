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
 * Finite set of allowable graph edge types.
 *
 * @author julianray
 *
 */
public enum EdgeDirectionType {

    FORWARD(1, "Forward"),
    REVERSE(-1, "Reverse"),
    BOTH(0, "Both");

    private Integer id;
    private String displayValue;

    /**
     *
     * @param id
     * @param displayValue
     */
    private EdgeDirectionType(Integer id, String displayValue) {
        this.id = id;
        this.displayValue = displayValue;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return displayValue;
    }

    /**
     *
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     *
     */
    public EdgeDirectionType getEnum() {
        return this;
    }

    public static EdgeDirectionType forId(Integer id) {
        for (EdgeDirectionType dir : EdgeDirectionType.values()) {
            if (dir.getId().equals(id)) {
                return dir;
            }
        }
        return null;
    }

}
