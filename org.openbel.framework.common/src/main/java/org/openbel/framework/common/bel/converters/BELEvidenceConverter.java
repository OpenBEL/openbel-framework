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

import org.openbel.bel.model.BELEvidence;
import org.openbel.framework.common.model.CommonModelFactory;
import org.openbel.framework.common.model.Evidence;

public class BELEvidenceConverter extends BELConverter<BELEvidence, Evidence> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Evidence convert(BELEvidence be) {
        if (be == null) {
            return null;
        }

        return CommonModelFactory.getInstance().createEvidence(
                be.getEvidenceLine());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELEvidence convert(Evidence e) {
        if (e == null) {
            return null;
        }

        return new BELEvidence(e.getValue());
    }
}
