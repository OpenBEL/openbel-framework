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
