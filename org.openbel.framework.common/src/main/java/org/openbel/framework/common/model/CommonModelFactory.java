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
package org.openbel.framework.common.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openbel.framework.common.enums.AnnotationType;
import org.openbel.framework.common.enums.CitationType;

/**
 * CommonModelFactory represents a factory for common model BEL objects.  The
 * factory deals with managing the construction of common model objects in
 * order to reduce redundant object creation.<br/><br/>
 *
 * For instance, any {@link String} used in a common model will be managed in
 * the a local {@link Map} in order to reduce duplicate {@link String} objects.
 * <br/><br/>
 *
 * The CommonModelFactory is a singleton class.  To obtain the single reference
 * access the static {@link CommonModelFactory#getInstance()}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public final class CommonModelFactory {

    private static CommonModelFactory instance;
    private final Map<String, String> valuePool;

    /**
     * Private constructor to control instantiation of singleton.
     */
    private CommonModelFactory() {
        valuePool = new HashMap<String, String>();
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return {@link CommonModelFactory} the single instance of this class
     */
    public synchronized static CommonModelFactory getInstance() {
        if (instance == null) {
            instance = new CommonModelFactory();
        }

        return instance;
    }

    public Annotation createAnnotation(String value) {
        final String sharedValue = resolveValue(value);
        return new Annotation(sharedValue);
    }

    public Annotation createAnnotation(final String value,
            final AnnotationDefinition annotationDefinition) {
        final String sharedValue = resolveValue(value);
        return new Annotation(sharedValue, annotationDefinition);
    }

    public AnnotationDefinition createAnnotationDefinition(final String id) {
        final String sharedId = resolveValue(id);
        return new AnnotationDefinition(sharedId);
    }

    public AnnotationDefinition createAnnotationDefinition(final String id,
            final AnnotationType t,
            final String desc, final String usage, final String url) {
        final String sharedId = resolveValue(id);
        final String sharedDesc = resolveValue(desc);
        final String sharedUsage = resolveValue(usage);
        final String sharedUrl = resolveValue(url);

        return new AnnotationDefinition(sharedId, t, sharedDesc, sharedUsage,
                sharedUrl);
    }

    public Citation createCitation(final String name) {
        final String sharedName = resolveValue(name);
        return new Citation(sharedName);
    }

    public Citation createCitation(final String name, String reference,
            String comment,
            Calendar date, List<String> authors, CitationType t) {
        final String sharedName = resolveValue(name);
        final String sharedReference = resolveValue(reference);
        final String sharedComment = resolveValue(comment);

        return new Citation(sharedName, sharedReference, sharedComment, date,
                authors, t);
    }

    public Parameter createParameter() {
        return new Parameter();
    }

    public Parameter createParameter(final Namespace namespace,
            final String value) {
        final String sharedValue = resolveValue(value);
        return new Parameter(namespace, sharedValue);
    }

    public Evidence createEvidence(final String value) {
        final String sharedValue = resolveValue(value);
        return new Evidence(sharedValue);
    }

    /**
     * Resolve a {@link String} against the string pool to obtain the canonical
     * instance of the string.
     *
     * @param value {@link String} the string to obtain canonical reference for
     * @return {@link String} the canonical reference for the string
     */
    public String resolveValue(final String value) {
        if (value == null) {
            return null;
        }

        final String sharedValue = valuePool.get(value);
        if (sharedValue != null) {
            return sharedValue;
        }

        valuePool.put(value, value);
        return value;
    }
}
