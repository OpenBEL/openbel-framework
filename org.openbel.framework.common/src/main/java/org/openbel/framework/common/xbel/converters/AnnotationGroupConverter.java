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
package org.openbel.framework.common.xbel.converters;

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.ArrayList;
import java.util.List;

import org.openbel.bel.xbel.model.XBELAnnotation;
import org.openbel.bel.xbel.model.XBELAnnotationGroup;
import org.openbel.bel.xbel.model.XBELCitation;
import org.openbel.framework.common.model.Annotation;
import org.openbel.framework.common.model.AnnotationGroup;
import org.openbel.framework.common.model.Citation;
import org.openbel.framework.common.model.CommonModelFactory;
import org.openbel.framework.common.model.Evidence;

/**
 * Converter class for converting between {@link XBELAnnotationGroup} and
 * {@link AnnotationGroup}.
 */
public final class AnnotationGroupConverter extends
        JAXBConverter<XBELAnnotationGroup, AnnotationGroup> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationGroup convert(XBELAnnotationGroup source) {
        if (source == null) return null;

        List<Object> list = source.getAnnotationOrEvidenceOrCitation();

        // Destination type
        AnnotationGroup dest = new AnnotationGroup();

        // Capture XBELCitation elements of list as a new list
        List<XBELCitation> l1 = listCapture(list, XBELCitation.class);
        List<Citation> l2 = new ArrayList<Citation>();

        CitationConverter cConverter = new CitationConverter();
        for (final XBELCitation xc : l1) {
            // Defer to CitationConverter
            l2.add(cConverter.convert(xc));
        }
        if (!l2.isEmpty()) {
            dest.setCitation(l2.get(0));
        }

        // Capture XBELAnnotation elements of list as a new list
        List<XBELAnnotation> l3 = listCapture(list, XBELAnnotation.class);
        List<Annotation> l4 = new ArrayList<Annotation>();

        AnnotationConverter aConverter = new AnnotationConverter();
        for (final XBELAnnotation xa : l3) {
            // Defer to AnnotationConverter
            l4.add(aConverter.convert(xa));
        }
        dest.setAnnotations(l4);

        // Capture evidence strings of list as a new list
        List<String> l5 = listCapture(list, String.class);
        List<Evidence> l6 = new ArrayList<Evidence>();

        for (final String str : l5) {
            l6.add(CommonModelFactory.getInstance().createEvidence(str));
        }
        if (!l6.isEmpty()) {
            dest.setEvidence(l6.get(0));
        }

        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELAnnotationGroup convert(AnnotationGroup source) {
        if (source == null) return null;

        List<Annotation> annotations = source.getAnnotations();
        Citation citation = source.getCitation();
        Evidence evidence = source.getEvidence();

        XBELAnnotationGroup xag = new XBELAnnotationGroup();
        List<Object> list = xag.getAnnotationOrEvidenceOrCitation();

        if (hasItems(annotations)) {
            // Defer to AnnotationConverter
            AnnotationConverter aConverter = new AnnotationConverter();
            for (final Annotation a : annotations) {
                XBELAnnotation xa = aConverter.convert(a);
                list.add(xa);
            }
        }

        if (citation != null) {
            // Defer to CitationConverter
            CitationConverter cConverter = new CitationConverter();
            XBELCitation xc = cConverter.convert(citation);
            list.add(xc);
        }

        if (evidence != null) {
            list.add(evidence.getValue());
        }

        return xag;
    }
}
