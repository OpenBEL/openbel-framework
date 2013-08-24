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
