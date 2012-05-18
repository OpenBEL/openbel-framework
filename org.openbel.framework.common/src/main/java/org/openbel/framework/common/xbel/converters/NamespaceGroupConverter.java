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

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.ArrayList;
import java.util.List;

import org.openbel.bel.xbel.model.XBELNamespace;
import org.openbel.bel.xbel.model.XBELNamespaceGroup;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.NamespaceGroup;

/**
 * Converter class for converting between {@link XBELNamespaceGroup} and
 * {@link NamespaceGroup}.
 *
 */
public final class NamespaceGroupConverter extends
        JAXBConverter<XBELNamespaceGroup, NamespaceGroup> {

    /**
     * {@inheritDoc}
     */
    @Override
    public NamespaceGroup convert(XBELNamespaceGroup source) {
        if (source == null) return null;

        String defaultResourceLocation = source.getDefaultResourceLocation();
        List<XBELNamespace> xbelnamespaces = source.getNamespace();

        NamespaceGroup dest = new NamespaceGroup();
        int size = xbelnamespaces.size();
        final List<Namespace> namespaces = new ArrayList<Namespace>(size);

        NamespaceConverter nsConverter = new NamespaceConverter();
        for (final XBELNamespace xns : xbelnamespaces) {
            // Defer to NamespaceConverter
            namespaces.add(nsConverter.convert(xns));
        }

        dest.setDefaultResourceLocation(defaultResourceLocation);
        dest.setNamespaces(namespaces);
        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELNamespaceGroup convert(NamespaceGroup source) {
        if (source == null) return null;

        String drl = source.getDefaultResourceLocation();

        XBELNamespaceGroup xnsg = new XBELNamespaceGroup();

        xnsg.setDefaultResourceLocation(drl);

        List<XBELNamespace> xnss = xnsg.getNamespace();

        final List<Namespace> nss = source.getNamespaces();
        if (hasItems(nss)) {
            for (final Namespace ns : nss) {
                XBELNamespace xns = new XBELNamespace();
                xns.setPrefix(ns.getPrefix());
                xns.setResourceLocation(ns.getResourceLocation());
                xnss.add(xns);
            }
        }

        return xnsg;
    }
}
