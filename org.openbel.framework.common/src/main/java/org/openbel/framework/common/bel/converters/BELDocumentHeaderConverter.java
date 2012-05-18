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
package org.openbel.framework.common.bel.converters;

import java.util.Arrays;
import java.util.List;

import org.openbel.bel.model.BELDocumentHeader;
import org.openbel.framework.common.model.Header;

public class BELDocumentHeaderConverter extends
        BELConverter<BELDocumentHeader, Header> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Header convert(BELDocumentHeader bdh) {
        String name = bdh.getName();
        String desc = bdh.getDescription();
        String version = bdh.getVersion();
        Header h = new Header(name, desc, version);
        h.setAuthors(Arrays.asList(bdh.getAuthor()));
        h.setContactInfo(bdh.getContactInfo());
        h.setCopyright(bdh.getCopyright());
        h.setDisclaimer(bdh.getDisclaimer());
        h.setLicenses(bdh.getLicense() == null ? null : Arrays.asList(bdh
                .getLicense()));

        return h;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELDocumentHeader convert(Header h) {
        String name = h.getName();
        String author = join(h.getAuthors());
        String copyright = h.getCopyright();
        String contactInfo = h.getContactInfo();
        String description = h.getDescription();
        String disclaimer = h.getDisclaimer();
        String license = join(h.getLicenses());
        String version = h.getVersion();

        return new BELDocumentHeader(name, description, version, author,
                copyright,
                contactInfo,
                disclaimer, license);
    }

    private String join(List<String> strings) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);

            if (i != 0) {
                b.append(", ");
            }

            b.append(s);
        }
        return b.toString();
    }
}
