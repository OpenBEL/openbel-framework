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
package org.openbel.bel.model;

import java.util.Map;

public class BELDocumentHeader extends BELObject {
    private static final long serialVersionUID = -8492135070788117346L;
    private final String name;
    private final String author;
    private final String copyright;
    private final String contactInfo;
    private final String description;
    private final String disclaimer;
    private final String license;
    private final String version;

    public BELDocumentHeader(final String name, final String description,
            final String version) {
        if (name == null) {
            throw new IllegalArgumentException("name must be set.");
        }

        if (description == null) {
            throw new IllegalArgumentException("description must be set.");
        }

        if (version == null) {
            throw new IllegalArgumentException("version must be set.");
        }

        this.name = name;
        this.description = description;
        this.version = version;
        this.author = null;
        this.copyright = null;
        this.contactInfo = null;
        this.disclaimer = null;
        this.license = null;
    }

    public BELDocumentHeader(final String name, final String description,
            final String version,
            final String author,
            final String copyright, final String contactInfo,
            final String disclaimer,
            final String license) {
        if (name == null) {
            throw new IllegalArgumentException("name must be set.");
        }

        this.name = clean(name);
        this.description = clean(description);
        this.version = clean(version);
        this.author = clean(author);
        this.copyright = clean(copyright);
        this.contactInfo = clean(contactInfo);
        this.disclaimer = clean(disclaimer);
        this.license = clean(license);
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getDescription() {
        return description;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public String getLicense() {
        return license;
    }

    public String getVersion() {
        return version;
    }

    public static BELDocumentHeader create(
            Map<BELDocumentProperty, String> docProp) {
        final String name = docProp.get(BELDocumentProperty.NAME);
        final String description = docProp.get(BELDocumentProperty.DESCRIPTION);
        final String version = docProp.get(BELDocumentProperty.VERSION);
        final String author = docProp.get(BELDocumentProperty.AUTHOR);
        final String copyright = docProp.get(BELDocumentProperty.COPYRIGHT);
        final String contactInfo =
                docProp.get(BELDocumentProperty.CONTACT_INFO);
        final String disclaimer = docProp.get(BELDocumentProperty.DISCLAIMER);
        final String license = docProp.get(BELDocumentProperty.LICENSE);

        return new BELDocumentHeader(name, description, version, author,
                copyright,
                contactInfo,
                disclaimer, license);
    }
}
