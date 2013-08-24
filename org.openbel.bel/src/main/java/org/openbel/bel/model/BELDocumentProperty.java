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
package org.openbel.bel.model;

import java.util.HashMap;
import java.util.Map;

public enum BELDocumentProperty {
    AUTHOR("Authors"),
    CONTACT_INFO("ContactInfo"),
    COPYRIGHT("Copyright"),
    DESCRIPTION("Description"),
    DISCLAIMER("Disclaimer"),
    LICENSE("Licenses"),
    NAME("Name"),
    VERSION("Version");

    private static final Map<String, BELDocumentProperty> STRINGTOENUM =
            new HashMap<String, BELDocumentProperty>(values().length, 1F);
    static {
        for (final BELDocumentProperty e : values()) {
            STRINGTOENUM.put(e.name, e);
        }
    }

    private final String name;

    private BELDocumentProperty(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static BELDocumentProperty getDocumentProperty(final String s) {
        return STRINGTOENUM.get(s);
    }
}
