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
package org.openbel.framework.common.bel.converters;

import static org.openbel.framework.common.BELUtilities.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openbel.bel.model.BELAnnotation;
import org.openbel.bel.model.BELCitation;
import org.openbel.bel.model.BELEvidence;
import org.openbel.bel.model.BELStatement;
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
