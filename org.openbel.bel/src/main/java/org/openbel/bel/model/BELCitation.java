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
package org.openbel.bel.model;

import java.util.Date;
import java.util.List;

public class BELCitation extends BELObject {
    private static final long serialVersionUID = 8002993294182387599L;
    private final String type;
    private final String name;
    private final Date publicationDate;
    private final String reference;
    private final List<String> authors;
    private final String comment;

    public BELCitation(final String type, final String name,
            final String reference) {
        this.type = type;
        this.name = name;
        this.reference = reference;
        this.publicationDate = null;
        this.authors = null;
        this.comment = null;
    }

    public BELCitation(final String type, final String name,
            final Date publicationDate,
            final String reference, final List<String> authors,
            final String comment) {
        this.type = clean(type);
        this.name = clean(name);
        this.reference = clean(reference);
        this.publicationDate = publicationDate;
        this.authors = authors;
        this.comment = clean(comment);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getReference() {
        return reference;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getComment() {
        return comment;
    }
}
