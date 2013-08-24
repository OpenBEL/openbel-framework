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
package org.openbel.framework.tools.xgmml;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;

/**
 * Defines a node data structure for XGMML output.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
class XGMMLObjects {

    /**
     * Defines a node data structure for XGMML output.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    protected static class Node {
        public Integer id;
        public FunctionEnum function;
        public String label;
    }

    /**
     * Defines an edge data structure for XGMML output.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    protected static class Edge {
        public Integer id;
        public Integer source;
        public Integer target;
        public RelationshipType rel;
    }

    /**
     * Defines a statement data structure for XGMML output.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    protected static class Statement {
        public Integer id;
        public Integer documentId;
        public String belSyntax;
    }

    /**
     * Defines a document data structure to include as a graph attribute to
     * the XGMML output.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    protected static class Document {
        public Integer id;
        public String documentName;
    }

    /**
     * Defines a term data structure to include as a node attribute to the
     * XGMML output.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    protected static class Term {
        public Integer id;
        public String termLabel;
    }
}
