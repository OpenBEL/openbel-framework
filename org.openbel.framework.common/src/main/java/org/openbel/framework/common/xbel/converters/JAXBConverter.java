/**
 * Copyright (C) 2012-2013 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
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
