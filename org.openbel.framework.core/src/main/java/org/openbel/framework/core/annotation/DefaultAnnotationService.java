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
package org.openbel.framework.core.annotation;

import static java.lang.String.format;
import static org.openbel.framework.common.Strings.INVALID_ANNOTATIONS;
import static org.openbel.framework.common.util.ValidationUtilities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.openbel.framework.common.model.Annotation;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.core.compiler.SymbolWarning;

/**
 * Default implementation of {@link AnnotationService}
 *
 * @author Steve Ungerer
 */
public class DefaultAnnotationService implements AnnotationService {

    /**
     * {@inheritDoc}
     */
    @Override
    public void verify(Annotation annotation) throws AnnotationSyntaxWarning {
        try {
            if (isValid(annotation)) return;
        } catch (PatternSyntaxException e) {
            // ignore it
        }
        // Pattern syntax is bad or annotation is not valid

        // The annotation is defined by its annotation definition.
        AnnotationDefinition annodef = annotation.getDefinition();

        String annodefID = annodef.getId();
        String url = annodef.getURL();
        String annoval = annotation.getValue();

        if (url == null) {
            throw new AnnotationSyntaxWarning(annodefID, annoval);
        }
        throw new AnnotationSyntaxWarning(url, annodefID, annoval);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verify(Statement statement) throws SymbolWarning {
        if (statement.getAnnotationGroup() == null) {
            return;
        }
        List<AnnotationSyntaxWarning> l =
                new ArrayList<AnnotationSyntaxWarning>();
        for (Annotation annotation : statement.getAnnotationGroup()
                .getAnnotations()) {
            try {
                verify(annotation);
            } catch (AnnotationSyntaxWarning asw) {
                l.add(asw);
            }
        }
        if (!l.isEmpty()) {
            final String name = statement.toBELLongForm();
            final String fmt = INVALID_ANNOTATIONS;
            final String msg = format(fmt, l.size());
            throw new SymbolWarning(name, msg, l);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verify(Document document) throws SymbolWarning {
        List<AnnotationSyntaxWarning> l =
                new ArrayList<AnnotationSyntaxWarning>();
        for (Annotation annotation : document.getAllAnnotations()) {
            try {
                verify(annotation);
            } catch (AnnotationSyntaxWarning asw) {
                l.add(asw);
            }
        }
        if (!l.isEmpty()) {
            final String name = document.getName();
            final String fmt = INVALID_ANNOTATIONS;
            final String msg = format(fmt, l.size());
            throw new SymbolWarning(name, msg, l);
        }
    }

}
