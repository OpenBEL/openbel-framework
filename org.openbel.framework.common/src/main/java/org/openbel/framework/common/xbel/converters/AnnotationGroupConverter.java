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
