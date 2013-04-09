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

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.enums.CitationType.fromString;

import java.util.Calendar;
import java.util.List;

import org.openbel.bel.xbel.model.CitationType;
import org.openbel.bel.xbel.model.XBELCitation;
import org.openbel.bel.xbel.model.XBELCitation.AuthorGroup;
import org.openbel.framework.common.model.Citation;
import org.openbel.framework.common.model.CommonModelFactory;

/**
 * Converter class for converting between {@link XBELCitation} and
 * {@link Citation}.
 *
 */
public final class CitationConverter extends
        JAXBConverter<XBELCitation, Citation> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Citation convert(XBELCitation source) {
        if (source == null) {
            return null;
        }

        String name = source.getName();

        // Destination type
        Citation dest = CommonModelFactory.getInstance().createCitation(name);

        Calendar date = source.getDate();
        dest.setDate(date);

        String comment = source.getComment();
        dest.setComment(comment);

        String reference = source.getReference();
        dest.setReference(reference);

        CitationType type = source.getType();
        if (type != null) {
            dest.setType(fromString(type.value()));
        }

        if (source.isSetAuthorGroup()) {
            List<String> author = source.getAuthorGroup().getAuthor();
            dest.setAuthors(author);
        }

        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELCitation convert(Citation source) {
        if (source == null) {
            return null;
        }

        XBELCitation xc = new XBELCitation();

        xc.setDate(source.getDate());
        xc.setComment(source.getComment());
        xc.setReference(source.getReference());
        xc.setName(source.getName());

        org.openbel.framework.common.enums.CitationType type = source.getType();
        if (type != null) {
            CitationType ct = CitationType.fromValue(type.getDisplayValue());
            xc.setType(ct);
        }

        List<String> authors = source.getAuthors();
        if (hasItems(authors)) {
            AuthorGroup ag = new AuthorGroup();
            List<String> xauthors = ag.getAuthor();
            for (final String author : authors) {
                xauthors.add(author);
            }
            xc.setAuthorGroup(ag);
        }

        return xc;
    }
}
