/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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
package org.openbel.framework.tools;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.PathConstants.PROTO_NETWORK_FILENAME;
import static org.openbel.framework.common.Strings.NOT_A_PHASE1_DIR;
import static org.openbel.framework.common.Strings.NO_PROTO_NETWORKS;
import static org.openbel.framework.common.Strings.PHASE2_STAGE1_HDR;
import static org.openbel.framework.common.Strings.PHASE2_STAGE2_HDR;
import static org.openbel.framework.common.Strings.PHASE2_STAGE3_HDR;
import static org.openbel.framework.common.Strings.PHASE2_STAGE4_HDR;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.tools.PhaseTwoOptions.phaseTwoOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.cli.Option;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.common.model.EquivalenceDataIndex;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.common.util.BELPathFilters.ProtonetworkFilter;
import org.openbel.framework.compiler.DefaultPhaseTwo;
import org.openbel.framework.compiler.PhaseTwoImpl;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.openbel.framework.core.equivalence.EquivalenceIndexerService;
import org.openbel.framework.core.equivalence.EquivalenceIndexerServiceImpl;
import org.openbel.framework.core.equivalence.EquivalenceMapResolutionFailure;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protonetwork.BinaryProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.ProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.ProtoNetworkService;
import org.openbel.framework.core.protonetwork.ProtoNetworkServiceImpl;
import org.openbel.framework.core.protonetwork.TextProtoNetworkExternalizer;

/**
 * BEL phase two compiler.
 */
public final class PhaseTwoApplication extends PhaseApplication {
    private final DefaultPhaseTwo p2;
    private final CacheableResourceService cache;
    private final SystemConfiguration syscfg;

    /** Phase two artifact directory. */
    public final static String DIR_ARTIFACT = "phaseII";

    private final static String NUM_PHASES = "4";

    /**
     * Phase two application constructor.
     *
     * @param args Command-line arguments
     */
    public PhaseTwoApplication(String[] args) {
        super(args);

        syscfg = getSystemConfiguration();
        cache = new DefaultCacheableResourceService();

        final ProtoNetworkService protonetService =
                new ProtoNetworkServiceImpl();
        final EquivalenceIndexerService indexer =
                new EquivalenceIndexerServiceImpl();
        final PhaseTwoImpl phaseTwo =
                new PhaseTwoImpl(cache, indexer, protonetService);
        phaseTwo.setReportable(getReportable());

        p2 = phaseTwo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        super.start();

        // load the resource index for phase II use.
        String resIdxURL = syscfg.getResourceIndexURL();
        try {
            final ResolvedResource resolvedResource = cache.resolveResource(
                    ResourceType.RESOURCE_INDEX, resIdxURL);
            ResourceIndex.INSTANCE.loadIndex(resolvedResource
                    .getCacheResourceCopy());
        } catch (ResourceDownloadError e) {
            stageError(e.getUserFacingMessage());
            ResourceIndex.INSTANCE.loadIndex();
        } catch (FileNotFoundException e) {
            defaultIndex();
        } catch (XMLStreamException e) {
            defaultIndex();
        }

        processOutputDirectory();
    }

    private void defaultIndex() {
        stageError("Could not read resource index file.  Equivalencing will be "
                + "disabled.");
        ResourceIndex.INSTANCE.loadIndex();
    }

    /**
     * Processes the output directory.
     */
    private void processOutputDirectory() {
        final String root = outputDirectory.getAbsolutePath();
        final String leaf = PhaseOneApplication.DIR_ARTIFACT;
        final String path = asPath(root, leaf);
        final File phaseIPath = new File(path);

        // Fail if the working path doesn't contain a phase I artifact
        if (!phaseIPath.isDirectory()) {
            error(NOT_A_PHASE1_DIR + ": " + phaseIPath);
            failUsage();
        }

        // Fail if the working path doesn't contain any proto-networks
        final File[] networks = phaseIPath.listFiles(new ProtonetworkFilter());
        if (networks.length == 0) {
            error(NO_PROTO_NETWORKS + " found in " + phaseIPath);
            failUsage();
        }

        // Create the directory artifact or fail
        artifactPath = createDirectoryArtifact(outputDirectory, DIR_ARTIFACT);

        processDirectories(networks);
    }

