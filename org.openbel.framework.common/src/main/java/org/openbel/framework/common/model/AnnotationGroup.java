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

import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.util.List;

/**
 * A grouping of annotations, evidence, and citation.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class AnnotationGroup implements BELModelObject {
    private static final long serialVersionUID = 7238463986461296243L;

    private List<Annotation> annotations;
    private Evidence evidence;
    private Citation citation;

    /**
     * Creates an annotation group with the supplied properties.
     * 
     * @param annotations List of annotations
     * @param evidence evidence
     * @param citation citation
     */
    public AnnotationGroup(List<Annotation> annotations,
            Evidence evidence, Citation citation) {
        this.annotations = annotations;
        this.evidence = evidence;
        this.citation = citation;
    }

    /**
     * Creates an annotation group.
     */
    public AnnotationGroup() {
    }

    /**
     * Returns the list of annotations.
     * 
     * @return List of annotations, which may be null
     */
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * Sets the list of annotations.
     * 
     * @param annotations List of annotations
     */
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    /**
     * Returns the evidence.
     * 
     * @return evidence, which may be null
     */
    public Evidence getEvidence() {
        return evidence;
    }

    /**
     * Sets the evidence.
     * 
     * @param evidence evidence
     */
    public void setEvidence(Evidence evidence) {
        this.evidence = evidence;
    }

    /**
     * Returns the citation.
     * 
     * @return citation, which may be null
     */
    public Citation getCitation() {
        return citation;
    }

    /**
     * Sets the citation.
     * 
     * @param citation
     */
    public void setCitation(Citation citation) {
        this.citation = citation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AnnotationGroup [");

        if (annotations != null) {
            builder.append("annotations=");
            builder.append(annotations);
            builder.append(", ");
        }

        if (evidence != null) {
            builder.append("evidence=");
            builder.append(evidence);
            builder.append(", ");
        }

        if (citation != null) {
            builder.append("citation=");
            builder.append(citation);
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
        if (annotations != null) result += annotations.hashCode();

        result *= prime;
        if (citation != null) result += citation.hashCode();

        result *= prime;
        if (evidence != null) result += evidence.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnnotationGroup)) return false;

        final AnnotationGroup ag = (AnnotationGroup) o;

        if (annotations == null) {
            if (ag.annotations != null) return false;
        } else if (!annotations.equals(ag.annotations)) return false;

        if (citation == null) {
            if (ag.citation != null) return false;
        } else if (!citation.equals(ag.citation)) return false;

        if (evidence == null) {
            if (ag.evidence != null) return false;
        } else if (!evidence.equals(ag.evidence)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationGroup clone() {
        List<Annotation> annotations2 = null;
        if (annotations != null) {
            annotations2 = sizedArrayList(annotations.size());
            for (final Annotation a : annotations)
                annotations2.add(a.clone());
        }

        Citation citation2 = null;
        if (citation != null) {
            citation2 = citation.clone();
        }

        Evidence evidence2 = null;
        if (evidence != null) {
            evidence2 = evidence.clone();
        }

        return new AnnotationGroup(annotations2, evidence2, citation2);
    }
}
