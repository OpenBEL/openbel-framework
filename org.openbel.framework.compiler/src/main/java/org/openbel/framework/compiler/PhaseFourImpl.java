package org.openbel.framework.compiler;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.nulls;
import static org.openbel.framework.common.BELUtilities.sizedHashMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.StatementGroup;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.protonetwork.model.ParameterTable;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.common.protonetwork.model.TermParameterMapTable;
import org.openbel.framework.common.protonetwork.model.TermTable;
import org.openbel.framework.compiler.DefaultPhaseThree.DocumentModificationResult;
import org.openbel.framework.core.compiler.expansion.ExpansionService;
import org.openbel.framework.core.indexer.JDBMEquivalenceLookup;
import org.openbel.framework.core.protonetwork.ProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.ProtoNetworkService;

/**
 * {@link PhaseFourImpl} implements the BEL compiler phase four.
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class PhaseFourImpl implements DefaultPhaseFour {

    private ProtoNetworkService protoNetworkService;
    private ExpansionService expansionService;

    /**
     * Constructs {@link PhaseFourImpl}.
     *
     * @param protoNetworkService {@link ProtoNetworkService} used to process
     * the compiled {@link ProtoNetwork}, which cannot be {@code null}
     * @param expansionService {@link ExpansionService} used to expand the
     * {@link Term terms} and {@link Statement statements} of the
     * {@link ProtoNetwork proto network}, which cannot be {@code null}
     * @throws InvalidArgument Thrown if {@code protoNetworkService} is
     * {@code null}
     */
    public PhaseFourImpl(final ProtoNetworkService protoNetworkService,
            final ExpansionService expansionService) {
        if (nulls(protoNetworkService, expansionService)) {
            throw new InvalidArgument("null parameter(s)");
        }

        this.protoNetworkService = protoNetworkService;
        this.expansionService = expansionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentModificationResult pruneOrthologyDocument(final Document d,
            final ProtoNetwork pn,
            final Map<String, JDBMEquivalenceLookup> lookups) {

        // Approach
        // 1.   map UUID to list of terms for proto network
        // 2.   iterate ortho statements
        //   a.   if valid orthologous statement
        //      -   find subject parameter's uuid in a proto network term, if found
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
        final DocumentModificationResult result = new DocumentModificationResult();
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

        // set result data
        result.setDeltaStatements(-pruned);
        result.setTotalStatements(total);
        result.setSuccess(true);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtoNetwork compile(final Document d) {
        // compile
        ProtoNetwork pn = protoNetworkService.compile(d);

        // expand statements to include reciprical orthologous edges
        // XXX General statement expansion will process nested statements again!
        // XXX Users should not be modelling nested statements in orthology BEL
        expansionService.expandStatements(d, pn, true);

        return pn;
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
    public ProtoNetworkDescriptor write(final String path, final ProtoNetwork pn)
            throws ProtoNetworkError {
        return protoNetworkService.write(path, pn);
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
}
