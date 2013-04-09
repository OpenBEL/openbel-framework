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
package org.openbel.framework.common.model;

import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.util.List;

import org.openbel.framework.common.InvalidArgument;

/**
 * The header provides ownership and legal context for a BEL document.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Header implements BELModelObject {
    private static final long serialVersionUID = -420407275543934943L;

    private final String name;
    private final String description;
    private final String version;
    private List<String> authors;
    private List<String> licenses;
    private String copyright;
    private String disclaimer;
    private String contactInfo;

    /**
     * Creates a header with the required name, description, and version
     * properties.
     *
     * @param name Name
     * @param description Description
     * @param version Version
     * @throws InvalidArgument Thrown if {@code name}, {@code description}, or
     * {@code version} is null
     */
    public Header(final String name, final String description,
            final String version) {
        if (name == null) {
            throw new InvalidArgument("name is null");
        }
        if (description == null) {
            throw new InvalidArgument("description is null");
        }
        if (version == null) {
            throw new InvalidArgument("version is null");
        }
        this.name = name;
        this.description = description;
        this.version = version;
    }

    /**
     * Create a header with the required name, description, and version required
     * properties and optional properties.
     *
     * @param name Name
     * @param authors List of authors
     * @param licenses List of licenses
     * @param copyright Copyright
     * @param description Description
     * @param disclaimer Disclaimer
     * @param version Version
     * @param contactInfo Contact information
     * @throws InvalidArgument Thrown if {@code name}, {@code description}, or
     * {@code version} is null
     */
    public Header(final String name, List<String> authors,
            List<String> licenses, String copyright, String description,
            String disclaimer, String version, String contactInfo) {
        if (name == null) {
            throw new InvalidArgument("name is null");
        }
        if (description == null) {
            throw new InvalidArgument("description is null");
        }
        if (version == null) {
            throw new InvalidArgument("version is null");
        }
        this.name = name;
        this.authors = authors;
        this.licenses = licenses;
        this.copyright = copyright;
        this.description = description;
        this.disclaimer = disclaimer;
        this.version = version;
        this.contactInfo = contactInfo;
    }

    /**
     * Returns the header's authors.
     *
     * @return {@link List} of {@link String strings}, which may be null
     */
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * Sets the header's authors.
     *
     * @param authors The header's authors, as a {@link List} of {@link String
     * strings}
     */
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    /**
     * Returns the header's licenses.
     *
     * @return {@link List} of {@link String strings}, which may be null
     */
    public List<String> getLicenses() {
        return licenses;
    }

    /**
     * Sets the header's licenses.
     *
     * @param licenses The header's licenses, as a {@link List} of
     * {@link String strings}
     */
    public void setLicenses(List<String> licenses) {
        this.licenses = licenses;
    }

    /**
     * Returns the header's name.
     *
     * @return {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the header's copyright.
     *
     * @return {@link String}, which may be null
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the header's copyright.
     *
     * @param copyright Header's copyright {@link String string}
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * Returns the header's description.
     *
     * @return {@link String}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the header's disclaimer.
     *
     * @return {@link String}, which may be null
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     * Sets the header's disclaimer.
     *
     * @param disclaimer Header's disclaimer {@link String string}
     */
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    /**
     * Returns the header's version.
     *
     * @return {@link String}
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the header's contact info.
     *
     * @return {@link String}, which may be null
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the header's contact info.
     *
     * @param contactInfo Header's contact info {@link String string}
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Header [");

        if (authors != null) {
            builder.append("authors=");
            builder.append(authors);
            builder.append(", ");
        }

        if (licenses != null) {
            builder.append("licenses=");
            builder.append(licenses);
            builder.append(", ");
        }

        // name is non-null by contract
        builder.append("name=");
        builder.append(name);
        builder.append(", ");

        if (copyright != null) {
            builder.append("copyright=");
            builder.append(copyright);
            builder.append(", ");
        }

        // description is non-null by contract
        builder.append("description=");
        builder.append(description);
        builder.append(", ");

        if (disclaimer != null) {
            builder.append("disclaimer=");
            builder.append(disclaimer);
            builder.append(", ");
        }

        // version is non-null by contract
        builder.append("version=");
        builder.append(version);

        if (contactInfo != null) {
            builder.append("contactInfo=");
            builder.append(contactInfo);
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result *= prime;
        if (authors != null) result += authors.hashCode();

        result *= prime;
        if (licenses != null) result += licenses.hashCode();

        // name is non-null by contract
        result *= prime;
        result += name.hashCode();

        result *= prime;
        if (copyright != null) result += copyright.hashCode();

        // description is non-null by contract
        result *= prime;
        result += description.hashCode();

        result *= prime;
        if (disclaimer != null) result += disclaimer.hashCode();

        // version is non-null by contract
        result *= prime;
        result += version.hashCode();

        result *= prime;
        if (contactInfo != null) result += contactInfo.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Header)) return false;

        final Header h = (Header) o;

        if (authors == null) {
            if (h.authors != null) return false;
        } else if (!authors.equals(h.authors)) return false;

        if (licenses == null) {
            if (h.licenses != null) return false;
        } else if (!licenses.equals(h.licenses)) return false;

        // name is non-null by contract
        if (!name.equals(h.name)) return false;

        if (copyright == null) {
            if (h.copyright != null) return false;
        } else if (!copyright.equals(h.copyright)) return false;

        if (description == null) {
            if (h.description != null) return false;
        } else if (!description.equals(h.description)) return false;

        if (disclaimer == null) {
            if (h.disclaimer != null) return false;
        } else if (!disclaimer.equals(h.disclaimer)) return false;

        if (version == null) {
            if (h.version != null) return false;
        } else if (!version.equals(h.version)) return false;

        if (contactInfo == null) {
            if (h.contactInfo != null) return false;
        } else if (!contactInfo.equals(h.contactInfo)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Header clone() {
        List<String> authors2 = null;
        if (authors != null) {
            authors2 = sizedArrayList(authors.size());
            authors2.addAll(authors);
        }

        List<String> licenses2 = null;
        if (licenses != null) {
            licenses2 = sizedArrayList(licenses.size());
            licenses2.addAll(licenses);
        }

        return new Header(name, authors2, licenses2, copyright, description,
                disclaimer, version, contactInfo);
    }
}
