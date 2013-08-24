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
package org.openbel.framework.common.bel.converters;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openbel.bel.model.BELCitation;
import org.openbel.framework.common.enums.CitationType;
import org.openbel.framework.common.model.Citation;
import org.openbel.framework.common.model.CommonModelFactory;

public class BELCitationConverter extends BELConverter<BELCitation, Citation> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Citation convert(BELCitation bc) {
        if (bc == null) {
            return null;
        }

        Citation c = CommonModelFactory.getInstance().createCitation(
                bc.getName());

        if (bc.getPublicationDate() != null) {
            Calendar date = Calendar.getInstance();
            date.setTime(bc.getPublicationDate());

            c.setDate(date);
        }

        c.setAuthors(bc.getAuthors());
        c.setComment(bc.getComment());
        c.setReference(bc.getReference());
        c.setType(CitationType.fromString(bc.getType()));

        return c;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELCitation convert(Citation c) {
        if (c == null) {
            return null;
        }

        final List<String> authors = c.getAuthors();
        final String comment = c.getComment();

        Date publicationDate = null;
        if (c.getDate() != null) {
            publicationDate = c.getDate().getTime();
        }

        String reference = c.getReference();

        BELCitation bc = new BELCitation(c.getType().getDisplayValue(),
                c.getName(), publicationDate, reference, authors, comment);
        return bc;
    }
}
