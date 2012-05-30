package org.openbel.framework.compiler;

import java.util.Map;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.compiler.DefaultPhaseThree.DocumentModificationResult;
import org.openbel.framework.core.indexer.JDBMEquivalenceLookup;
import org.openbel.framework.core.protonetwork.ProtoNetworkDescriptor;

/**
 * {@link DefaultPhaseFour} defines the BEL compiler phase four interface.
 * <p>
 * Phase four consists of the following stages and is meant to run sequentially
 * for the number of orthologous BEL documents:
 * <ol>
 * <li>Prunes orthologous BEL document based on {@link ProtoNetwork proto network}</li>
 * <li>Compiles orthologous BEL document into a {@link ProtoNetwork proto network}</li>
 * <li>Merges orthologous {@link ProtoNetwork proto network} into
 * {@link ProtoNetwork proto network}</li>
 * <li>Save orthologized {@link ProtoNetwork proto network}</li>
 * </ol>
 * </p>
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public interface DefaultPhaseFour {

    /**
     * Stage 1 pruning of orthology BEL {@link Document document} by:
     * <p>
     * Prune the {@link Document document}, relative to the
     * {@link ProtoNetwork proto network} being compiled, using equivalencing
     * to match up BEL terms.
     * </p>
     *
     * @param d {@link Document} the orthology BEL document, which cannot be
     * {@code null}
     * @param pn {@link ProtoNetwork} the compiled proto network, which cannot
     * be {@code null}
     * @param lookups {@link Map} of {@link String resource location} to
     * {@link JDBMEquivalenceLookup equivalence lookup}, which cannot be
     * {@code null}
     * @return {@link DocumentModificationResult} containing the pruning
     * results
     * @throws InvalidArgument Thrown if {@code d} or {@code pn} is
     * {@code null}
     */
    public DocumentModificationResult pruneOrthologyDocument(final Document d,
            final ProtoNetwork pn,
            final Map<String, JDBMEquivalenceLookup> lookups);

    /**
     * Compiles {@link Document BEL common documents} to {@link ProtoNetwork
     * proto-networks}.
     *
     * @param document {@link Document} the document to compile
     * @return {@link ProtoNetwork} the compiled proto network
     */
    public ProtoNetwork compile(final Document d);

    /**
     * Deserialize and merge the proto network in <tt>protoNetworkSource</tt>
     * into the proto network in <tt>protoNetworkSource</tt>.
     *
     * @param destination {@link ProtoNetwork}, the proto-network containing the
     * merged results
     * @param source {@link ProtoNetwork}, the proto-network to merge into
     * {@code destination}
     * @throws ProtoNetworkError Thrown if an error occurred reading the proto-
     * network from the {@code source} descriptor.
     */
    public void merge(final ProtoNetwork destination, final ProtoNetwork source)
            throws ProtoNetworkError;

    /**
     * Stage 3 writing of the merged and equivalenced
     * {@link ProtoNetwork proto network} that now included orthology data.
     *
     * @param path {@link String} the path to write the
     * {@link ProtoNetwork proto network} to, which cannot be {@code null}
     * @param pn {@link ProtoNetwork} the merged and equivalenced network that
     * contains orthologous data
     * @return {@link ProtoNetworkDescriptor} providing access to the written
     * proto network
     * @throws InvalidArgument Thrown if {@code path} or {@code pn} is
     * {@code null}
     */
    public ProtoNetworkDescriptor write(final String path,
            final ProtoNetwork pn) throws ProtoNetworkError;
}
