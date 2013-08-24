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
package org.openbel.framework.api;

import java.util.HashSet;
import java.util.Set;

import org.openbel.framework.common.enums.FunctionEnum;

public class FunctionTypeFilterCriteria extends FilterCriteria {

    private Set<FunctionEnum> valueSet;

    public FunctionTypeFilterCriteria() {
        super();
        this.valueSet = new HashSet<FunctionEnum>();
    }

    @Override
    public Set<FunctionEnum> getValues() {
        return valueSet;
    }

    public FunctionTypeFilterCriteria add(FunctionEnum functionType) {
        valueSet.add(functionType);
        return this;
    }
}
