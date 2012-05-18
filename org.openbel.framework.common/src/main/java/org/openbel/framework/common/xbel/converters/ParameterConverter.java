/**
 * Copyright (C) 2012 Selventa, Inc.
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
