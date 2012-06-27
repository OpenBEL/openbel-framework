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
package org.openbel.framework.compiler;

import static java.util.Collections.emptySet;
import static org.openbel.framework.common.BELUtilities.constrainedHashSet;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.BELUtilities.sizedHashMap;
import static org.openbel.framework.common.enums.FunctionEnum.PROTEIN_ABUNDANCE;
import static org.openbel.framework.common.enums.RelationshipType.IS_A;
import static org.openbel.framework.common.protonetwork.model.ProtoNetwork.NAMESPACE_INDEX;
import static org.openbel.framework.common.protonetwork.model.ProtoNetwork.PARAM_INDEX;
import static org.openbel.framework.common.protonetwork.model.ProtoNetwork.TERM_INDEX;
import static org.openbel.framework.common.protonetwork.model.TermTable.PARAMETER_SUBSTITUTION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.lang.ComplexAbundance;
import org.openbel.framework.common.lang.ProteinAbundance;
import org.openbel.framework.common.model.DataFileIndex;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.EquivalenceDataIndex;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Statement.Object;
import org.openbel.framework.common.model.StatementGroup;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.protonetwork.model.NamespaceTable;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ParameterTable;
import org.openbel.framework.common.protonetwork.model.ParameterTable.TableParameter;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.common.protonetwork.model.TermParameterMapTable;
import org.openbel.framework.common.protonetwork.model.TermTable;
import org.openbel.framework.core.equivalence.EquivalenceMapResolutionFailure;
import org.openbel.framework.core.indexer.JDBMEquivalenceLookup;
import org.openbel.framework.core.protonetwork.ProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.ProtoNetworkService;

/**
 * BEL compiler phase three implementation.
 */
public class PhaseThreeImpl implements DefaultPhaseThree {
    private final ProtoNetworkService protoNetworkService;
    private final DefaultPhaseTwo p2;

    private final static String PF_LITERAL;
    private final static String NC_LITERAL;
    private final static String[] ACT_LITERALS;
    static {
        String suffix = "(" + PARAMETER_SUBSTITUTION + ")";
        // proteinAbundance(#) - the p() form w/in the proto-network
        PF_LITERAL = ProteinAbundance.NAME.concat(suffix);
        // complexAbundance(#) - the complex() form w/in the proto-network
        NC_LITERAL = ComplexAbundance.NAME.concat(suffix);

        final String ACT_TERM = "(".concat(PF_LITERAL).concat(")");
        final Set<FunctionEnum> actFunctions = FunctionEnum.getActivities();
        ACT_LITERALS = new String[actFunctions.size()];
        int i = 0;
        for (final FunctionEnum f : actFunctions) {
            ACT_LITERALS[i++] = f.getDisplayValue().concat(ACT_TERM);
        }
    }

