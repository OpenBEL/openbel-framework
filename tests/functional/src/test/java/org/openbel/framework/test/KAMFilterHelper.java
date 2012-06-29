package org.openbel.framework.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openbel.framework.api.AnnotationFilterCriteria;
import org.openbel.framework.api.BelDocumentFilterCriteria;
import org.openbel.framework.api.CitationFilterCriteria;
import org.openbel.framework.api.FilterCriteria;
import org.openbel.framework.api.RelationshipTypeFilterCriteria;
import org.openbel.framework.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.internal.KAMStoreDao;
import org.openbel.framework.internal.KAMStoreDaoImpl.Annotation;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelDocumentInfo;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelStatement;
import org.openbel.framework.internal.KAMStoreDaoImpl.Citation;
import org.openbel.framework.internal.KAMStoreDaoImpl.KamProtoEdge;
import org.openbel.framework.internal.KAMStoreDaoImpl.KamProtoNode;


/**
 * KAM Filtering Helper
 *
 */
public class KAMFilterHelper {

    private KAMStoreDao kamStoreDao; // for supporting evidence lookup

    public KAMFilterHelper(KAMStoreDao kamStoreDao) {
        this.kamStoreDao = kamStoreDao;
    }

    /**
     * @param kamProtoEdgeMap
     * @param kamFilter
     */
    public void filter(Map<Integer, KamProtoNode> kamProtoNodeMap, Map<Integer, KamProtoEdge> kamProtoEdgeMap, KamFilter kamFilter) throws SQLException {
        List<FilterCriteria> criteria = kamFilter.getFilterCriteria();

        for (FilterCriteria criterion : criteria) {
            //process each criteria, number of edges to filter may go down in each loop
            Set<Integer> matched = null;
            if (criterion instanceof RelationshipTypeFilterCriteria) {
                matched = processRelationshipTypeFilterCriteria(kamProtoEdgeMap, (RelationshipTypeFilterCriteria) criterion);
            } else if (criterion instanceof BelDocumentFilterCriteria) {
                matched = processBelDocumentFilterCriteria(kamProtoEdgeMap, (BelDocumentFilterCriteria) criterion);
            } else if (criterion instanceof AnnotationFilterCriteria) {
                matched = processAnnotationFilterCriteria(kamProtoEdgeMap, (AnnotationFilterCriteria) criterion);
            } else if (criterion instanceof CitationFilterCriteria) {
                matched = processCitationFilterCriteria(kamProtoEdgeMap, (CitationFilterCriteria) criterion);
            }
            if( matched != null ) {
                if (criterion.isInclude()) {
                    kamProtoEdgeMap.keySet().retainAll(matched);
                } else {
                    // must be exclude
                    kamProtoEdgeMap.keySet().removeAll(matched);
                }
            }
        }

        //post processing to remove orphaned nodes
        removeOrphanNodes(kamProtoNodeMap, kamProtoEdgeMap);
    }

    /**
     * remove all nodes from the proto node map that is not in at least one edge in the proto edge map
     * @param kamProtoNodeMap
     * @param kamProtoEdgeMap
     */
    protected void removeOrphanNodes(Map<Integer, KamProtoNode> kamProtoNodeMap, Map<Integer, KamProtoEdge> kamProtoEdgeMap) {
        Set<Integer> nodesInUse = new HashSet<Integer>();
        for (Entry<Integer, KamProtoEdge> entry : kamProtoEdgeMap.entrySet()) {
            KamProtoEdge edge = entry.getValue();
            nodesInUse.add(edge.getSourceNode().getId());
            nodesInUse.add(edge.getTargetNode().getId());
        }
        kamProtoNodeMap.keySet().retainAll(nodesInUse);
    }

    protected Set<Integer> processCitationFilterCriteria(Map<Integer, KamProtoEdge> kamProtoEdgeMap, CitationFilterCriteria c) throws SQLException {
        Set<Integer> matched = new HashSet<Integer>();
        Set<Citation> citations = c.getValues();
        Set<String> referenceIds = new HashSet<String>();
        for (Citation citation : citations) {
            referenceIds.add(citation.getId());
        }
        // statement level filter: edge is removed if no statement supports it
        for (Entry<Integer, KamProtoEdge> entry : kamProtoEdgeMap.entrySet()) {
            List<BelStatement> statements = kamStoreDao.getSupportingEvidence(entry.getValue().getId());
            boolean hasSupport = false;
            if( c.isInclude() ) {
                for (BelStatement statement : statements) {
                    if (referenceIds.contains(statement.getCitation().getId())) {
                        hasSupport = true;
                        break;
                    }
                }
                if( hasSupport ) {
                    matched.add(entry.getKey());
                }
            } else {
                for (BelStatement statement : statements) {
                    //try to find at least one statement not in the exclude set
                    if ( ! referenceIds.contains(statement.getCitation().getId())) {
                        hasSupport = true;
                        break;
                    }
                }
                if( !hasSupport ) {
                    matched.add(entry.getKey());
                }
            }

        }
        return matched;
    }



