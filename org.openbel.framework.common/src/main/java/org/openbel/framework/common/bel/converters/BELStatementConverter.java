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
package org.openbel.framework.common.bel.converters;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.RecognitionException;
import org.openbel.bel.model.BELAnnotation;
import org.openbel.bel.model.BELCitation;
import org.openbel.bel.model.BELEvidence;
import org.openbel.bel.model.BELStatement;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.Annotation;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.AnnotationGroup;
import org.openbel.framework.common.model.CommonModelFactory;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;

public class BELStatementConverter extends
        BELConverter<BELStatement, Statement> {
    private static final Pattern QUOTED_NS_VALUE = Pattern.compile("\"(.*)\"");
    private final BELCitationConverter bcc = new BELCitationConverter();
    private final BELEvidenceConverter bec = new BELEvidenceConverter();
    private final Map<String, Namespace> ndefs;
    private final Map<String, AnnotationDefinition> adefs;

    public BELStatementConverter(Map<String, Namespace> ndefs,
            Map<String, AnnotationDefinition> adefs) {
        this.ndefs = ndefs;
        this.adefs = adefs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement convert(BELStatement bs) {
        if (bs == null) {
            return null;
        }

        String comment = bs.getComment();
        String belSyntax = bs.getStatementSyntax();

        try {
            Statement s = BELParser.parseStatement(belSyntax);
            repopulateNamespaces(s);

            s.setComment(comment);

            final BELAnnotationConverter bac =
                    new BELAnnotationConverter(adefs);
            List<BELAnnotation> annotations = bs.getAnnotations();
            List<Annotation> alist = null;
            if (hasItems(annotations)) {
                alist = sizedArrayList(annotations.size());
                for (BELAnnotation annotation : annotations) {
                    alist.addAll(bac.convert(annotation));
                }
            }

            BELCitation bc = bs.getCitation();
            BELEvidence be = bs.getEvidence();

            AnnotationGroup ag = new AnnotationGroup();
            boolean hasAnnotation = false;
            if (hasItems(alist)) {
                ag.setAnnotations(alist);
                hasAnnotation = true;
            }

            if (bc != null) {
                ag.setCitation(bcc.convert(bc));
                hasAnnotation = true;
            }

            if (be != null) {
                ag.setEvidence(bec.convert(be));
                hasAnnotation = true;
            }

            if (hasAnnotation) {
                s.setAnnotationGroup(ag);
            }

            return s;
        } catch (RecognitionException e) {
            throw new InvalidArgument("statement syntax is invalid - "
                    + belSyntax);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELStatement convert(Statement s) {
        throw new UnsupportedOperationException(
                "Do not support conversion to BELScript.");
    }

    protected void repopulateNamespaces(final Statement statement) {
        if (hasItems(statement.getAllParameters())) {
            for (Parameter p : statement.getAllParameters()) {
                Namespace ns = p.getNamespace();
                if (ns != null) {
                    String prefix = ns.getPrefix();
                    Namespace namespace = ndefs.get(prefix);

                    if (namespace == null) {
                        continue;
                    }

                    p.setNamespace(namespace);
                }

                // remove quoted ns value boundary if present.
                if (p.getValue() != null) {
                    Matcher m = QUOTED_NS_VALUE.matcher(p.getValue());
                    if (m.matches()) {
                        p.setValue(CommonModelFactory.getInstance()
                                .resolveValue(m.group(1)));
                    }
                }
            }
        }
    }
}
