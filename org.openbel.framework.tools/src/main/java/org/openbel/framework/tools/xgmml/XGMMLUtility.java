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
package org.openbel.framework.tools.xgmml;

import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.hasLength;

import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.tools.xgmml.XGMMLObjects.Edge;
import org.openbel.framework.tools.xgmml.XGMMLObjects.Node;

/**
 * XGMMLUtility provides utility methods for writing graph, node, and edge
 * sections of an XGMML xml document.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class XGMMLUtility {
    /**
     * Format: {@code %s %s %d %d}. In order, graphics type, fill color, x-pos,
     * and y-pos.
     */
    private static String NODE_GRAPHICS;
    /**
     * Format: {@code %d %s %d %s}. In order, line width, fill color, target
     * arrow indicator, and edge label.
     */
    private static String EDGE_GRAPHICS;
    /**
     * Format: {@code %s %s %s}.
     */
    private static String EDGE_LABEL;
    private static String DFLT_NODE_SHAPE;
    private static String DFLT_EDGE_COLOR;
    private static String DFLT_NODE_COLOR;
    static {
        NODE_GRAPHICS = "    <graphics type='%s' fill='%s' ";
        NODE_GRAPHICS += "x='%d' y='%d' h='20.0' w='80.0' ";
        NODE_GRAPHICS += "cy:nodeLabel='%s'/>\n";
        EDGE_GRAPHICS = "    <graphics width='%d' fill='%s' ";
        EDGE_GRAPHICS += "cy:targetArrow='%d' cy:edgeLabel='%s'/>\n";
        EDGE_LABEL = "%s (%s) %s";
        DFLT_NODE_SHAPE = "rectangle";
        DFLT_EDGE_COLOR = "0,0,0";
        DFLT_NODE_COLOR = "150,150,150";
    }

    /**
     * Returns a shape for the supplied {@link FunctionEnum}.
     * 
     * @param fe {@link FunctionEnum}
     * @return Non-null {@link String}
     * @see #DFLT_NODE_SHAPE
     */
    public static String type(FunctionEnum fe) {
        if (fe == null) {
            return DFLT_NODE_SHAPE;
        }
        switch (fe) {
        case ABUNDANCE:
            return "ver_ellipsis";
        case BIOLOGICAL_PROCESS:
            return "rhombus";
        case CATALYTIC_ACTIVITY:
            return "hexagon";
        case CELL_SECRETION:
            return "arc";
        case CELL_SURFACE_EXPRESSION:
            return "arc";
        case CHAPERONE_ACTIVITY:
            return "hexagon";
        case COMPLEX_ABUNDANCE:
            return "hor_ellipsis";
        case COMPOSITE_ABUNDANCE:
            return "hor_ellipsis";
        case DEGRADATION:
            return "hor_ellipsis";
        case GENE_ABUNDANCE:
            return "hor_ellipsis";
        case GTP_BOUND_ACTIVITY:
            return "hexagon";
        case KINASE_ACTIVITY:
            return "hexagon";
        case MICRORNA_ABUNDANCE:
            return "hor_ellipsis";
        case MOLECULAR_ACTIVITY:
            return "hexagon";
        case PATHOLOGY:
            return "rhombus";
        case PEPTIDASE_ACTIVITY:
            return "hexagon";
        case PHOSPHATASE_ACTIVITY:
            return "hexagon";
        case PRODUCTS:
        case PROTEIN_ABUNDANCE:
            return "hor_ellipsis";
        case REACTANTS:
        case RIBOSYLATION_ACTIVITY:
            return "hexagon";
        case RNA_ABUNDANCE:
            return "hor_ellipsis";
        case TRANSCRIPTIONAL_ACTIVITY:
            return "hexagon";
        case TRANSPORT_ACTIVITY:
            return "hexagon";
        }
        return DFLT_NODE_SHAPE;
    }

    /**
     * Returns an RGB tuple of the form {@code "x,x,x"} for the supplied
     * {@link FunctionEnum}. Defaults to gray; RGB {@code "150,150,150"}.
     * 
     * @param fe {@link FunctionEnum}
     * @return Non-null {@link String}
     */
    public static String color(FunctionEnum fe) {
        if (fe == null) {
            return DFLT_NODE_COLOR;
        }
        switch (fe) {
        case ABUNDANCE:
            return "40,255,85";
        case BIOLOGICAL_PROCESS:
            return "255,51,102";
        case CATALYTIC_ACTIVITY:
            return "100,100,255";
        case CELL_SECRETION:
            return "204,204,255";
        case CELL_SURFACE_EXPRESSION:
            return "204,204,255";
        case CHAPERONE_ACTIVITY:
            return "100,100,255";
        case COMPLEX_ABUNDANCE:
            return "102,153,255";
        case COMPOSITE_ABUNDANCE:
            return "222,255,255";
        case DEGRADATION:
            return "255,51,102";
        case GENE_ABUNDANCE:
            return "204,255,204";
        case GTP_BOUND_ACTIVITY:
            return "100,100,255";
        case KINASE_ACTIVITY:
            return "100,100,255";
        case MICRORNA_ABUNDANCE:
            return "0,255,150";
        case MOLECULAR_ACTIVITY:
            return "100,100,255";
        case PATHOLOGY:
            return "255,51,102";
        case PEPTIDASE_ACTIVITY:
            return "100,100,255";
        case PHOSPHATASE_ACTIVITY:
            return "100,100,255";
        case PROTEIN_ABUNDANCE:
            return "85,255,255";
        case REACTION:
            return "255,51,102";
        case RIBOSYLATION_ACTIVITY:
            return "100,100,255";
        case RNA_ABUNDANCE:
            return "40,255,85";
        case TRANSCRIPTIONAL_ACTIVITY:
            return "100,100,255";
        case TRANSPORT_ACTIVITY:
            return "100,100,255";
        }
        return DFLT_NODE_COLOR;
    }

    /**
     * Returns an RGB tuple of the form {@code "x,x,x"} for the supplied
     * {@link RelationshipType}. Defaults to black; RGB {@code "0,0,0"}.
     * 
     * @param fe {@link RelationshipType}
     * @return Non-null {@link String}
     */
    public static String color(RelationshipType rel) {
        if (rel == null) {
            return DFLT_EDGE_COLOR;
        }
        switch (rel) {
        case ACTS_IN:
            return "153,153,153";
        case HAS_COMPONENT:
            return "153,153,153";
        case HAS_MEMBER:
            return "153,153,153";
        case HAS_MODIFICATION:
            return "153,153,153";
        case HAS_PRODUCT:
            return "153,153,153";
        case HAS_VARIANT:
            return "153,153,153";
        case INCLUDES:
            return "153,153,153";
        case IS_A:
            return "153,153,153";
        case REACTANT_IN:
            return "153,153,153";
        case SUB_PROCESS_OF:
            return "153,153,153";
        case TRANSCRIBED_TO:
            return "153,153,153";
        case TRANSLATED_TO:
            return "153,153,153";
        case TRANSLOCATES:
            return "153,153,153";
        }
        return DFLT_EDGE_COLOR;
    }

    /**
     * Write the XGMML start using the {@code graphName} as the label.
     * 
     * @param name {@link String}, the name of the XGMML graph
     * @param writer {@link PrintWriter}, the writer
     */
    public static void writeStart(String name, PrintWriter writer) {
        StringBuilder sb = new StringBuilder();
        sb.append("<graph xmlns='http://www.cs.rpi.edu/XGMML' ")
                .append("xmlns:ns2='http://www.w3.org/1999/xlink' ")
                .append("xmlns:cy='http://www.cytoscape.org' ")
                .append("Graphic='1' label='").append(name)
                .append("' directed='1'>\n");
        writer.write(sb.toString());
    }

    /**
     * Write an XGMML {@code <node>} from {@code node} properties.
     * 
     * @param node {@link Node}, the node to write
     * @param writer {@link PrintWriter}, the writer
     */
    public static void writeNode(Node node, List<BelTerm> supportingTerms,
            PrintWriter writer) {
        int x = new Random().nextInt(200);
        int y = new Random().nextInt(200);

        StringBuilder sb = new StringBuilder();
        sb.append("  <node label='");
        sb.append(node.label);
        sb.append("' id='");
        sb.append(node.id.toString());
        sb.append("'>\n");
        sb.append("    <att name='function type'");
        sb.append(" value='");
        sb.append(node.function.getDisplayValue());
        sb.append("' />\n");
        sb.append("    <att name='parameters'");
        sb.append(" value='");

        String params = "";
        for (BelTerm t : supportingTerms) {
            if (hasLength(params)) {
                params = params.concat("&#10;");
            }
            String label = t.getLabel();
            label = label.replaceAll("&", "&amp;");
            label = label.replaceAll("'", "&quot;");
            params = params.concat(label);
        }
        sb.append(params);
        sb.append("' />\n");

        // Graphics type and fill color
        String graphics = format(NODE_GRAPHICS, type(node.function),
                color(node.function), x, y, params);
        sb.append(graphics);

        sb.append("  </node>\n");
        writer.write(sb.toString());
    }

    /**
     * Write an XGMML &lt;edge&gt; from {@code edge} properties.
     * 
     * @param edge {@link Edge}, the edge to write
     * @param writer {@link PrintWriter}, the writer
     */
    public static void writeEdge(Node src, Node tgt, Edge edge,
            PrintWriter writer) {
        StringBuilder sb = new StringBuilder();
        RelationshipType rel = edge.rel;
        String reldispval = rel.getDisplayValue();
        sb.append("  <edge label='");
        String dispval = format(EDGE_LABEL, src.label, rel, tgt.label);
        sb.append(dispval);
        sb.append("' source='");
        sb.append(edge.source.toString());
        sb.append("' target='");
        sb.append(edge.target.toString());
        sb.append("'>\n");
        sb.append("    <att name='relationship type'");
        sb.append(" value='");
        sb.append(reldispval);
        sb.append("' />\n");

        // Edge graphics
        String color = color(rel);
        String graphics = format(EDGE_GRAPHICS, 1, color, 1, reldispval);
        sb.append(graphics);

        sb.append("  </edge>\n");
        writer.write(sb.toString());
    }

    /**
     * Write the XGMML end.
     * 
     * @param writer {@link PrintWriter}, the writer
     */
    public static void writeEnd(PrintWriter writer) {
        writer.write("</graph>");
    }
}
