package org.openbel.framework.api;

import java.util.List;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.enums.CitationType;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.internal.KAMCatalogDao.AnnotationFilter;
import org.openbel.framework.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KAMCatalogDao.NamespaceFilter;
import org.openbel.framework.internal.KAMStoreDaoImpl.AnnotationType;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelDocumentInfo;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelStatement;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.internal.KAMStoreDaoImpl.Citation;
import org.openbel.framework.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.internal.KAMStoreDaoImpl.TermParameter;

public class MockKamStore implements KamStore {

    @Override
    public void close(Kam k) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void teardown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<KamInfo> readCatalog() {
        throw new UnsupportedOperationException();
    }

    @Override
    public KamInfo getKamInfo(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Kam getKam(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AnnotationType> getAnnotationTypes(Kam k)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AnnotationType> getAnnotationTypes(KamInfo info)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getAnnotationTypeDomainValues(KamInfo info,
            AnnotationType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BelDocumentInfo> getBelDocumentInfos(Kam k) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BelDocumentInfo> getBelDocumentInfos(KamInfo info) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Namespace getNamespace(Kam k, String resourceLocation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Namespace> getNamespaces(Kam k) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Namespace> getNamespaces(KamInfo info) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BelStatement> getSupportingEvidence(KamEdge edge,
            AnnotationFilter fltr) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BelStatement> getSupportingEvidence(KamEdge edge) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BelTerm> getSupportingTerms(KamNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BelTerm> getSupportingTerms(
            KamNode node, boolean removeDups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BelTerm> getSupportingTerms(KamNode node,
            boolean removeDups, NamespaceFilter fltr) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TermParameter> getTermParameters(
            KamInfo info, BelTerm term) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KamNode getKamNode(Kam k, String termString) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KamNode getKamNode(Kam k, BelTerm term) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<KamNode> getKamNodes(Kam k, FunctionEnum function,
            Namespace ns, String paramValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<KamNode> getKamNodes(
            Kam k, Namespace ns, String paramValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<KamNode> getKamNodes(Kam k, KamNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<KamNode> getKamNodes(Kam k, SkinnyUUID uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<KamNode> getKamNodes(Kam k, FunctionEnum function,
            SkinnyUUID uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Citation> getCitations(KamInfo kaminfo,
            BelDocumentInfo docinfo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Citation> getCitations(KamInfo kaminfo,
            BelDocumentInfo docinfo, CitationType citation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Citation> getCitations(KamInfo info, CitationType citation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Citation> getCitations(KamInfo info, CitationType citation,
            String... refIDs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Kam getKam(KamInfo info, KamFilter fltr) throws KamStoreException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Kam getKam(KamInfo info) throws KamStoreException {
        throw new UnsupportedOperationException();
    }
}