    /**
     * Creates the phase three implementation with the associated
     * {@link ProtoNetworkService}.
     *
     * @param protoNetSvc Proto-network service
     * @param p2 Selventa phase two
     */
    public PhaseThreeImpl(final ProtoNetworkService protoNetSvc,
            final DefaultPhaseTwo p2) {
        this.protoNetworkService = protoNetSvc;
        this.p2 = p2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentModificationResult pruneFamilies(boolean expand,
            final Document families,
            final ProtoNetwork network) {

        DocumentModificationResult pr = new DocumentModificationResult();

        // Search for all p(#) parameters within the network.
        Set<TableEntry> networkLiterals = search(network, PF_LITERAL);

        // Load the equivalences
        Set<EquivalenceDataIndex> equivs;
        try {
            equivs = p2.stage2LoadNamespaceEquivalences();
        } catch (EquivalenceMapResolutionFailure f) {
            // Unrecoverable error
            pr.addError(f.getUserFacingMessage());
            pr.setSuccess(false);
            return pr;
        }

        // Map namespace to lookup
        Map<String, JDBMEquivalenceLookup> lookups =
                sizedHashMap(equivs.size());
        for (final EquivalenceDataIndex edi : equivs) {
            String rl = edi.getNamespaceResourceLocation();
            DataFileIndex dfi = edi.getEquivalenceIndex();
            lookups.put(rl, new JDBMEquivalenceLookup(dfi.getIndexPath()));
        }

        // Open the indices
        for (final JDBMEquivalenceLookup jl : lookups.values()) {
            try {
                jl.open();
            } catch (IOException e) {
                pr.addError(e.getMessage());
                pr.setSuccess(false);
                return pr;
            }
        }

        // It's helpful to reference the contents of the protein families
        // BEL document in understanding this section.

        // Establish set of UUIDs relevant for the document's member parameters
        // (this only matters if the expand flag is set)
        Set<SkinnyUUID> uuids = new HashSet<SkinnyUUID>();
        final List<Statement> familyStmts = families.getAllStatements();
        if (expand) {
            uuids = findUUIDs(network, lookups, familyStmts);
        }
        boolean haveUUIDs = !uuids.isEmpty();

        int pruned = 0, total = familyStmts.size();
        List<Parameter> parameters;

        // Pruning is simply a matter of iterating all statements in families...
        FAMILIES: for (final Statement stmt : familyStmts) {

            // ... all protein family parameters
            parameters = stmt.getSubject().getAllParameters();
            for (final Parameter p : parameters) {
                if (!validParameter(p)) {
                    continue;
                }
                Namespace ns = p.getNamespace();
                String val = p.getValue();
                String rl = ns.getResourceLocation();

                // ... do we have the family?
                if (networkLiterals.contains(new TableEntry(val, rl))) {
                    continue FAMILIES;
                }
            }

            Object stmtObj = stmt.getObject();
            if (haveUUIDs && stmtObj != null && stmtObj.getTerm() != null) {
                // ... all family members
                parameters = stmtObj.getTerm().getAllParameters();
                for (final Parameter p : parameters) {
                    if (!validParameter(p)) {
                        continue;
                    }
                    Namespace ns = p.getNamespace();
                    String val = p.getValue();
                    String rl = ns.getResourceLocation();

                    // ... do we have set membership?
                    JDBMEquivalenceLookup jdbmLookup = lookups.get(rl);
                    if (jdbmLookup != null) {
                        SkinnyUUID uuid = jdbmLookup.lookup(val);
                        // Check for set membership, uuid ∊ uuids
                        if (uuids.contains(uuid)) {

                            // This indicates equivalent parameters are
                            // used by both document and network. This
                            // statement will remain (i.e., not be pruned).
                            continue FAMILIES;

                        }
                    }
                }
            }

            // Map statement group to statement set
            Map<StatementGroup, Set<Statement>> groupMap =
                    families.mapStatements();

            // Failed to match both either family or family member.
            // Prune the statement.
            Set<Entry<StatementGroup, Set<Statement>>> entries =
                    groupMap.entrySet();
            for (final Entry<StatementGroup, Set<Statement>> entry : entries) {
                StatementGroup group = entry.getKey();
                Set<Statement> stmts = entry.getValue();
                if (stmts.contains(stmt)) {
                    group.getStatements().remove(stmt);
                    pruned++;
                    break;
                }
            }
        }

        // represent pruned statements as negative
        pr.setDeltaStatements(-(pruned));
        pr.setTotalStatements(total);
        pr.setSuccess(true);

        for (final JDBMEquivalenceLookup jl : lookups.values()) {
            try {
                jl.close();
            } catch (IOException e) {
                pr.addWarning(e.getMessage());
            }
        }

        return pr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentModificationResult inferFamilies(Document d, ProtoNetwork p) {
        final DocumentModificationResult pr = new DocumentModificationResult();

        // Load the equivalences
        Set<EquivalenceDataIndex> equivs;
        try {
            equivs = p2.stage2LoadNamespaceEquivalences();
        } catch (EquivalenceMapResolutionFailure f) {
            // Unrecoverable error
            pr.addError(f.getUserFacingMessage());
            pr.setSuccess(false);
            return pr;
        }

        // Map namespace to lookup
        Map<String, JDBMEquivalenceLookup> lookups =
                sizedHashMap(equivs.size());
        for (final EquivalenceDataIndex edi : equivs) {
            String rl = edi.getNamespaceResourceLocation();
            DataFileIndex dfi = edi.getEquivalenceIndex();
            lookups.put(rl, new JDBMEquivalenceLookup(dfi.getIndexPath()));
        }

        // Open the indices
        for (final JDBMEquivalenceLookup jl : lookups.values()) {
            try {
                jl.open();
            } catch (IOException e) {
                pr.addError(e.getMessage());
                pr.setSuccess(false);
                return pr;
            }
        }

        // get all statements for protein family document
        final List<Statement> stmts = d.getAllStatements();

        // the number of inferred statements
        int inferred = 0;

        // find molecular activity functions referencing protein family param
        TableEntry entry;
        // iterate all activity functions of a proteinAbundance
        final List<Statement> isaStmts = new ArrayList<Statement>();
        final Set<FunctionEnum> actFunctions = FunctionEnum.getActivities();
        for (final FunctionEnum f : actFunctions) {
            final String actLiteral = f.getDisplayValue().concat("(")
                    .concat(PF_LITERAL).concat(")");

            // find parameters for this activity function in the proto network
            final Set<TableEntry> entries = search(p, actLiteral);

            // if no results for activity function, continue to next one
            if (entries.isEmpty()) {
                continue;
            }

            Set<SkinnyUUID> eu = null;

            // entries found, iterate protein family statements
            for (final Statement stmt : stmts) {
                // inspect protein family parameter
                final List<Parameter> sp = stmt.getSubject().getAllParameters();
                for (final Parameter fp : sp) {
                    if (!validParameter(fp)) {
                        continue;
                    }
                    Namespace ns = fp.getNamespace();
                    String val = fp.getValue();
                    String rl = ns.getResourceLocation();

                    // have we seen a molecular activity function for this
                    // protein family parameter
                    entry = new TableEntry(val, rl);
                    if (entries.contains(entry)) {
                        // construct family term once, in case we find matches
                        final Term fpt = new Term(PROTEIN_ABUNDANCE);
                        fpt.addFunctionArgument(new Parameter(ns, val));
                        final Term fat = new Term(f);
                        fat.addFunctionArgument(fpt);

                        // since we found a molecular activity function for a
                        // protein family look up UUIDs for all entries
                        // ... loading uuids on demand to save resources
                        if (eu == null) {
                            eu = findUUIDs(entries, lookups);
                        }

                        // search protein family member parameters for same
                        // molecular activity function
                        final List<Parameter> op = stmt.getObject().getTerm()
                                .getAllParameters();
                        for (final Parameter mp : op) {
                            Namespace mns = mp.getNamespace();
                            String mval = mp.getValue();
                            String mrl = mns.getResourceLocation();

                            // lookup UUID for protein family member
                            JDBMEquivalenceLookup jdbmLookup = lookups.get(mrl);
                            if (jdbmLookup == null) {
                                continue;
                            }
                            SkinnyUUID uuid = jdbmLookup.lookup(mval);

                            // do we have protein family member parameter uuid
                            // in molecular activity function entries?
                            if (eu.contains(uuid)) {
                                // construct member term
                                final Term mpt = new Term(PROTEIN_ABUNDANCE);
                                mpt.addFunctionArgument(new Parameter(mns, mval));
                                final Term mat = new Term(f);
                                mat.addFunctionArgument(mpt);

                                // construct isA statement between member and family
                                isaStmts.add(new Statement(mat, null,
                                        null, new Object(fat), IS_A));
                                inferred++;
                            }
                        }
                    }
                }
            }
        }

        // add isA relationships to protein family document, if necessary
        if (!isaStmts.isEmpty()) {
            final StatementGroup isaStmtGroup = new StatementGroup();
            isaStmtGroup.setStatements(isaStmts);
            d.addStatementGroup(isaStmtGroup);
        }

        pr.setDeltaStatements(inferred);
        pr.setTotalStatements(d.getStatementCount());
        pr.setSuccess(true);

        // Close the indices
        for (final JDBMEquivalenceLookup jl : lookups.values()) {
            try {
                jl.close();
            } catch (IOException e) {
                pr.addWarning(e.getMessage());
            }
        }

        return pr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentModificationResult pruneComplexes(boolean expand,
            final Document complexes,
            final ProtoNetwork network) {

        DocumentModificationResult pr = new DocumentModificationResult();

        // Search for all complex(#) parameters within the network.
        Set<TableEntry> networkLiterals = search(network, NC_LITERAL);

        // Load the equivalences
        Set<EquivalenceDataIndex> equivs;
        try {
            equivs = p2.stage2LoadNamespaceEquivalences();
        } catch (EquivalenceMapResolutionFailure f) {
            // Unrecoverable error
            pr.addError(f.getUserFacingMessage());
            pr.setSuccess(false);
            return pr;
        }

        // Map namespace to lookup
        Map<String, JDBMEquivalenceLookup> lookups =
                sizedHashMap(equivs.size());
        for (final EquivalenceDataIndex edi : equivs) {
            String rl = edi.getNamespaceResourceLocation();
            DataFileIndex dfi = edi.getEquivalenceIndex();
            lookups.put(rl, new JDBMEquivalenceLookup(dfi.getIndexPath()));
        }

        // Open the indices
        for (final JDBMEquivalenceLookup jl : lookups.values()) {
            try {
                jl.open();
            } catch (IOException e) {
                pr.addError(e.getMessage());
                pr.setSuccess(false);
                return pr;
            }
        }

        // It's helpful to reference the contents of the protein families
        // BEL document in understanding this section.

        // Establish set of UUIDs relevant for the document's member parameters
        // (this only matters if the expand flag is set)
        Set<SkinnyUUID> uuids = new HashSet<SkinnyUUID>();
        final List<Statement> complexStmts = complexes.getAllStatements();
        if (expand) {
            uuids = findUUIDs(network, lookups, complexStmts);
        }
        boolean haveUUIDs = !uuids.isEmpty();

        int pruned = 0, total = complexStmts.size();
        List<Parameter> parameters;

        // Pruning is simply a matter of iterating all statements in document...
        COMPLEXES: for (final Statement stmt : complexStmts) {

            // ... all named complex parameters
            parameters = stmt.getSubject().getAllParameters();
            for (final Parameter p : parameters) {
                if (!validParameter(p)) {
                    continue;
                }
                Namespace ns = p.getNamespace();
                String val = p.getValue();
                String rl = ns.getResourceLocation();

                // ... do we have the complex?
                if (networkLiterals.contains(new TableEntry(val, rl))) {
                    continue COMPLEXES;
                }
            }

            Object stmtObj = stmt.getObject();
            if (haveUUIDs && stmtObj != null && stmtObj.getTerm() != null) {
                // ... all complex components
                parameters = stmtObj.getTerm().getAllParameters();
                for (final Parameter p : parameters) {
                    if (!validParameter(p)) {
                        continue;
                    }
                    Namespace ns = p.getNamespace();
                    String val = p.getValue();
                    String rl = ns.getResourceLocation();

                    // ... do we have set membership?
                    JDBMEquivalenceLookup jdbmLookup = lookups.get(rl);
                    if (jdbmLookup != null) {
                        SkinnyUUID uuid = jdbmLookup.lookup(val);
                        // Check for set membership, uuid ∊ uuids
                        if (uuids.contains(uuid)) {

                            // This indicates equivalent parameters are being
                            // used by both document and network. This
                            // statement will remain (i.e., not be pruned).
                            continue COMPLEXES;

                        }
                    }
                }
            }

            // Map statement group to statement set
            Map<StatementGroup, Set<Statement>> groupMap =
                    complexes.mapStatements();

            // Failed to match both either complex or component.
            // Prune the statement.
            Set<Entry<StatementGroup, Set<Statement>>> entries =
                    groupMap.entrySet();
            for (final Entry<StatementGroup, Set<Statement>> entry : entries) {
                StatementGroup group = entry.getKey();
                Set<Statement> stmts = entry.getValue();
                if (entry.getValue().contains(stmt)) {
                    stmts.remove(stmt);
                    group.getStatements().remove(stmt);
                    pruned++;
                    break;
                }
            }
        }

        pr.setDeltaStatements(-(pruned));
        pr.setTotalStatements(total);
        pr.setSuccess(true);

        for (final JDBMEquivalenceLookup jl : lookups.values()) {
            try {
                jl.close();
            } catch (IOException e) {
                pr.addWarning(e.getMessage());
            }
        }

        return pr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentModificationResult pruneGene(final Document genes,
            final ProtoNetwork network) {

        DocumentModificationResult pr = new DocumentModificationResult();

        // Load the equivalences
        Set<EquivalenceDataIndex> equivs;
        try {
            equivs = p2.stage2LoadNamespaceEquivalences();
        } catch (EquivalenceMapResolutionFailure f) {
            // Unrecoverable error
            pr.addError(f.getUserFacingMessage());
            pr.setSuccess(false);
            return pr;
        }

        // Map namespace to lookup
        Map<String, JDBMEquivalenceLookup> lookups =
                sizedHashMap(equivs.size());
        for (final EquivalenceDataIndex edi : equivs) {
            String rl = edi.getNamespaceResourceLocation();
            DataFileIndex dfi = edi.getEquivalenceIndex();
            lookups.put(rl, new JDBMEquivalenceLookup(dfi.getIndexPath()));
        }

        // Open the indices
        for (final JDBMEquivalenceLookup jl : lookups.values()) {
            try {
                jl.open();
            } catch (IOException e) {
                pr.addError(e.getMessage());
                pr.setSuccess(false);
                return pr;
            }
        }

        // It's helpful to reference the contents of the gene scaffolding
        // BEL document in understanding this section.

        // Establish set of UUIDs relevant for the document's member parameters
        Set<SkinnyUUID> uuids = new HashSet<SkinnyUUID>();
        pr.setTotalStatements(genes.getNumberOfStatements());

        for (final Statement stmt : genes) {
            // Add all the parameters
            final List<Parameter> parameters = stmt.getAllParameters();
            for (final Parameter p : parameters) {
                if (!validParameter(p)) {
                    continue;
                }
                Namespace ns = p.getNamespace();
                String val = p.getValue();
                String rl = ns.getResourceLocation();

                JDBMEquivalenceLookup jdbmLookup = lookups.get(rl);
                if (jdbmLookup == null) {
                    continue;
                }

                SkinnyUUID uuid = jdbmLookup.lookup(val);
                if (uuid != null) {
                    uuids.add(uuid);
                }
            }
        }

        // Establish set of UUIDs relevant for the proto-network
        Set<SkinnyUUID> pnUUIDs = new HashSet<SkinnyUUID>();
        ParameterTable paramTbl = network.getParameterTable();
        TableParameter[] paramArr = paramTbl.getTableParameterArray();
        for (final TableParameter tp : paramArr) {
            if (!validParameter(tp)) {
                continue;
            }
            TableNamespace namespace = tp.getNamespace();
            String value = tp.getValue();
            String rl = namespace.getResourceLocation();
            JDBMEquivalenceLookup jdbmLookup = lookups.get(rl);
            if (jdbmLookup == null) {
                continue;
            }

            SkinnyUUID uuid = jdbmLookup.lookup(value);
            if (uuid != null) {
                pnUUIDs.add(uuid);
            }
        }

        // Two sets of UUIDs have been established at this point.
        // The first set, uuids, is the set based on the genes.
        // The second set, pnUUIDs, is the set based on the proto-network.

        // Make uuids the intersection of the two: (uuids ∩ pnUUIDs)
        uuids.retainAll(pnUUIDs);

        final Set<Statement> toPrune = new HashSet<Statement>();
        int pruned = 0;

        // Pruning is simply a matter of iterating all statements in genes...
        GENES: for (final Statement stmt : genes) {
            List<Parameter> parameters = stmt.getAllParameters();

            // ... and all parameters in each statement...
            for (final Parameter p : parameters) {
                if (!validParameter(p)) {
                    continue;
                }

                Namespace ns = p.getNamespace();
                String val = p.getValue();
                String rl = ns.getResourceLocation();

                // ... do we have set membership?
                JDBMEquivalenceLookup jdbmLookup = lookups.get(rl);
                if (jdbmLookup == null) {
                    continue;
                }
                SkinnyUUID uuid = jdbmLookup.lookup(val);
                // Check for set membership, uuid ∊ uuids
                if (uuids.contains(uuid)) {

                    // This indicates equivalent parameters are being
                    // used by both document and network. This
                    // statement will remain (i.e., not be pruned).
                    continue GENES;

                }
            }
            toPrune.add(stmt);
        }

        for (final StatementGroup sg : genes.getStatementGroups()) {
            List<Statement> curstmts = sg.getStatements();
            List<Statement> newstmts = sizedArrayList(curstmts.size());
            for (final Statement stmt : curstmts) {
                if (!toPrune.contains(stmt)) {
                    newstmts.add(stmt);
                } else {
                    pruned++;
                }
            }
            sg.setStatements(newstmts);
        }

        pr.setDeltaStatements(-(pruned));
        pr.setSuccess(true);

        for (final JDBMEquivalenceLookup jl : lookups.values()) {
            try {
                jl.close();
            } catch (IOException e) {
                pr.addWarning(e.getMessage());
            }
        }

        return pr;
    }

    /**
     * Finds the {@link SkinnyUUID} for every {@link Parameter parameter} in
     * the {@link ProtoNetwork proto network} that is also referenced in the
     * {@link Term object term} of each {@link Statement statement}.
     *
     * @param network {@link ProtoNetwork}, the proto network containing
     * {@link Parameter parameters} that we'll get {@link String uuids} for
     * @param lookups {@link Map} of {@link JDBMLookup} keyed by resource
     * location and assumed to be open / ready for lookups
     * @param stmts {@link List} of {@link Statement}, the statements
     * that contain object-term {@link Parameter parameters} that we'll get
     * {@link SkinnyUUID}s for
     * @return the {@link SkinnyUUID} intersection between the statement's
     * object term {@link Parameter parameters} and the proto network's
     * {@link Parameter parameters}
     */
    private Set<SkinnyUUID> findUUIDs(final ProtoNetwork network,
            Map<String, JDBMEquivalenceLookup> lookups,
            final List<Statement> stmts) {
        final Set<SkinnyUUID> uuids = new HashSet<SkinnyUUID>();

        for (final Statement stmt : stmts) {
            // Add all the object's parameters
            Object stmtObj = stmt.getObject();
            if (stmtObj == null || stmtObj.getTerm() == null) {
                continue;
            }
            for (final Parameter p : stmtObj.getTerm()) {
                if (!validParameter(p)) {
                    continue;
                }
                Namespace ns = p.getNamespace();
                String val = p.getValue();
                String rl = ns.getResourceLocation();

                JDBMEquivalenceLookup jdbmLookup = lookups.get(rl);
                if (jdbmLookup == null) {
                    continue;
                }

                SkinnyUUID uuid = jdbmLookup.lookup(val);
                if (uuid != null) {
                    uuids.add(uuid);
                }
            }
        }

        // Establish set of UUIDs relevant for the proto-network
        Set<SkinnyUUID> pnUUIDs = new HashSet<SkinnyUUID>();
        ParameterTable paramTbl = network.getParameterTable();
        TableParameter[] paramArr = paramTbl.getTableParameterArray();
        for (final TableParameter tp : paramArr) {
            if (!validParameter(tp)) {
                continue;
            }
            TableNamespace namespace = tp.getNamespace();
            String value = tp.getValue();
            String rl = namespace.getResourceLocation();
            JDBMEquivalenceLookup jdbmLookup = lookups.get(rl);
            if (jdbmLookup == null) {
                continue;
            }

            SkinnyUUID uuid = jdbmLookup.lookup(value);
            if (uuid != null) {
                pnUUIDs.add(uuid);
            }
        }

        // Two sets of UUIDs have been established at this point.
        // The first set, uuids, is the set based on the statements.
        // The second set, pnUUIDs, is the set based on the proto-network.
        // Make uuids the intersection of the two: (uuids ∩ pnUUIDs)
        uuids.retainAll(pnUUIDs);

        return uuids;
    }

    private Set<SkinnyUUID> findUUIDs(final Set<TableEntry> entries,
            Map<String, JDBMEquivalenceLookup> lookups) {
        final Set<SkinnyUUID> uuids = new HashSet<SkinnyUUID>();

        for (final TableEntry e : entries) {
            String val = e.value;
            String rl = e.resourceLocation;

            JDBMEquivalenceLookup jdbmLookup = lookups.get(rl);
            if (jdbmLookup == null) {
                continue;
            }

            SkinnyUUID uuid = jdbmLookup.lookup(val);
            if (uuid != null) {
                uuids.add(uuid);
            }
        }

        return uuids;
    }

    /**
     * Aggregate the set of all table entries for a proto-network that match at
     * least one of the provided term literals.
     *
     * @param literals Literals, e.g., {@code proteinAbundance(#)}
     * @return Table entry set
     */
    private Set<TableEntry> search(final ProtoNetwork pn, String... literals) {
        Set<TableEntry> ret;
        if (noItems(literals)) {
            return emptySet();
        }

        // An initial capacity would be ideal for this set, but we don't
        // have any reasonable estimations.
        ret = new HashSet<TableEntry>();

        // Establish access to the relevant table objects
        final ParameterTable paramTbl = pn.getParameterTable();
        final TermTable termTbl = pn.getTermTable();
        final List<String> terms = termTbl.getTermValues();
        final NamespaceTable nsTbl = pn.getNamespaceTable();

        final int[][] indata = pn.getTermIndices();

        final Set<String> literalSet = constrainedHashSet(literals.length);
        for (final String s : literals) {
            literalSet.add(s);
        }

        // Search indata for elements of the term literal set
        for (int i = 0; i < indata.length; i++) {
            int tid = indata[i][TERM_INDEX];
            String string = terms.get(tid);
            if (!literalSet.contains(string)) {
                continue;
            }

            int pid = indata[i][PARAM_INDEX];
            final TableParameter tp = paramTbl.getTableParameter(pid);

            final String inval = tp.getValue();
            int nid = indata[i][NAMESPACE_INDEX];

            final TableNamespace tns = nsTbl.getTableNamespace(nid);
            String inrl = null;
            if (tns != null) inrl = tns.getResourceLocation();

            final TableEntry te = new TableEntry(inval, inrl);
            ret.add(te);
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentModificationResult pruneOrthologyDocument(final Document d,
            final ProtoNetwork pn) {

        DocumentModificationResult result = new DocumentModificationResult();

        // Load the equivalences
        Set<EquivalenceDataIndex> equivs;
        try {
            equivs = p2.stage2LoadNamespaceEquivalences();
        } catch (EquivalenceMapResolutionFailure f) {
            // Unrecoverable error
            result.addError(f.getUserFacingMessage());
            result.setSuccess(false);
            return result;
        }

        // Map namespace to lookup
        Map<String, JDBMEquivalenceLookup> lookups =
                sizedHashMap(equivs.size());
        for (final EquivalenceDataIndex edi : equivs) {
            String rl = edi.getNamespaceResourceLocation();
            DataFileIndex dfi = edi.getEquivalenceIndex();
            lookups.put(rl, new JDBMEquivalenceLookup(dfi.getIndexPath()));
        }

        // Open the indices
        for (final JDBMEquivalenceLookup jl : lookups.values()) {
            try {
                jl.open();
            } catch (IOException e) {
                result.addError(e.getMessage());
                result.setSuccess(false);
                return result;
            }
        }

        // Approach
        // 1.   map UUID to list of terms for proto network
        // 2.   iterate ortho statements
        //   a.   if valid orthologous statement
        //      -   lookup exact match of subject term in proto network, if found
        //          continue (orthologous statement intersects the proto network)
        //      -   find subject parameter's uuid in a proto network term, if found
        //          continue (orthologous statement intersects the proto network)
        //      -   lookup exact match of object term in proto network, if found
        //          continue (orthologous statement intersects the proto network)
        //      -   find object parameter's uuid in a proto network term, if found
        //          continue (orthologous statement intersects the proto network)

        // 1. Map parameter UUIDs to containing term ids in the proto network
        final TermTable tt = pn.getTermTable();
        final TermParameterMapTable tpmt = pn.getTermParameterMapTable();
        final ParameterTable pt = pn.getParameterTable();
        final Map<Integer, Integer> pglob = pt.getGlobalIndex();
        final Map<Integer, SkinnyUUID> puuid = pt.getGlobalUUIDs();
        final Set<Integer> tidset = tt.getIndexedTerms().keySet();
        final Map<SkinnyUUID, Set<Integer>> uuidterms = sizedHashMap(puuid
                .size());
        final Set<Term> pnterms = tt.getVisitedTerms().keySet();
        // for each term
        for (final Integer tid : tidset) {
            // get its parameters
            final List<Integer> pids = tpmt.getParameterIndexes(tid);
            for (final Integer pid : pids) {
                // find global parameter index for pid
                Integer globalpid = pglob.get(pid);

                // find UUID for global parameter index
                final SkinnyUUID pu = puuid.get(globalpid);

                // save this term to this UUID
                Set<Integer> terms = uuidterms.get(pu);
                if (terms == null) {
                    terms = new HashSet<Integer>();
                    uuidterms.put(pu, terms);
                }
                terms.add(tid);
            }
        }

        // get all statement in orthology document
        final List<Statement> orthoStmts = d.getAllStatements();
        // map them to statement groups for efficient pruning
        Map<StatementGroup, Set<Statement>> orthomap = d.mapStatements();

        // set up pruning result
        int total = orthoStmts.size();
        int pruned = 0;

        // establish cache to skinny uuids to avoid superfluous jdbm lookups
        final Map<Parameter, SkinnyUUID> paramcache = sizedHashMap(total * 2);

        // iterate all statements in the orthology document
        ORTHO_STATEMENT: for (final Statement orthoStmt : orthoStmts) {

            // rule out invalid or non-orthologous statements
            if (validOrthologousStatement(orthoStmt)) {

                final Term sub = orthoStmt.getSubject();
                final FunctionEnum subf = sub.getFunctionEnum();
                final List<Parameter> subp = sub.getParameters();
                final Parameter subjectParam = subp.get(0);

                // lookup exact match of subject term
                if (pnterms.contains(sub)) {
                    continue;
                }

                // find UUID for subject parameter
                SkinnyUUID uuid = paramcache.get(subjectParam);
                if (uuid == null) {
                    final Namespace ns = subjectParam.getNamespace();
                    final JDBMEquivalenceLookup lookup = lookups.get(ns
                            .getResourceLocation());
                    if (lookup == null) {
                        continue;
                    }

                    uuid = lookup.lookup(subjectParam.getValue());
                    paramcache.put(subjectParam, uuid);
                }

                // if there is a proto network term with this UUID contained, then
                // this orthologous statement intersects the proto network, continue
                if (uuid != null) {
                    Set<Integer> tids = uuidterms.get(uuid);
                    if (hasItems(tids)) {
                        for (final Integer tid : tids) {
                            final Term t = tt.getIndexedTerms().get(tid);
                            if (t.getFunctionEnum() == subf) {
                                continue ORTHO_STATEMENT;
                            }
                        }
                    }
                }

                final Term obj = orthoStmt.getObject().getTerm();
                final FunctionEnum objf = obj.getFunctionEnum();
                final List<Parameter> objp = obj.getParameters();
                final Parameter objectParam = objp.get(0);

                // lookup exact match of object term
                if (pnterms.contains(obj)) {
                    continue;
                }

                // find UUID for object parameter
                uuid = paramcache.get(objectParam);
                if (uuid == null) {
                    final Namespace ns = objectParam.getNamespace();
                    final JDBMEquivalenceLookup lookup = lookups.get(ns
                            .getResourceLocation());
                    if (lookup == null) {
                        continue;
                    }

                    uuid = lookup.lookup(objectParam.getValue());
                    paramcache.put(objectParam, uuid);
                }

                // if there is a proto network term with this UUID contained, then
                // this orthologous statement intersects the proto network, continue
                if (uuid != null) {
                    Set<Integer> tids = uuidterms.get(uuid);
                    if (hasItems(tids)) {
                        for (final Integer tid : tids) {
                            final Term t = tt.getIndexedTerms().get(tid);
                            if (t.getFunctionEnum() == objf) {
                                continue ORTHO_STATEMENT;
                            }
                        }
                    }
                }

                // proto network does not contain either equivalent term so prune
                // from its parent statement group
                Set<Entry<StatementGroup, Set<Statement>>> entries = orthomap
                        .entrySet();
                for (final Entry<StatementGroup, Set<Statement>> e : entries) {
                    StatementGroup group = e.getKey();
                    Set<Statement> stmts = e.getValue();
                    if (stmts.contains(orthoStmt)) {
                        group.getStatements().remove(orthoStmt);
                        pruned++;
                        break;
                    }
                }
            }
        }

        // close equivalences
        for (final JDBMEquivalenceLookup jl : lookups.values()) {
            try {
                jl.close();
            } catch (IOException e) {
                result.addWarning(e.getMessage());
            }
        }

        // set result data
        result.setDeltaStatements(-pruned);
        result.setTotalStatements(total);
        result.setSuccess(true);

        return result;
    }

    /**
     * Determines whether the {@link Statement statement} is a valid
     * {@link RelationshipType#ORTHOLOGOUS orthologous} statement.
     *
     * @param stmt {@link Statement} to evaluate
     * @return {@code true} if it is valid, {@code false} otherwise
     */
    private boolean validOrthologousStatement(final Statement stmt) {
        // test statement is well-formed
        if (stmt.getRelationshipType() == RelationshipType.ORTHOLOGOUS
                && stmt.getObject() != null
                && stmt.getObject().getTerm() != null) {

            // test subject has only one namespace parameter
            final Term subject = stmt.getSubject();
            List<Parameter> subparams = subject.getParameters();
            if (subparams == null || subparams.size() != 1
                    || subparams.get(0).getNamespace() == null) {
                return false;
            }

            // test object has only one namespace parameter
            final Term object = stmt.getObject().getTerm();
            List<Parameter> objparams = object.getParameters();
            if (objparams == null || objparams.size() != 1
                    || objparams.get(0).getNamespace() == null) {
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtoNetwork compile(final Document d) {
        return protoNetworkService.compile(d);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(
            final ProtoNetwork pnDestination,
            final ProtoNetwork pndSource)
            throws ProtoNetworkError {

        protoNetworkService.merge(pnDestination, pndSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtoNetworkDescriptor write(final String path,
            final ProtoNetwork network) throws ProtoNetworkError {
        return protoNetworkService.write(path, network);
    }

    /**
     * Returns true if the parameter has a namespace and value, false if not.
     *
     * @param p Parameter
     */
    private static boolean validParameter(final Parameter p) {
        if (p.getNamespace() == null) {
            return false;
        }
        if (p.getValue() == null) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if the table parameter has a namespace and value, false if
     * not.
     *
     * @param tp Table parameter
     */
    private static boolean validParameter(final TableParameter tp) {
        if (tp.getNamespace() == null) {
            return false;
        }
        if (tp.getValue() == null) {
            return false;
        }
        return true;
    }

    /**
     * Table entry.
     */
    private static class TableEntry {
        /** Parameter value. */
        private final String value;
        /** Namespace containing this parameter value. */
        private final String resourceLocation;
        private final int hash;

        /**
         * Creates a table entry; establishes a hash code.
         *
         * @param val String value
         * @param rl String resource location
         */
        private TableEntry(final String val, final String rl) {
            this.value = val;
            this.resourceLocation = rl;

            int prime = 31;
            int result = prime;

            result *= prime;
            result += value.hashCode();

            if (rl != null) {
                result *= prime;
                result += rl.hashCode();
            }

            hash = result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return hash;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TableEntry)) {
                return false;
            }

            final TableEntry te = (TableEntry) o;
            if (!value.equals(te.value)) {
                return false;
            }

            if (resourceLocation == null) {
                if (te.resourceLocation != null) {
                    return false;
                }
            } else if (!resourceLocation.equals(te.resourceLocation)) {
                return false;
            }

            return true;
        }
    }
}
