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
package org.openbel.framework.core.protonetwork;

import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.noItems;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.protonetwork.model.*;
import org.openbel.framework.common.protonetwork.model.AnnotationDefinitionTable.TableAnnotationDefinition;
import org.openbel.framework.common.protonetwork.model.AnnotationValueTable.TableAnnotationValue;
import org.openbel.framework.common.protonetwork.model.DocumentTable.DocumentHeader;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ParameterTable.TableParameter;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable.TableProtoEdge;
import org.openbel.framework.common.protonetwork.model.StatementAnnotationMapTable.AnnotationPair;
import org.openbel.framework.common.protonetwork.model.StatementTable.TableStatement;

import au.com.bytecode.opencsv.CSVWriter;

public class TextProtoNetworkExternalizer implements ProtoNetworkExternalizer {

    /**
     * TextProtoNetworkExternalizer does not support the read proto network
     * operation.
     *
     * @throws UnsupportedOperationException
     *             , if this method is called
     */
    @Override
    public ProtoNetwork readProtoNetwork(
            ProtoNetworkDescriptor protoNetworkDescriptor)
            throws ProtoNetworkError {
        throw new UnsupportedOperationException(
                "Not implemented, should it be?");
    }

    /**
     * Writes a text table-based proto network and produces a descriptor
     * including all of the table files.
     *
     * @param pn
     *            {@link ProtoNetwork}, the proto network, which cannot be null
     * @param protoNetworkRootPath
     *            {@link String}, the root path where proto network files should
     *            be created
     * @return {@link ProtoNetworkDescriptor}, the proto network descriptor
     * @throws ProtoNetworkError Thrown if there was an error writing the {@link ProtoNetwork}
     * @throws InvalidArgument Thrown if {@code protoNetwork} is null
     */
    @Override
    public ProtoNetworkDescriptor writeProtoNetwork(ProtoNetwork pn,
            String protoNetworkRootPath) throws ProtoNetworkError {
        if (pn == null) throw new InvalidArgument("protoNetwork is null");

        CSVWriter allWriter;
        String path = asPath(protoNetworkRootPath, "all.tbl");
        try {
            allWriter = new CSVWriter(new FileWriter(path), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);
        } catch (IOException e) {
            final String msg = "Cannot open all tbl debug file for write";
            throw new ProtoNetworkError(path, msg, e);
        }

        CSVWriter tdvWriter;

        allWriter.writeNext(new String[] { "Document table" });

        // write document table
        DocumentTable dt = pn.getDocumentTable();
        File dfile = new File(protoNetworkRootPath + File.separator
                + "document.tbl");
        try {
            tdvWriter = new CSVWriter(new FileWriter(dfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "DocumentIndex", "Name",
                    "Description", "Version", "Copyright", "Disclaimer",
                    "ContactInfo", "Authors", "Licenses");

            List<DocumentHeader> dhs = dt.getDocumentHeaders();
            for (int i = 0; i < dhs.size(); i++) {
                DocumentHeader dh = dhs.get(i);

                tdvWriter
                        .writeNext(new String[] {
                                String.valueOf(i),
                                dh.getName() == null ? "-" : dh.getName(),
                                dh.getDescription() == null ? "-" : dh
                                        .getDescription(),
                                dh.getVersion() == null ? "-" : dh.getVersion(),
                                dh.getCopyright() == null ? "-" : dh
                                        .getCopyright(),
                                dh.getDisclaimer() == null ? "-" : dh
                                        .getDisclaimer(),
                                dh.getContactInfo() == null ? "-" : dh
                                        .getContactInfo(),
                                dh.getAuthors() == null ? "-" : dh.getAuthors(),
                                dh.getLicenses() == null ? "-" : dh
                                        .getLicenses() });

                allWriter
                        .writeNext(new String[] {
                                String.valueOf(i),
                                dh.getName() == null ? "-" : dh.getName(),
                                dh.getDescription() == null ? "-" : dh
                                        .getDescription(),
                                dh.getVersion() == null ? "-" : dh.getVersion(),
                                dh.getCopyright() == null ? "-" : dh
                                        .getCopyright(),
                                dh.getDisclaimer() == null ? "-" : dh
                                        .getDisclaimer(),
                                dh.getContactInfo() == null ? "-" : dh
                                        .getContactInfo(),
                                dh.getAuthors() == null ? "-" : dh.getAuthors(),
                                dh.getLicenses() == null ? "-" : dh
                                        .getLicenses() });
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String msg = "Cannot create document file for proto-network";
            throw new ProtoNetworkError(dfile.getAbsolutePath(), msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Namespace table" });

        // Write namespace table
        NamespaceTable nt = pn.getNamespaceTable();
        File nsfile = new File(protoNetworkRootPath + File.separator
                + "namespace.tbl");
        try {
            tdvWriter = new CSVWriter(new FileWriter(nsfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "NamespaceIndex", "Prefix",
                    "ResourceLocation");

            for (TableNamespace ns : nt.getNamespaces()) {
                String nsp =
                        StringUtils.isBlank(ns.getPrefix()) ? "-" : ns
                                .getPrefix();

                tdvWriter.writeNext(new String[] {
                        String.valueOf(nt.getNamespaceIndex().get(ns)),
                        String.valueOf(nsp),
                        String.valueOf(ns.getResourceLocation()) });

                allWriter.writeNext(new String[] {
                        String.valueOf(nt.getNamespaceIndex().get(ns)),
                        String.valueOf(nsp),
                        String.valueOf(ns.getResourceLocation()) });
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String msg = "Cannot create namespace file for proto-network";
            final String name = nsfile.getAbsolutePath();
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Document Namespace table" });

        // Write document-namespace table
        path = asPath(protoNetworkRootPath, "document-namespace.tbl");
        File dnsfile = new File(path);
        try {
            tdvWriter = new CSVWriter(new FileWriter(dnsfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "DocumentIndex", "NamespaceIndex");

            Map<Integer, List<Integer>> dnsi = nt.getDocumentNamespaces();
            for (final Entry<Integer, List<Integer>> e : dnsi.entrySet()) {
                Integer di = e.getKey();
                List<Integer> dnsl = e.getValue();
                if (noItems(dnsl)) continue;
                for (Integer nsi : dnsl) {
                    tdvWriter.writeNext(new String[] {
                            String.valueOf(di),
                            String.valueOf(nsi) });

                    allWriter.writeNext(new String[] {
                            String.valueOf(di),
                            String.valueOf(nsi) });
                }
            }
            tdvWriter.close();
        } catch (IOException e) {
            final String name = path;
            final String msg =
                    "Cannot create document namespace file for proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Parameter table" });

        // write parameter table
        ParameterTable parameterTable = pn.getParameterTable();
        NamespaceTable nsTable = pn.getNamespaceTable();
        File pfile = new File(protoNetworkRootPath + File.separator
                + "parameter.tbl");
        try {
            tdvWriter = new CSVWriter(new FileWriter(pfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "ParameterIndex",
                    "NamespaceIndex", "Value", "(Global ParameterIndex)");

            for (final TableParameter parameter : parameterTable
                    .getTableParameters()) {
                // fetch the namespace value
                TableNamespace tn = parameter.getNamespace();
                Integer nsIndex = nsTable.getNamespaceIndex().get(tn);
                String nsValue = "-";
                if (nsIndex != null) {
                    nsValue = String.valueOf(nsIndex);
                }

                if (parameterTable.getGlobalIndex().isEmpty()) {
                    tdvWriter.writeNext(new String[] {
                            String.valueOf(parameterTable
                                    .getTableParameterIndex()
                                    .get(parameter)),
                            nsValue,
                            parameter.getValue() });

                    allWriter.writeNext(new String[] {
                            String.valueOf(parameterTable
                                    .getTableParameterIndex()
                                    .get(parameter)),
                            nsValue,
                            parameter.getValue() });
                } else {
                    Integer globalIndex = parameterTable.getGlobalIndex().get(
                            parameterTable.getTableParameterIndex().get(
                                    parameter));

                    tdvWriter.writeNext(new String[] {
                            String.valueOf(parameterTable
                                    .getTableParameterIndex().get(parameter)),
                            nsValue,
                            parameter.getValue(),
                            globalIndex.toString() });

                    allWriter.writeNext(new String[] {
                            String.valueOf(parameterTable
                                    .getTableParameterIndex().get(parameter)),
                            nsValue,
                            parameter.getValue(),
                            globalIndex.toString() });
                }
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String msg = "Cannot create parameter file for proto-network";
            final String name = pfile.getAbsolutePath();
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Term table" });

        // write term table
        TermTable termTable = pn.getTermTable();
        File tfile = new File(protoNetworkRootPath + File.separator
                + "term.tbl");
        try {
            tdvWriter = new CSVWriter(new FileWriter(tfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "TermIndex", "Value",
                    "(Global TermIndex)");

            List<String> termValues = termTable.getTermValues();
            Map<Integer, Integer> tgi = termTable.getGlobalTermIndex();
            for (int i = 0; i < termValues.size(); i++) {

                if (tgi.isEmpty()) {
                    tdvWriter.writeNext(new String[] { String.valueOf(i),
                            termValues.get(i) });

                    allWriter.writeNext(new String[] { String.valueOf(i),
                            termValues.get(i) });
                } else {
                    Integer globalIndex = tgi.get(i);
                    tdvWriter.writeNext(new String[] { String.valueOf(i),
                            termValues.get(i),
                            globalIndex.toString() });

                    allWriter.writeNext(new String[] { String.valueOf(i),
                            termValues.get(i),
                            globalIndex.toString() });
                }
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String msg = "Cannot create term file for proto-network";
            final String name = tfile.getAbsolutePath();
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Term Parameter table" });

        // write term-parameter table
        TermParameterMapTable termParameterMapTable = pn
                .getTermParameterMapTable();
        File tpmtfile = new File(protoNetworkRootPath
                + File.separator + "term-parameter-map.tbl");
        try {
            tdvWriter = new CSVWriter(
                    new FileWriter(tpmtfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "TermIndex", "ParameterIndex",
                    "Ordinal");

            for (final Integer termIndex : termParameterMapTable
                    .getTermParameterIndex().keySet()) {
                List<Integer> parameterIndexes = termParameterMapTable
                        .getTermParameterIndex().get(termIndex);

                int ord = 0;
                for (Integer parameterIndex : parameterIndexes) {
                    tdvWriter.writeNext(new String[] {
                            String.valueOf(termIndex),
                            String.valueOf(parameterIndex),
                            String.valueOf(ord) });

                    allWriter.writeNext(new String[] {
                            String.valueOf(termIndex),
                            String.valueOf(parameterIndex),
                            String.valueOf(ord) });

                    ord++;
                }
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String name = tpmtfile.getAbsolutePath();
            final String msg = "Cannot create term to parameter mapping file " +
                    "for proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Statement table" });

        // write statement table
        StatementTable statementTable = pn.getStatementTable();
        File sfile = new File(protoNetworkRootPath + File.separator
                + "statement.tbl");
        try {
            tdvWriter = new CSVWriter(new FileWriter(sfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "StatementIndex",
                    "SubjectTermIndex", "Relationship", "ObjectTermIndex",
                    "NestedSubjectTermIndex", "NestedRelationship",
                    "NestedObjectTermIndex");

            List<TableStatement> statements = statementTable.getStatements();
            for (int i = 0; i < statements.size(); i++) {
                TableStatement statement = statements.get(i);

                String si = String.valueOf(i);
                if (statement.getRelationshipName() == null) {
                    tdvWriter.writeNext(new String[] {
                            si,
                            String.valueOf(statement.getSubjectTermId()),
                            "-", "-", "-", "-", "-" });

                    allWriter.writeNext(new String[] {
                            si,
                            String.valueOf(statement.getSubjectTermId()),
                            "-", "-", "-", "-", "-" });
                } else if (statement.getObjectTermId() != null) {
                    tdvWriter.writeNext(new String[] {
                            si,
                            String.valueOf(statement.getSubjectTermId()),
                            statement.getRelationshipName(),
                            String.valueOf(statement.getObjectTermId()),
                            "-", "-", "-" });

                    allWriter.writeNext(new String[] {
                            si,
                            String.valueOf(statement.getSubjectTermId()),
                            statement.getRelationshipName(),
                            String.valueOf(statement.getObjectTermId()),
                            "-", "-", "-" });
                } else {
                    tdvWriter.writeNext(new String[] {
                            si,
                            String.valueOf(statement.getSubjectTermId()),
                            statement.getRelationshipName(),
                            String.valueOf(statement.getObjectTermId()),
                            String.valueOf(statement.getNestedSubject()),
                            statement.getNestedRelationship(),
                            String.valueOf(statement.getNestedObject()) });

                    allWriter.writeNext(new String[] {
                            si,
                            String.valueOf(statement.getSubjectTermId()),
                            statement.getRelationshipName(),
                            String.valueOf(statement.getObjectTermId()),
                            String.valueOf(statement.getNestedSubject()),
                            statement.getNestedRelationship(),
                            String.valueOf(statement.getNestedObject()) });
                }
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String name = sfile.getAbsolutePath();
            final String msg = "Cannot create statement file for proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Statement Document table" });

        // Write document-statement table
        path = asPath(protoNetworkRootPath, "document-statement.tbl");
        File dsfile = new File(path);
        try {
            tdvWriter = new CSVWriter(new FileWriter(dsfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "DocumentIndex", "StatementIndex");

            List<TableStatement> sts = statementTable.getStatements();
            Map<Integer, Integer> dsm = statementTable.getStatementDocument();
            for (int si = 0; si < sts.size(); si++) {

                tdvWriter.writeNext(new String[] {
                        String.valueOf(dsm.get(si)),
                        String.valueOf(si) });

                allWriter.writeNext(new String[] {
                        String.valueOf(dsm.get(si)),
                        String.valueOf(si) });
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String name = path;
            final String msg =
                    "Cannot create document statement file for proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Annotation Definition table" });

        // Write annotation definition table
        AnnotationDefinitionTable adt = pn.getAnnotationDefinitionTable();
        File adfile = new File(protoNetworkRootPath + File.separator
                + "annotation-definition.tbl");
        try {
            tdvWriter = new CSVWriter(new FileWriter(adfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "AnnotationDefinitionIndex",
                    "Name", "Description", "Usage", "Domain",
                    "AnnotationDefinitionType");

            for (final TableAnnotationDefinition ad : adt
                    .getAnnotationDefinitions()) {
                tdvWriter
                        .writeNext(new String[] {
                                String.valueOf(adt.getDefinitionIndex().get(ad)),
                                ad.getName(),
                                ad.getDescription() == null ? "-" : ad
                                        .getDescription(),
                                ad.getUsage() == null ? "-" : ad.getUsage(),
                                ad.getAnnotationDomain() == null ? "-" : ad
                                        .getAnnotationDomain(),
                                String.valueOf(ad.getAnnotationType()) });

                allWriter
                        .writeNext(new String[] {
                                String.valueOf(adt.getDefinitionIndex().get(ad)),
                                ad.getName(),
                                ad.getDescription() == null ? "-" : ad
                                        .getDescription(),
                                ad.getUsage() == null ? "-" : ad.getUsage(),
                                ad.getAnnotationDomain() == null ? "-" : ad
                                        .getAnnotationDomain(),
                                String.valueOf(ad.getAnnotationType()) });
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String name = adfile.getAbsolutePath();
            final String msg =
                    "Cannot create annotation definition file for proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter
                .writeNext(new String[] { "Document Annotation Definition table" });

        // Write document annotation definition table
        path =
                asPath(protoNetworkRootPath,
                        "document-annotation-definition.tbl");
        File dadfile = new File(path);
        try {
            tdvWriter = new CSVWriter(new FileWriter(dadfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "DocumentIndex",
                    "AnnotationDefinitionIndex");

            Map<Integer, Set<Integer>> dnsi = adt
                    .getDocumentAnnotationDefinitions();
            for (final Entry<Integer, Set<Integer>> e : dnsi.entrySet()) {
                Integer di = e.getKey();
                Set<Integer> adl = e.getValue();
                if (noItems(adl)) continue;
                for (Integer adi : adl) {
                    tdvWriter.writeNext(new String[] {
                            String.valueOf(di),
                            String.valueOf(adi) });

                    allWriter.writeNext(new String[] {
                            String.valueOf(di),
                            String.valueOf(adi) });
                }
            }
            tdvWriter.close();
        } catch (IOException e) {
            final String name = path;
            final String msg = "Cannot create document annotation definition " +
                    "file for proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Annotation Value table" });

        // Write annotation value table
        AnnotationValueTable avt = pn.getAnnotationValueTable();
        File avfile = new File(protoNetworkRootPath + File.separator
                + "annotation-value.tbl");
        try {
            tdvWriter = new CSVWriter(new FileWriter(avfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "AnnotationValueIndex",
                    "AnnotationDefinitionIndex", "AnnotationValue");

            Map<Integer, TableAnnotationValue> annotationIndex = avt
                    .getIndexValue();
            Set<Integer> annotationIds = annotationIndex.keySet();
            for (Integer annotationId : annotationIds) {
                TableAnnotationValue value = annotationIndex.get(annotationId);

                tdvWriter.writeNext(new String[] {
                        String.valueOf(annotationId),
                        String.valueOf(value.getAnnotationDefinitionId()),
                        value.getAnnotationValue() });
                allWriter.writeNext(new String[] {
                        String.valueOf(annotationId),
                        String.valueOf(value.getAnnotationDefinitionId()),
                        value.getAnnotationValue() });
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String name = avfile.getAbsolutePath();
            final String msg =
                    "Cannot create annotation value file for proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Statement Annotation Map table" });

        // Write statement annotation map table
        StatementAnnotationMapTable samt = pn.getStatementAnnotationMapTable();
        File samtfile = new File(protoNetworkRootPath + File.separator
                + "statement-annotation-map.tbl");
        try {
            tdvWriter = new CSVWriter(new FileWriter(samtfile), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);

            writeHeader(tdvWriter, allWriter, "StatementIndex",
                    "AnnotationDefinitionIndex", "AnnotationValueIndex");

            for (final Map.Entry<Integer, Set<AnnotationPair>> same : samt
                    .getStatementAnnotationPairsIndex().entrySet()) {
                Integer sid = same.getKey();

                for (AnnotationPair ap : same.getValue()) {
                    tdvWriter.writeNext(new String[] {
                            String.valueOf(sid),
                            String.valueOf(ap.getAnnotationDefinitionId()),
                            String.valueOf(ap.getAnnotationValueId()) });

                    allWriter.writeNext(new String[] {
                            String.valueOf(sid),
                            String.valueOf(ap.getAnnotationDefinitionId()),
                            String.valueOf(ap.getAnnotationValueId()) });
                }
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String name = samtfile.getAbsolutePath();
            final String msg = "Cannot create statement annotation map file " +
                    "for proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Proto Node Table" });

        // Write proto node table
        final ProtoNodeTable pnt = pn.getProtoNodeTable();
        final Map<Integer, Integer> eqn = pnt.getEquivalences();
        File pntf = new File(asPath(protoNetworkRootPath, "proto-node.tbl"));
        try {
            tdvWriter = new CSVWriter(new FileWriter(pntf), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);
            writeHeader(tdvWriter, allWriter, "ProtoNodeIndex", "Label",
                    "EquivalenceIndex");

            final List<String> nodes = pnt.getProtoNodes();
            int nodeId = 0;
            for (int i = 0, n = nodes.size(); i < n; i++) {
                String nodeidstr = String.valueOf(nodeId);
                String eqidstr = String.valueOf(eqn.get(i));
                tdvWriter.writeNext(new String[] {
                        nodeidstr,
                        nodes.get(i),
                        eqidstr });
                allWriter.writeNext(new String[] {
                        nodeidstr,
                        nodes.get(i),
                        eqidstr });

                nodeId++;
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String name = pntf.getAbsolutePath();
            final String msg =
                    "Cannot create proto node file for proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Term Proto Node Table" });

        // Write term proto node table
        final TermTable tt = pn.getTermTable();
        File tpnf =
                new File(asPath(protoNetworkRootPath, "term-proto-node.tbl"));
        try {
            tdvWriter = new CSVWriter(new FileWriter(tpnf), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);
            writeHeader(tdvWriter, allWriter, "TermIndex", "ProtoNodeIndex");

            final List<String> terms = tt.getTermValues();
            final Map<Integer, Integer> visited = pnt.getTermNodeIndex();
            for (int i = 0, n = terms.size(); i < n; i++) {
                String nodeidstr = String.valueOf(visited.get(i));
                tdvWriter.writeNext(new String[] {
                        String.valueOf(i),
                        nodeidstr });
                allWriter.writeNext(new String[] {
                        String.valueOf(i),
                        nodeidstr });
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String name = tpnf.getAbsolutePath();
            final String msg = "Cannot create term proto node file for " +
                    "proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Proto Edge Table" });

        // Write proto edge table
        final ProtoEdgeTable pet = pn.getProtoEdgeTable();
        final Map<Integer, Integer> edgeEq = pet.getEquivalences();
        File petf = new File(asPath(protoNetworkRootPath, "proto-edge.tbl"));
        try {
            tdvWriter = new CSVWriter(new FileWriter(petf), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);
            writeHeader(tdvWriter, allWriter, "ProtoEdgeIndex",
                    "SourceProtoNodeIndex", "Relationship",
                    "TargetProtoNodeIndex", "EquivalenceIndex");

            final List<TableProtoEdge> edges = pet.getProtoEdges();
            for (int i = 0, n = edges.size(); i < n; i++) {
                final TableProtoEdge edge = edges.get(i);

                final String ei = String.valueOf(i);
                final String source = String.valueOf(edge.getSource());
                final String rel = edge.getRel();
                final String target = String.valueOf(edge.getTarget());

                String eqIndex = edgeEq.get(i).toString();

                tdvWriter.writeNext(new String[] { ei, source, rel, target,
                        eqIndex });
                allWriter.writeNext(new String[] { ei, source, rel, target,
                        eqIndex });
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String name = tpnf.getAbsolutePath();
            final String msg = "Cannot create proto edge file for " +
                    "proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        allWriter.writeNext(new String[0]);
        allWriter.writeNext(new String[] { "Proto Edge Statement Table" });

        // Write proto edge statement table
        final List<TableProtoEdge> edges = pet.getProtoEdges();
        File spet =
                new File(asPath(protoNetworkRootPath,
                        "proto-edge-statement.tbl"));
        try {
            tdvWriter = new CSVWriter(new FileWriter(spet), '\t',
                    CSVWriter.NO_QUOTE_CHARACTER);
            writeHeader(tdvWriter, allWriter, "ProtoEdgeIndex",
                    "StatementIndex");

            final Map<Integer, Set<Integer>> edgeStmts =
                    pet.getEdgeStatements();
            for (int i = 0, n = edges.size(); i < n; i++) {
                final String ei = String.valueOf(i);

                final Set<Integer> stmts = edgeStmts.get(i);
                if (hasItems(stmts)) {
                    for (final Integer stmt : stmts) {
                        final String stmtstring = String.valueOf(stmt);
                        tdvWriter.writeNext(new String[] { ei, stmtstring });
                        allWriter.writeNext(new String[] { ei, stmtstring });
                    }
                }
            }

            tdvWriter.close();
        } catch (IOException e) {
            final String name = tpnf.getAbsolutePath();
            final String msg = "Cannot create proto edge file for " +
                    "proto-network";
            throw new ProtoNetworkError(name, msg, e);
        }

        try {
            allWriter.close();
        } catch (IOException e) {
            final String name = samtfile.getAbsolutePath();
            final String msg = "Cannot close all tbl debug file after write";
            throw new ProtoNetworkError(name, msg, e);
        }

        return new TextProtoNetworkDescriptor(protoNetworkRootPath, dfile,
                nsfile, dnsfile, pfile, tfile, tpmtfile, sfile, dsfile, adfile,
                dadfile, avfile, samtfile, pntf, tpnf, petf, spet);
    }

    private void writeHeader(final CSVWriter allWriter,
            final CSVWriter tdvWriter, String... headers) {
        if (headers != null && headers.length > 0) {
            tdvWriter.writeNext(headers);
            allWriter.writeNext(headers);
        }
    }
}
