package org.openbel.framework.tools;

import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.util.List;

import org.apache.commons.cli.Option;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.core.CommandLineApplication;

/**
 * Orthologize provides a command line tool to create an orthologized version
 * of a KAM in the KAMStore.  The orthologization primarily requires a KAM and
 * an NCBI Taxonomy Id.
 */
public class Orthologize extends CommandLineApplication {
    
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
    private static final String NEW_KAM = "new-kam-name";
    private static final String NEW_KAM_SHORT = "k";
    private static final String NEW_KAM_DESCRIPTION = "the new kam name";
    private static final String NEW_KAM_DESC = "new-kam-description";
    private static final String NEW_KAM_DESC_SHORT = "d";
    private static final String NEW_KAM_DESC_DESCRIPTION = "the new kam description";

    /**
     * Constructs and execute this tool.
     * 
     * @param args {@code String[]} main arguments
     */
    public Orthologize(String[] args) {
        super(args);
        
        reportable();
        printApplicationInfo();
        initializeSystemConfiguration();
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
        options.add(new Option(NEW_KAM_SHORT, NEW_KAM, true, NEW_KAM_DESCRIPTION));
        options.add(new Option(NEW_KAM_DESC_SHORT, NEW_KAM_DESC, true, NEW_KAM_DESC_DESCRIPTION));
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
    
    /**
     * Main entry point for this tool.
     * 
     * @param args {@code String[]} main arguments
     */
    public static void main(String[] args) {
        Orthologize tool = new Orthologize(args);
        tool.end();
    }
}
