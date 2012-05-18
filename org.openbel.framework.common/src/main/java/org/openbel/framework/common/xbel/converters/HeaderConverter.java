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

import java.util.List;

import org.openbel.bel.xbel.model.XBELAuthorGroup;
import org.openbel.bel.xbel.model.XBELHeader;
import org.openbel.bel.xbel.model.XBELLicenseGroup;
import org.openbel.framework.common.model.Header;

/**
 * Converter class for converting between {@link XBELHeader} and {@link Header}.
 */
public final class HeaderConverter extends JAXBConverter<XBELHeader, Header> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Header convert(XBELHeader source) {
        if (source == null) return null;

        String name = source.getName();
        String description = source.getDescription();
        String version = source.getVersion();

        // Destination type
        Header dest = new Header(name, description, version);

        if (source.isSetAuthorGroup()) {
            List<String> author = source.getAuthorGroup().getAuthor();
            dest.setAuthors(author);
        }

        if (source.isSetLicenseGroup()) {
            List<String> license = source.getLicenseGroup().getLicense();
            dest.setLicenses(license);
        }

        String copyright = source.getCopyright();
        dest.setCopyright(copyright);

        String disclaimer = source.getDisclaimer();
        dest.setDisclaimer(disclaimer);

        String contactInfo = source.getContactInfo();
        dest.setContactInfo(contactInfo);

        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELHeader convert(Header source) {
        if (source == null) return null;

        XBELHeader xh = new XBELHeader();

        List<String> authors = source.getAuthors();
        if (hasItems(authors)) {
            XBELAuthorGroup xag = new XBELAuthorGroup();
            List<String> xauthors = xag.getAuthor();
            for (final String author : authors) {
                xauthors.add(author);
            }
            xh.setAuthorGroup(xag);
        }

        List<String> licenses = source.getLicenses();
        if (hasItems(licenses)) {
            XBELLicenseGroup xlg = new XBELLicenseGroup();
            List<String> xlicenses = xlg.getLicense();
            for (final String license : licenses) {
                xlicenses.add(license);
            }
            xh.setLicenseGroup(xlg);
        }

        String contactInfo = source.getContactInfo();
        String copyright = source.getCopyright();
        String description = source.getDescription();
        String disclaimer = source.getDisclaimer();
        String name = source.getName();
        String version = source.getVersion();

        xh.setContactInfo(contactInfo);
        xh.setCopyright(copyright);
        xh.setDescription(description);
        xh.setDisclaimer(disclaimer);
        xh.setName(name);
        xh.setVersion(version);

        return xh;
    }
}
