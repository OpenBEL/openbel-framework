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
package org.openbel.framework.common.protonetwork.model;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;
import org.openbel.framework.common.model.Header;

/**
 * DocumentTable holds the document header information. This class manages the
 * document headers through the {@link #addDocumentHeader(DocumentHeader)}
 * operation.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class DocumentTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767009L;

    /**
     * Defines an ordered list to hold {@link DocumentHeader} objects. Note:
     * This collection is backed by {@link ArrayList}, which is not thread-safe.
     */
    private List<DocumentHeader> headers = new ArrayList<DocumentHeader>();

    /**
     * Adds a document to the {@code headers} set. The insertion index is
     * maintained in the {@code headers} list.
     * 
     * @param header {@link DocumentHeader}, the header to add, which cannot be
     * null
     * @return {@code int}, the index of the added document header, which will
     * be &gt;= 0
     * @throws InvalidArgument Thrown if {@code header} is null
     */
    public int addDocumentHeader(DocumentHeader header) {
        if (header == null) {
            throw new InvalidArgument("header is null");
        }

        headers.add(header);
        return headers.size() - 1;
    }

    /**
     * Returns the document table's {@code headers} list. This list is
     * unmodifiable to preserve the state of the document table.
     * 
     * @return {@link List}, the document {@code headers} list, which cannot be
     * null or modified
     */
    public List<DocumentHeader> getDocumentHeaders() {
        return Collections.unmodifiableList(headers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((headers == null) ? 0 : headers.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DocumentTable other = (DocumentTable) obj;
        if (headers == null) {
            if (other.headers != null)
                return false;
        } else if (!headers.equals(other.headers))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        // 1: read the number of document headers
        final int size = in.readInt();
        headers = sizedArrayList(size);
        for (int i = 0; i < size; ++i) {
            final DocumentHeader header = new DocumentHeader();
            // 1: read each document header
            header.readExternal(in);
            addDocumentHeader(header);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        // 1: write the number of document headers
        out.writeInt(headers.size());
        for (DocumentHeader header : headers) {
            // 1: write each document header
            header.writeExternal(out);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in, ReadCache cache)
            throws IOException,
            ClassNotFoundException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out, WriteCache cache)
            throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * DocumentHeader holds the document header properties
     * 
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     * @version 1.3 Derives from {@link ExternalType}
     */
    public static class DocumentHeader extends ExternalType {
        private static final long serialVersionUID = -6486699009265767008L;

        /**
         * Defines the name of the document.
         */
        private/* final */String name;

        /**
         * Defines the description of the document.
         */
        private/* final */String description;

        /**
         * Defines the version of the document.
         */
        private/* final */String version;

        /**
         * Defines the copyright of the document.
         */
        private/* final */String copyright;

        /**
         * Defines the disclaimer of the document.
         */
        private/* final */String disclaimer;

        /**
         * Defines the contact info of the document.
         */
        private/* final */String contactInfo;

        /**
         * Defines the authors of the document as a packed {@link String} where
         * the author names are separated by a
         * {@value ProtoNetwork#VALUE_SEPARATOR}.
         */
        private/* final */String authors;

        /**
         * Defines the licenses of the document as a packed {@link String} where
         * the license values are separated by
         * {@value ProtoNetwork#VALUE_SEPARATOR}.
         */
        private/* final */String licenses;

        /**
         * Defines the hash for {@link DocumentHeader this document header}.
         */
        private/* final */int hash;

        /**
         * Creates a DocumentHeader from a bel {@link Header}.
         * 
         * @param belHeader {@link Header}, the bel header, which cannot be null
         * @throws InvalidArgument - Thrown if {@code belHeader} is null
         */
        public DocumentHeader(Header belHeader) {
            if (belHeader == null) {
                throw new InvalidArgument("belHeader is null");
            }

            this.name = belHeader.getName();
            this.description = belHeader.getDescription();
            this.version = belHeader.getVersion();
            this.copyright = belHeader.getCopyright();
            this.disclaimer = belHeader.getDisclaimer();
            this.contactInfo = belHeader.getContactInfo();

            List<String> authors = belHeader.getAuthors();
            if (hasItems(authors)) {
                StringBuilder sb = new StringBuilder();
                for (String a : authors) {
                    sb.append(a).append(ProtoNetwork.VALUE_SEPARATOR);
                }
                this.authors = sb.substring(0, sb.length() - 1);
            } else {
                this.authors = null;
            }

            List<String> licenses = belHeader.getLicenses();
            if (hasItems(licenses)) {
                StringBuilder sb = new StringBuilder();
                for (String a : licenses) {
                    sb.append(a).append(ProtoNetwork.VALUE_SEPARATOR);
                }
                this.licenses = sb.substring(0, sb.length() - 1);
            } else {
                this.licenses = null;
            }

            this.hash = computeHash();
        }

        /**
         * Creates a DocumentHeader. This public, no-argument constructor is
         * required when implementing Externalizable but it is not meant to be
         * used for anything else.
         */
        public DocumentHeader() {
        }

        /**
         * Returns the document's name.
         * 
         * @return {@link String}, the document's name, which cannot be null
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the document's description.
         * 
         * @return {@link String}, the document's description, which can be null
         */
        public String getDescription() {
            return description;
        }

        /**
         * Returns the document's version.
         * 
         * @return {@link String}, the document's version, which can be null
         */
        public String getVersion() {
            return version;
        }

        /**
         * Returns the document's copyright.
         * 
         * @return {@link String}, the document's copyright, which can be null
         */
        public String getCopyright() {
            return copyright;
        }

        /**
         * Returns the document's disclaimer.
         * 
         * @return {@link String}, the document's disclaimer, which can be null
         */
        public String getDisclaimer() {
            return disclaimer;
        }

        /**
         * Returns the document's contact info.
         * 
         * @return {@link String}, the document's contact info, which can be
         * null
         */
        public String getContactInfo() {
            return contactInfo;
        }

        /**
         * Returns the document's authors as a packed {@link String} where the
         * author names are separated by a {@value ProtoNetwork#VALUE_SEPARATOR}
         * .
         * 
         * @return {@link String}, the document's authors, which can be null
         */
        public String getAuthors() {
            return authors;
        }

        /**
         * Returns the document's licenses as a packed {@link String} where the
         * license values are separated by a
         * {@value ProtoNetwork#VALUE_SEPARATOR}.
         * 
         * @return {@link String}, the document's licenses, which can be null
         */
        public String getLicenses() {
            return licenses;
        }

        /**
         * Compute the hash for {@link DocumentHeader this document header}.
         * 
         * @return the hash
         */
        private int computeHash() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((authors == null) ? 0 : authors.hashCode());
            result = prime * result
                    + ((licenses == null) ? 0 : licenses.hashCode());
            result = prime * result
                    + ((contactInfo == null) ? 0 : contactInfo.hashCode());
            result = prime * result
                    + ((copyright == null) ? 0 : copyright.hashCode());
            result = prime * result
                    + ((description == null) ? 0 : description.hashCode());
            result = prime * result
                    + ((disclaimer == null) ? 0 : disclaimer.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result
                    + ((version == null) ? 0 : version.hashCode());
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return hash;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DocumentHeader other = (DocumentHeader) obj;
            if (authors == null) {
                if (other.authors != null)
                    return false;
            } else if (!authors.equals(other.authors))
                return false;
            if (licenses == null) {
                if (other.licenses != null)
                    return false;
            } else if (!licenses.equals(other.licenses))
                return false;
            if (contactInfo == null) {
                if (other.contactInfo != null)
                    return false;
            } else if (!contactInfo.equals(other.contactInfo))
                return false;
            if (copyright == null) {
                if (other.copyright != null)
                    return false;
            } else if (!copyright.equals(other.copyright))
                return false;
            if (description == null) {
                if (other.description != null)
                    return false;
            } else if (!description.equals(other.description))
                return false;
            if (disclaimer == null) {
                if (other.disclaimer != null)
                    return false;
            } else if (!disclaimer.equals(other.disclaimer))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (version == null) {
                if (other.version != null)
                    return false;
            } else if (!version.equals(other.version))
                return false;
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in) throws IOException,
                ClassNotFoundException {
            name = readString(in);
            description = readString(in);
            version = readString(in);
            copyright = readString(in);
            disclaimer = readString(in);
            contactInfo = readString(in);
            authors = readString(in);
            licenses = readString(in);
            hash = computeHash();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _to(ObjectOutput out) throws IOException {
            out.writeObject(name);
            out.writeObject(description);
            out.writeObject(version);
            out.writeObject(copyright);
            out.writeObject(disclaimer);
            out.writeObject(contactInfo);
            out.writeObject(authors);
            out.writeObject(licenses);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in, ReadCache cache)
                throws IOException,
                ClassNotFoundException {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _to(ObjectOutput out, WriteCache cache)
                throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}
