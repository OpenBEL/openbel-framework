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
