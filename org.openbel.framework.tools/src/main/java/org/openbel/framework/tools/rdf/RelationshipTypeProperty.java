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
package org.openbel.framework.tools.rdf;

import static org.openbel.framework.common.BELUtilities.constrainedHashMap;
import static org.openbel.framework.common.enums.RelationshipType.values;
import static org.openbel.framework.tools.rdf.BELRDFDefinitions.RELATIONSHIP_TYPE_NS;

import java.util.Map;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.RelationshipType;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * RelationshipTypeProperty defines the RDF {@link Property} values based on the BEL
 * {@link RelationshipType}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
final class RelationshipTypeProperty {
    /**
     * Holds {@link RelationshipType} to RDF {@link Property}
     */
    private static final Map<RelationshipType, Property> TYPETOPROPERTY;

    // statically-initialize TYPETOPROPERTY from RelationshipType
    static {
        RelationshipType[] rtypes = values();
        TYPETOPROPERTY = constrainedHashMap(rtypes.length);
        final Model defaultModel = ModelFactory.createDefaultModel();
        for (RelationshipType rtype : rtypes) {
            TYPETOPROPERTY.put(
                    rtype,
                    defaultModel.createProperty(RELATIONSHIP_TYPE_NS,
                            rtype.getDisplayValue()));
        }
    }

    /**
     * Retrieve the RDF {@link Property} for the BEL {@link RelationshipType}.
     *
     * @param rtype {@link RelationshipType}, the relationship type, which
     * cannot be null
     * @return {@link Property} the property for the {@link RelationshipType}
     * @throws InvalidArgument Thrown if <tt>rtype</tt> is null
     */
    public static final Property propertyFor(RelationshipType rtype) {
        if (rtype == null) {
            throw new InvalidArgument("rtype", rtype);
        }

        return TYPETOPROPERTY.get(rtype);
    }
}
