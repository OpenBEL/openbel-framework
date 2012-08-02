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
package org.openbel.framework.common.model;

import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.enums.AnnotationType.ENUMERATION;

import java.util.List;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.AnnotationType;

/**
 * Annotations express knowledge about a statement.
 * <p>
 * Two forms of annotation definitions exist within the BEL framework.
 * <dl>
 * <dt>Internal Definitions</dt>
 * <dd>Internal definitions are defined within a document.</dd>
 * <dt>External Definitions</dt>
 * <dd>External definitions are defined by a URL.</dd>
 * </p>
 * TODO This class should be broken into AnnotationDefinition w/
 * InternalAnnotation/ExternalAnnotation as subclasses
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class AnnotationDefinition implements BELModelObject {
    private static final long serialVersionUID = -2284464179660999964L;

    private final String id;

    private AnnotationType type;

    private String description;
    private String usage;
    private String value;
    private List<String> enums;

    // Not a URL type, we don't give URL full RFC 2396 treatment
    private String url;

    /**
     * Creates an annotation definition with the required identifier.
     * 
     * @param id Identifier
     * @throws InvalidArgument Thrown if {@code id} is null
     */
    public AnnotationDefinition(final String id) {
        if (id == null) {
            throw new InvalidArgument("id", id);
        }
        this.id = id;
    }

    /**
     * Creates an annotation definition with the required identifier, type,
     * description, usage, and URL properties.
     * 
     * @param id Identifier
     * @param t Type of annotation
     * @param desc Description of annotation definition
     * @param usage Usage of annotation definition
     * @param url URL defining this annotation definition
     * @throws InvalidArgument Thrown if {@code id} is null
     */
    public AnnotationDefinition(final String id, final AnnotationType t,
            final String desc, final String usage, final String url) {
        if (id == null) {
            throw new InvalidArgument("id", id);
        }
        this.id = id;
        this.description = desc;
        this.usage = usage;
        this.type = t;
        this.url = url;
    }

    /**
     * Creates an annotation definition with the required identifier, type,
     * description, usage, and URL properties.
     * 
     * @param id Identifier
     * @param t Type of annotation
     * @param desc Description of annotation definition
     * @param usage Usage of annotation definition
     * @throws InvalidArgument Thrown if {@code id} is null
     */
    public AnnotationDefinition(final String id, final AnnotationType t,
            final String desc, final String usage) {
        if (id == null) {
            throw new InvalidArgument("id", id);
        }
        this.id = id;
        this.description = desc;
        this.usage = usage;
        this.type = t;
    }

    /**
     * Creates an annotation definition of type
     * {@link AnnotationType#ENUMERATION ENUMERATION} with the required
     * identifier, optional description and usage, and enumeration values.
     * 
     * @param id Identifier
     * @param desc Description of annotation definition
     * @param usage Usage of annotation definition
     * @param enums List of enumeration values
     * @throws InvalidArgument Thrown if {@code id} is null or {@code enums} has
     * no items
     */
    public AnnotationDefinition(final String id, final String desc,
            final String usage, final List<String> enums) {
        if (id == null) {
            throw new InvalidArgument("id", id);
        }
        if (noItems(enums)) {
            throw new InvalidArgument("enums has no items");
        }
        this.id = id;
        this.description = desc;
        this.usage = usage;
        type = ENUMERATION;
        this.enums = enums;
        this.url = null;
    }

    /**
     * Returns the annotation definition's description.
     * 
     * @return String, which may be null
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the annotation definition's description.
     * 
     * @param desc {@link String}
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * Returns the annotation definition's usage.
     * 
     * @return String, which may be null
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Sets the annotation definition's usage.
     * 
     * @param usage {@link String}
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * Returns the annotation definition's identifier uniquely identifying it
     * within a document.
     * 
     * @return Non-null string
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the annotation definition's type.
     * 
     * @return {@link AnnotationType}; which may be null
     */
    public AnnotationType getType() {
        return type;
    }

    /**
     * Sets the annotation definition's type.
     * 
     * @param t {@link AnnotationType}
     */
    public void setType(AnnotationType t) {
        this.type = t;
    }

    /**
     * Returns the annotation definition's value.
     * 
     * @return String, may be null, in which case, enums is non-null
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the annotation definition's value.
     * 
     * @param value Annotation definition's value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the annotation definition's enums.
     * 
     * @return List of strings, may be null, in which case, value is non-null
     */
    public List<String> getEnums() {
        return enums;
    }

    /**
     * Sets the annotation definition's enums.
     * 
     * @param enums Annotation definition's enums
     */
    public void setEnums(List<String> enums) {
        this.enums = enums;
    }

    /**
     * Returns the annotation definition's URL.
     * 
     * @return URL {@link String string}, which may be null
     */
    public String getURL() {
        return url;
    }

    /**
     * Sets the annotation definition's URL.
     * 
     * @param url {@link String}
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("AnnotationDefinition [id=");
        // id is non-null by contract
        builder.append(id);

        if (type != null) {
            builder.append(", type=");
            builder.append(type);
        }

        if (description != null) {
            builder.append(", description=");
            builder.append(description);
        }

        if (usage != null) {
            builder.append(", usage=");
            builder.append(usage);
        }

        if (enums != null) {
            builder.append(", enums=");
            builder.append(enums);
        }

        if (value != null) {
            builder.append(", value=");
            builder.append(value);
        }

        if (url != null) {
            builder.append(", url=");
            builder.append(url);
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
        // id is non-null by contract
        result += id.hashCode();

        if (type != null) {
            result *= prime;
            result += type.hashCode();
        }

        if (description != null) {
            result *= prime;
            result += description.hashCode();
        }

        if (usage != null) {
            result *= prime;
            result += usage.hashCode();
        }

        if (enums != null) {
            result *= prime;
            result += enums.hashCode();
        }

        if (value != null) {
            result *= prime;
            result += value.hashCode();
        }

        if (url != null) {
            result *= prime;
            result += url.hashCode();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnotationDefinition)) {
            return false;
        }

        final AnnotationDefinition ad = (AnnotationDefinition) o;

        // id is non-null by contract
        if (!id.equals(ad.id)) return false;

        if (type == null) {
            if (ad.type != null) {
                return false;
            }
        } else if (!type.equals(ad.type)) {
            return false;
        }

        if (description == null) {
            if (ad.description != null) {
                return false;
            }
        } else if (!description.equals(ad.description)) {
            return false;
        }

        if (usage == null) {
            if (ad.usage != null) {
                return false;
            }
        } else if (!usage.equals(ad.usage)) {
            return false;
        }

        if (enums == null) {
            if (ad.enums != null) {
                return false;
            }
        } else if (!enums.equals(ad.enums)) {
            return false;
        }

        if (value == null) {
            if (ad.value != null) {
                return false;
            }
        } else if (!value.equals(ad.value)) {
            return false;
        }

        if (url == null) {
            if (ad.url != null) {
                return false;
            }
        } else if (!url.equals(ad.url)) {
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationDefinition clone() {
        AnnotationDefinition ret =
                CommonModelFactory.getInstance().createAnnotationDefinition(id,
                        type, description, usage, url);

        ret.value = value;
        if (enums != null) {
            List<String> enums2 = sizedArrayList(enums.size());
            enums2.addAll(enums);
            ret.enums = enums2;
        }
        return ret;
    }
}
