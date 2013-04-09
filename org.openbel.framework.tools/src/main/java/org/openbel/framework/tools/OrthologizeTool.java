package org.openbel.framework.tools;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.isNumeric;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.common.enums.RelationshipType.ORTHOLOGOUS;
import static org.openbel.framework.core.StandardOptions.SHORT_OPT_VERBOSE;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.cli.Option;
import org.openbel.framework.api.DefaultOrthologize;
import org.openbel.framework.api.DefaultSpeciesDialect;
import org.openbel.framework.api.KAMStore;
import org.openbel.framework.api.KAMStoreImpl;
import org.openbel.framework.api.Kam;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.Orthologize;
import org.openbel.framework.api.OrthologizedKam;
import org.openbel.framework.api.SpeciesDialect;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.core.CommandLineApplication;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.DatabaseService;
import org.openbel.framework.core.df.DatabaseServiceImpl;

/**
 * OrthologizeTool provides a command line tool to create an orthologized
 * version of a KAM in the KAMStore.  The orthologization primarily requires a
 * KAM and an NCBI Taxonomy Id.
 */
public class OrthologizeTool extends CommandLineApplication {

    // command line app metadata
    private static final String NAME = "";
    private static final String SHORT = "";
    private static final String DESCRIPTION = "";

    // command line options
    private static final String USAGE = "-t <taxonomy id> " +
    		"[-k <new kam name>] " +
    		"[-d <new kam description>] " +
    		"[--no-preserve] " +
    		"<kam name>";
    private static final String TAX_ID = "tax-id";
    private static final String TAX_ID_SHORT = "t";
    private static final String TAX_ID_DESCRIPTION = "a ncbi taxonomy id to orthologize to";

    /**
     * Constructs and execute this tool.
     *
     * @param args {@code String[]} main arguments
     */
    public OrthologizeTool(String[] args) {
        super(args);

        // setup
        reportable();
        printApplicationInfo();
        initializeSystemConfiguration();

        // set cli fields then validate
        String taxIdValue = getOptionValue(TAX_ID);
        List<String> extra = getExtraneousArguments();
        String kamName = extra.isEmpty() ? null : extra.iterator().next();
        if (taxIdValue == null || !isNumeric(taxIdValue)) {
            printUsage();
            String fmt = "%s (-%s, --%s) value is invalid";
            String msg = format(fmt, TAX_ID, TAX_ID_SHORT, TAX_ID);
            reportable.error(msg);
            bail(ExitCode.GENERAL_FAILURE);
            return;
        }
        int taxId = parseInt(taxIdValue);
        if (kamName == null) {
            printUsage();
            String msg = "<kam name> must be provided as the last argument";
            reportable.error(msg);
            bail(ExitCode.GENERAL_FAILURE);
            return;
        }
        if (!kamExists(kamName)) {
            printUsage();
            String fmt = "the kam %s is not in the KAM store";
            String msg = format(fmt, kamName);
            reportable.error(msg);
            bail(ExitCode.GENERAL_FAILURE);
            return;
        }

        // orthologize msg
        if (hasOption(SHORT_OPT_VERBOSE)) {
            String fmt = "Orthologizing kam \"%s\".";
            reportable.output(format(fmt, kamName));
        }

        // run orthologize
        Kam kam = kam(kamName);
        KamInfo info = kam.getKamInfo();
        KAMStore kamstore = kamstore();
        Orthologize ortho = new DefaultOrthologize();
        SpeciesDialect dialect = dialect(taxId, info, kamstore);
        OrthologizedKam orthokam = ortho.orthologize(kam, kamstore, dialect);

        // remap collapsing nodes
        kam = kam(kamName);
        info = kam.getKamInfo();
        Map<KamNode, KamNode> collapsed = orthokam.getCollapsedNodes();
        Set<Entry<KamNode, KamNode>> set = collapsed.entrySet();
        for (Entry<KamNode, KamNode> e : set) {
            KamNode collapsing = e.getKey();
            KamNode collapseTo = e.getValue();

            collapsing = kam.findNode(collapsing.getId());
            collapseTo = kam.findNode(collapseTo.getId());
            kamstore.collapseKamNode(info, collapsing, collapseTo);
        }

        // remove orthologous edges/statements
        kamstore.removeKamEdges(info, ORTHOLOGOUS);

        // coalesce duplicate edges
        kamstore.coalesceKamEdges(info);

        // clean up
        kamstore.teardown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationShortName() {
        return SHORT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationDescription() {
        return DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        return USAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Option> getCommandLineOptions() {
        List<Option> options = sizedArrayList(4);
        options.add(new Option(TAX_ID_SHORT, TAX_ID, true, TAX_ID_DESCRIPTION));
        return options;
    }

    /**
     * Sets the reportable implementation for {@link System#out} and
     * {@link System#err}.
     */
    private void reportable() {
        final SimpleOutput reportable = new SimpleOutput();
        reportable.setErrorStream(System.err);
        reportable.setOutputStream(System.out);
        setReportable(reportable);
    }

    private static boolean kamExists(String kam) {
        DatabaseService db = new DatabaseServiceImpl();
        SystemConfiguration sc = getSystemConfiguration();

        DBConnection c = null;
        try {
            c = db.dbConnection(sc.getKamURL(), sc.getKamUser(),
                    sc.getKamPassword());
            KAMStore store = new KAMStoreImpl(c);
            return store.getKamInfo(kam) != null;
        } catch (SQLException e) {
            return false;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private static Kam kam(String kam) {
        DatabaseService db = new DatabaseServiceImpl();
        SystemConfiguration sc = getSystemConfiguration();

        DBConnection c = null;
        try {
            c = db.dbConnection(sc.getKamURL(), sc.getKamUser(),
                    sc.getKamPassword());
            KAMStore store = new KAMStoreImpl(c);
            return store.getKam(kam);
        } catch (SQLException e) {
            return null;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private static KAMStore kamstore() {
        DatabaseService db = new DatabaseServiceImpl();
        SystemConfiguration sc = getSystemConfiguration();

        DBConnection c = null;
        try {
            c = db.dbConnection(sc.getKamURL(), sc.getKamUser(),
                    sc.getKamPassword());
            return new KAMStoreImpl(c);
        } catch (SQLException e) {
            return null;
        }
    }

    private static SpeciesDialect dialect(int taxId, KamInfo kam,
            KAMStore kamStore) {
        return new DefaultSpeciesDialect(kam, kamStore, taxId, true);
    }

    /**
     * Main entry point for this tool.
     *
     * @param args {@code String[]} main arguments
     */
    public static void main(String[] args) {
        OrthologizeTool tool = new OrthologizeTool(args);
        tool.end();
    }
}
