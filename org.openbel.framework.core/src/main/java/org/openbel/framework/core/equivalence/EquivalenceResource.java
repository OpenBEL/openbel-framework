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
package org.openbel.framework.core.equivalence;

import java.io.IOException;

import org.openbel.framework.core.indexer.EquivalenceLookup;

/**
 * Interface for accessing JDBM-based equivalence resources.
 */
public interface EquivalenceResource {

    /**
     * Opens all resources, making them available for access.
     *
     * @throws IOException Thrown if an I/O error occurred while opening
     * @see {@link #forResourceLocation(String)}
     */
    public void openResources() throws IOException;

    /**
     * Closes all resources, making them unavailable for access.
     *
     * @throws IOException Thrown if an I/O error occurred while closing
     */
    public void closeResources() throws IOException;

    /**
     * Returns {@code true} if the resources have been {@link #openResources()
     * opened}, {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean opened();

    /**
     * Return a {@link EquivalenceLookup} for a resource location.
     *
     * @param rl Resource location
     * @return {@link EquivalenceLookup}; may be null
     * @throws IllegalStateException Thrown if resources have not been
     * {@link #openResources() opened}
     */
    public EquivalenceLookup forResourceLocation(String rl)
            throws IllegalStateException;

}
