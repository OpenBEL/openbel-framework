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

import org.openbel.bel.xbel.model.XBELParameter;
import org.openbel.framework.common.model.CommonModelFactory;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;

/**
 * Converter class for converting between {@link XBELParameter} and
 * {@link Parameter}.
 * <p>
 * The XBEL schema defines a referential {@code refID} property of annotations
 * that refers to a specific annotation definition instance. This instance is
 * not available to the converter and will need to be resolved prior to use as a
 * common {@link Parameter} object.
 * </p>
 *
 */
public final class ParameterConverter extends
        JAXBConverter<XBELParameter, Parameter> {

    /**
     * Constant used in namespace resource locations. Only namespace prefixes
     * are available to this converter. This field will be used as a placeholder
     * in namespace resource locations.
     */
    public static final String UNRESOVLED_NS = "<unresolved_namespace>";

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter convert(final XBELParameter source) {
        if (source == null) return null;

        // Destination type
        Parameter dest = new Parameter();

        if (source.isSetNs()) {
            String prefix = source.getNs();
            Namespace ns = new Namespace(prefix, UNRESOVLED_NS);
            dest.setNamespace(ns);
        }

        if (source.isSetValue()) {
            dest.setValue(CommonModelFactory.getInstance().resolveValue(
                    source.getValue()));
        }

        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELParameter convert(Parameter source) {
        if (source == null) return null;

        Namespace namespace = source.getNamespace();
        String value = source.getValue();

        XBELParameter xp = new XBELParameter();

        if (namespace != null)
            xp.setNs(namespace.getPrefix());
        if (value != null)
            xp.setValue(value);

        return xp;
    }
}
