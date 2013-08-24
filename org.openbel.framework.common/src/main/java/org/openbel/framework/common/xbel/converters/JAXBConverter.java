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
package org.openbel.framework.common.xbel.converters;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import org.openbel.bel.xbel.model.JAXBElement;

/**
 * Base class for JAXBElement-to-BEL object converters.
 *
 * @param <T1> Generic type of {@link JAXBElement} suitable for conversion to a
 * BEL model object
 * @param <T2> Generic type of any {@link Object} that can convert to a type of
 * {@link JAXBElement}
 */
public abstract class JAXBConverter<T1 extends JAXBElement, T2> {

    public abstract T1 convert(T2 t);

    public abstract T2 convert(T1 t);

    /**
     * Captures all objects of type {@code t} contained in the provided list as
     * a new checked list.
     *
     * @param <T> Captured type for new checked list
     * @param objects List of objects
     * @param t Class type to capture
     * @return Checked list of type {@code T} which may be empty
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> listCapture(List<?> objects, Class<T> t) {
        if (objects == null || objects.isEmpty()) {
            return emptyList();
        }
        List<T> ret = new ArrayList<T>(objects.size());
        for (final Object o : objects) {
            Class<?> oc = o.getClass();
            if (oc == t || t.isAssignableFrom(oc)) {
                ret.add((T) o);
            }
        }
        return ret;
    }
}