    /**
     * If filter type is include, return list of edges to be included.
     * If filter type is exclude, return list of edges to be excluded.
     *
     * @param kamProtoEdgeMap
     * @param c
     * @return
     * @throws SQLException
     */
    protected Set<Integer> processAnnotationFilterCriteria(Map<Integer, KamProtoEdge> kamProtoEdgeMap, AnnotationFilterCriteria c) throws SQLException {
        Set<Integer> matched = new HashSet<Integer>();
        // annotation level filter: supported statement is remove if no other annotation of the same type exists

        for (Entry<Integer, KamProtoEdge> entry : kamProtoEdgeMap.entrySet()) {
            List<BelStatement> statements = kamStoreDao.getSupportingEvidence(entry.getValue().getId());
            boolean hasStatementSupport = false;
            for (BelStatement statement : statements) {
                if( isStatementSupported(statement.getAnnotationList(), c) ) {
                    hasStatementSupport = true;
                    break;
                }
            }
            if( c.isInclude() ) {
                if( hasStatementSupport ) {
                    matched.add(entry.getKey());
                }
            } else {
                if( !hasStatementSupport ) {
                    //added the edge to list of edges to be removed
                    matched.add(entry.getKey());
                }
            }
        }



        return matched;
    }

    /**
     *     Statement is considered supported according to the following:
     *      if filter type is include: if it has at least one annotation matching the specified values
     *      if filter type is exclude: if it has at least one annotation not match the specified values, or
     *                            if it has no annotations of the specified annotation type
     * @param statementAnnotations
     * @param c
     * @return
     */
    private boolean isStatementSupported(List<Annotation> statementAnnotations, AnnotationFilterCriteria c) {


        boolean isStatementSupported = false;
        List<Annotation> matchAnnotationsSubset = new ArrayList<Annotation>();
        for(Annotation annotation : statementAnnotations) {
            //identify annotations relevant to our filtering criterion
            if (c.getAnnotationType().getId().equals(annotation.getAnnotationType().getId())) {
                matchAnnotationsSubset.add(annotation);
            }
        }

        if( matchAnnotationsSubset.size() > 0 ) {
            if( c.isInclude() ) {
                for(Annotation annotation : matchAnnotationsSubset) {
                    if (c.getValues().contains(annotation.getValue())) {
                        //at least one annotation matched the specified values, statement is supported
                        isStatementSupported = true;
                        break;
                    }
                }
            } else {
                for(Annotation annotation : matchAnnotationsSubset) {
                    if (!c.getValues().contains(annotation.getValue())) {
                        //at least one annotation doesn't match the specified values to exclude, statement is supported
                        isStatementSupported = true;
                        break;
                    }
                }
            }
        } else {
            if( c.isInclude() ) {
                isStatementSupported = false; //include filter, no match annotation type on statement
            } else {
                isStatementSupported = true;
            }
        }
        return isStatementSupported;
    }

    /**
     * @param kamProtoEdgeMap
     * @param c
     * @return
     * @throws SQLException
     */
    protected Set<Integer> processBelDocumentFilterCriteria(Map<Integer, KamProtoEdge> kamProtoEdgeMap, BelDocumentFilterCriteria c) throws SQLException {
        Set<Integer> matched = new HashSet<Integer>();
        Set<BelDocumentInfo> docInfos = c.getValues();
        Set<Integer> docInfoIds = new HashSet<Integer>();
        for (BelDocumentInfo info : docInfos) {
            docInfoIds.add(info.getId());
        }
        // statement level filter: edge is removed if no statement supports it
        for (Entry<Integer, KamProtoEdge> entry : kamProtoEdgeMap.entrySet()) {
            List<BelStatement> statements = kamStoreDao.getSupportingEvidence(entry.getValue().getId());
            boolean hasSupport = false;
            if( c.isInclude() ) {
                for (BelStatement statement : statements) {
                    if (docInfoIds.contains(statement.getBelDocumentInfo().getId())) {
                        hasSupport = true;
                        break;
                    }
                }
                if( hasSupport ) {
                    matched.add(entry.getKey());
                }
            } else {
                for (BelStatement statement : statements) {
                    //try to find at least one statement not in the exclude set
                    if ( ! docInfoIds.contains(statement.getBelDocumentInfo().getId())) {
                        hasSupport = true;
                        break;
                    }
                }
                if( !hasSupport ) {
                    matched.add(entry.getKey());
                }
            }

        }
        return matched;
    }

    /**
     * @param kamProtoEdgeMap
     * @param c
     * @return
     */
    protected Set<Integer> processRelationshipTypeFilterCriteria(Map<Integer, KamProtoEdge> kamProtoEdgeMap, RelationshipTypeFilterCriteria c) {
        Set<Integer> matched = new HashSet<Integer>();
        // edge level filtering: filter based on type match
        for (Entry<Integer, KamProtoEdge> entry : kamProtoEdgeMap.entrySet()) {
            if (c.getValues().contains(entry.getValue().getRelationship())) {
                matched.add(entry.getKey());
            }
        }
        return matched;
    }
}