    /**
     * Starts phase two compilation of proto-networks.
     *
     * @param networks
     */
    private void processDirectories(final File[] networks) {
        phaseOutput(format("=== %s ===", getApplicationName()));

        ProtoNetwork network = stage1(networks);
        Set<EquivalenceDataIndex> eqs = stage2();

        if (!eqs.isEmpty()) {
            network = stage3(network, eqs);
        }
        stage4(network);
    }

    /**
     * Stage one merger of networks, returning the merged proto-network.
     *
     * @param networks Proto-networks
     * @return {@link ProtoNetwork}
     */
    private ProtoNetwork stage1(final File[] networks) {
        beginStage(PHASE2_STAGE1_HDR, "1", NUM_PHASES);
        final int netct = networks.length;

        final StringBuilder bldr = new StringBuilder();
        bldr.append("Merging ");
        bldr.append(netct);
        bldr.append(" network");
        if (netct > 1) {
            bldr.append("s");
        }
        stageOutput(bldr.toString());

        long t1 = currentTimeMillis();
        Collection<ProtoNetworkDescriptor> nds = sizedArrayList(netct);
        for (final File network : networks) {
            final String root = network.getAbsolutePath();
            final String netPath = asPath(root, PROTO_NETWORK_FILENAME);
            final File networkBin = new File(netPath);
            nds.add(new BinaryProtoNetworkDescriptor(networkBin));
        }

        ProtoNetwork ret = p2.stage1Merger(nds);

        new File(artifactPath.getAbsolutePath() + "/merged").mkdirs();

        p2.stage4WriteEquivalentProtoNetwork(ret,
                artifactPath.getAbsolutePath() + "/merged");

        if (withDebug()) {
            try {
                TextProtoNetworkExternalizer textExternalizer =
                        new TextProtoNetworkExternalizer();
                textExternalizer.writeProtoNetwork(ret,
                        artifactPath.getAbsolutePath() + "/merged");
            } catch (ProtoNetworkError e) {
                error("Could not write out equivalenced proto network.");
            }
        }

        long t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());
        return ret;
    }

    /**
     * Stage two equivalence loading, returning a set of data indices.
     *
     * @param equivalenceFile Equivalence file
     * @return Set of equivalence data indices
     */
    private Set<EquivalenceDataIndex> stage2() {
        beginStage(PHASE2_STAGE2_HDR, "2", NUM_PHASES);

        Set<EquivalenceDataIndex> ret = new HashSet<EquivalenceDataIndex>();

        final StringBuilder bldr = new StringBuilder();
        bldr.append("Loading namespace equivalences from resource index");
        stageOutput(bldr.toString());

        long t1 = currentTimeMillis();
        try {
            ret.addAll(p2.stage2LoadNamespaceEquivalences());
        } catch (EquivalenceMapResolutionFailure e) {
            warning(e.getUserFacingMessage());
            // continue with an empty equivalence data index set
        }

        for (final EquivalenceDataIndex edi : ret) {
            final String nsLocation = edi.getNamespaceResourceLocation();
            bldr.setLength(0);
            bldr.append("Equivalence for ");
            bldr.append(nsLocation);
            stageOutput(bldr.toString());
        }
        long t2 = currentTimeMillis();
        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());
        return ret;
    }

    /**
     * Stage three equivalencing, returning the equivalenced proto-network.
     * If parameter equivalencing yields a {@code 0} count then term and
     * statement equivalencing is not run.
     *
     * @param network Merged proto-network
     * @param equivalences Equivalence data indices
     * @return {@link ProtoNetwork}
     */
    private ProtoNetwork stage3(final ProtoNetwork network,
            Set<EquivalenceDataIndex> equivalences) {
        beginStage(PHASE2_STAGE3_HDR, "3", NUM_PHASES);
        final StringBuilder bldr = new StringBuilder();
        long t1 = currentTimeMillis();

        int pct = stage3Parameter(network, equivalences, bldr);
        stage3Term(network, pct);
        stage3Statement(network, pct);

        long t2 = currentTimeMillis();

        final int paramct =
                network.getParameterTable().getTableParameters().size();
        final int termct = network.getTermTable().getTermValues().size();
        final int stmtct = network.getStatementTable().getStatements().size();

        bldr.setLength(0);
        bldr.append(stmtct);
        bldr.append(" statements, ");
        bldr.append(termct);
        bldr.append(" terms, ");
        bldr.append(paramct);
        bldr.append(" parameters");
        stageOutput(bldr.toString());

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());
        return network;
    }

    /**
     * Stage three parameter equivalencing.
     *
     * @param network the {@link ProtoNetwork network} to equivalence
     * @param equivalences the {@link Set set} of {@link EquivalenceDataIndex}
     * @param bldr the {@link StringBuilder}
     * @return the {@code int} count of parameter equivalences
     */
    private int stage3Parameter(final ProtoNetwork network,
            Set<EquivalenceDataIndex> equivalences, final StringBuilder bldr) {
        bldr.append("Equivalencing parameters");
        stageOutput(bldr.toString());
        ProtoNetwork ret = network;
        int ct = 0;
        try {
            ct = p2.stage3EquivalenceParameters(ret, equivalences);
            stageOutput("(" + ct + " equivalences)");
        } catch (IOException ioex) {
            final String err = ioex.getMessage();
            fatal(err);
        }

        return ct;
    }

    /**
     * Stage three term equivalencing.
     *
     * @param network the {@link ProtoNetwork network} to equivalence
     * @param pct the parameter equivalencing count to control output
     */
    private void stage3Term(final ProtoNetwork network, int pct) {
        stageOutput("Equivalencing terms");
        int tct = p2.stage3EquivalenceTerms(network);
        stageOutput("(" + tct + " equivalences)");
    }

    /**
     * Stage three statement equivalencing.
     *
     * @param network the {@link ProtoNetwork network} to equivalence
     * @param pct the parameter equivalencing count to control output
     */
    private void stage3Statement(final ProtoNetwork network, int pct) {
        stageOutput("Equivalencing statements");
        int sct = p2.stage3EquivalenceStatements(network);
        stageOutput("(" + sct + " equivalences)");
    }

    /**
     * Stage four saving
     *
     * @param eqNetwork
     * @return
     */
    private ProtoNetworkDescriptor stage4(final ProtoNetwork eqNetwork) {
        beginStage(PHASE2_STAGE4_HDR, "4", NUM_PHASES);
        final StringBuilder bldr = new StringBuilder();
        bldr.append("Saving network");
        stageOutput(bldr.toString());

        long t1 = currentTimeMillis();
        ProtoNetworkDescriptor ret =
                p2.stage4WriteEquivalentProtoNetwork(eqNetwork,
                        artifactPath.getAbsolutePath());

        if (withDebug()) {
            try {
                TextProtoNetworkExternalizer textExternalizer =
                        new TextProtoNetworkExternalizer();
                textExternalizer.writeProtoNetwork(eqNetwork,
                        artifactPath.getAbsolutePath());
            } catch (ProtoNetworkError e) {
                error("Could not write out equivalenced proto network.");
            }
        }

        bldr.setLength(0);
        long t2 = currentTimeMillis();
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhaseTwoOptions getPhaseConfiguration() {
        return phaseTwoOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validCommandLine() {
        // We only need output from phase one. Any command-line is valid.
        return true;
    }

    /**
     * Returns {@code "Phase II: Merging proto-networks"}.
     *
     * @return String
     */
    @Override
    public String getApplicationName() {
        return "Phase II: Merging proto-networks";
    }

    /**
     * Returns {@code "Phase II"}.
     *
     * @return String
     */
    @Override
    public String getApplicationShortName() {
        return "Phase II";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Option> getCommandLineOptions() {
        return super.getCommandLineOptions();
    }

    /**
     * Returns the application's description.
     *
     * @return String
     */
    @Override
    public String getApplicationDescription() {
        final StringBuilder bldr = new StringBuilder();
        bldr.append("Merges proto-networks into a composite network and ");
        bldr.append("equivalences term references across namespaces.");
        return bldr.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("[OPTION]...");
        return bldr.toString();
    }

    /**
     * Invokes {@link #harness(PhaseApplication)} for
     * {@link PhaseTwoApplication}.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        harness(new PhaseTwoApplication(args));
    }

    /**
     * {@inheritDoc}
     */
    public static String getRequiredArguments() {
        // Nothing is required
        return "";
    }
}
