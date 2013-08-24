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
package org.openbel.framework.core;

import static org.openbel.framework.common.Strings.UTF_8;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openbel.framework.common.MissingEncodingException;
import org.openbel.framework.common.bel.parser.BELParseResults;
import org.openbel.framework.common.bel.parser.BELParser;

public class BELValidatorServiceImpl implements BELValidatorService {

    /**
     * {@inheritDoc}
     */
    @Override
    public BELParseResults validateBELScript(String belScriptText) {
        return BELParser.parse(belScriptText);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELParseResults validateBELScript(File belScriptFile) {
        try {
            String belScriptText = FileUtils.readFileToString(belScriptFile,
                    UTF_8);
            return validateBELScript(belScriptText);
        } catch (IOException e) {
            throw new MissingEncodingException(UTF_8, e);
        }
    }
}
