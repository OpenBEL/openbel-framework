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
