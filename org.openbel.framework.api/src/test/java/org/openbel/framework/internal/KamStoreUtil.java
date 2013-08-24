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
package org.openbel.framework.internal;

import org.openbel.framework.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.internal.KAMStoreDaoImpl.TermParameter;

public class KamStoreUtil {

    public static BelTerm createBelTerm(final Integer id, final String lbl) {
        return new BelTerm(id, lbl);
    }

    public static Namespace createNamespace(final Integer id,
            final String prefix, final String resourceLocation) {
        return new Namespace(id, prefix, resourceLocation);
    }

    public static TermParameter createTermParameter(final Integer id,
            Namespace ns, String value) {
        return new TermParameter(id, ns, value);
    }
}
