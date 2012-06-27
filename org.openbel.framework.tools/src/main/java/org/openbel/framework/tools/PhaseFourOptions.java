package org.openbel.framework.tools;

import static java.lang.System.out;
import static org.openbel.framework.common.Strings.RC_READ_FAILURE;

import java.io.IOException;

import org.openbel.framework.common.BELRuntimeException;
import org.openbel.framework.common.cfg.RuntimeConfiguration;
import org.openbel.framework.common.enums.ExitCode;

/**
 * Phase four compiler options.
 * <p>
 * This is a {@link #phaseFourOptions() singleton object} that sets its
 * properties based on the presence and contents of a file.
 * </p>
 *
 * @see com.selventa.belframework.common.PathConstants BEL framework path
 * constants
 */
public class PhaseFourOptions extends RuntimeConfiguration {

    /**
     * KAM setting name: {@value #KAM_NAME_DESC}
     */
    public static final String KAM_NAME_DESC = "kam_name";

    /**
     * KAM setting name: {@value #KAM_DESCRIPTION_DESC}
     */
    public static final String KAM_DESCRIPTION_DESC = "kam_description";

    private String kamName;
    private String kamDescription;

    private static final PhaseFourOptions self;
    static {
        try {
            self = new PhaseFourOptions();
        } catch (IOException e) {
            final String err = RC_READ_FAILURE;
            throw new BELRuntimeException(err, ExitCode.UNRECOVERABLE_ERROR, e);
        }
    }

    /**
     * Default private constructor.
     *
     * @throws IOException Thrown if an I/O error occurs
     */
    private PhaseFourOptions() throws IOException {
        super();
    }

    /**
     * Returns the phase four compiler options.
     *
     * @return PhaseFourOptions
     */
    public static PhaseFourOptions phaseFourOptions() {
        return self;
    }

    /**
     * Returns phase four's KAM name option.
     *
     * @return String; may be null
     */
    public final String getKAMName() {
        return kamName;
    }

    /**
     * Sets phase four's KAM name option.
     *
     * @param kamName Phase four's KAM name
     */
    public final void setKAMName(String kamName) {
        this.kamName = kamName;
    }

    /**
     * Returns phase four's KAM description option value.
     *
     * @return {@link String}, the kam description option value
     */
    public final String getKAMDescription() {
        return kamDescription;
    }

    /**
     * Sets phase four's KAM description option value.
     *
     * @param kamDescription {@link String}, the kam description option value
     */
    public final void setKAMDescription(String kamDescription) {
        this.kamDescription = kamDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processSetting(String name, String value) {
        super.processSetting(name, value);
        if (KAM_NAME_DESC.equals(name)) {
            kamName = value;
        } else if(KAM_DESCRIPTION_DESC.equals(name)) {
            kamDescription = value;
        }
    }

    /**
     * Prints the default configuration for phase four.
     *
     * @param args Ignored command-line arguments
     */
    public static void main(String... args) {
        out.println("Phase four defaults are:");
        out.println(phaseFourOptions().defaultConfiguration());
    }
}
