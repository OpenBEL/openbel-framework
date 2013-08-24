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

import java.util.HashMap;
import java.util.Map;

import org.openbel.framework.common.model.Namespace;

/**
 * Constants for {@link Namespace} domains.
 *
 * @author Steve Ungerer
 */
public enum NamespaceDomain {
    Gene("Gene and Gene Products"),
    BiologicalProcess("BiologicalProcess"),
    Chemical("Chemical");

    private String domainString;

    private NamespaceDomain(String domainString) {
        this.domainString = domainString;
    }

    /**
     * Retrieve the domain string to which this {@link NamespaceDomain} maps.
     *
     * @return
     */
    public String getDomainString() {
        return domainString;
    }

    private static Map<String, NamespaceDomain> DSMAP =
            new HashMap<String, NamespaceDomain>();
    static {
        for (NamespaceDomain nd : values()) {
            DSMAP.put(nd.getDomainString(), nd);
        }
    }

    /**
     * Retrieve the {@link NamespaceDomain} constant for the given domain
     * string.
     *
     * @param domainString
     * @return
     */
    public static NamespaceDomain forDomainString(String domainString) {
        return DSMAP.get(domainString);
    }
}
