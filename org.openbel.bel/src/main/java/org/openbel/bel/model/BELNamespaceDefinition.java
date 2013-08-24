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

public class BELNamespaceDefinition extends BELObject {
    private static final long serialVersionUID = -1563073414296240878L;
    private final boolean nsDefault;
    private final String prefix;
    private final String resourceLocation;

    public BELNamespaceDefinition(final String prefix,
            final String resourceLocation, final boolean nsDefault) {
        if (prefix == null) {
            throw new IllegalArgumentException("name must be set");
        }

        if (resourceLocation == null) {
            throw new IllegalArgumentException("resourc location must be set");
        }

        this.prefix = clean(prefix);

        if (resourceLocation.startsWith("\"")
                && resourceLocation.endsWith("\"")) {
            this.resourceLocation = clean(resourceLocation.substring(1,
                    resourceLocation.length() - 1));
        } else {
            this.resourceLocation = clean(resourceLocation);
        }

        this.nsDefault = nsDefault;
    }

    public boolean isNsDefault() {
        return nsDefault;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }
}
