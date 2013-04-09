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
package org.openbel.framework.common.bel.converters;

import org.openbel.bel.model.BELAnnotationDefinition;
import org.openbel.bel.model.BELAnnotationType;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.AnnotationType;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.CommonModelFactory;

public final class BELAnnotationDefinitionConverter extends
        BELConverter<BELAnnotationDefinition, AnnotationDefinition> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationDefinition convert(BELAnnotationDefinition bad) {
        if (bad == null) {
            return null;
        }

        AnnotationDefinition ad = CommonModelFactory.getInstance()
                .createAnnotationDefinition(bad.getName());
        switch (bad.getAnnotationType()) {
        case LIST:
            ad.setType(AnnotationType.ENUMERATION);
            ad.setEnums(bad.getList());
            break;
        case PATTERN:
            ad.setType(AnnotationType.REGULAR_EXPRESSION);
            ad.setValue(bad.getValue());
            break;
        case URL:
            ad.setType(null);
            ad.setURL(bad.getValue());
            break;
        default:
            throw new InvalidArgument("BELAnnotationType '"
                    + bad.getAnnotationType() + "' not known.");
        }

        return ad;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELAnnotationDefinition convert(AnnotationDefinition ad) {
        if (ad == null) {
            return null;
        }

        if (ad.getType() == null) {
            return new BELAnnotationDefinition(
                    ad.getId(), BELAnnotationType.URL, ad.getURL());
        }

        switch (ad.getType()) {
        case ENUMERATION:
            return new BELAnnotationDefinition(ad.getId(),
                    BELAnnotationType.LIST, ad.getEnums());
        case REGULAR_EXPRESSION:
            return new BELAnnotationDefinition(ad.getId(),
                    BELAnnotationType.PATTERN, ad.getValue());
        default:
            throw new InvalidArgument("AnnotationType '"
                    + ad.getType() + "' not known.");
        }
    }
}
