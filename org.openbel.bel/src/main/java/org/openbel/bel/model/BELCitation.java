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
