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
