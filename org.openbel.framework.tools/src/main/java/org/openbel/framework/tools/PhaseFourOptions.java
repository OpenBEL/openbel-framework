package org.openbel.framework.tools;

import static java.lang.System.out;
import static org.openbel.framework.common.Strings.RC_READ_FAILURE;
import static org.openbel.framework.common.enums.ExitCode.UNRECOVERABLE_ERROR;

import java.io.IOException;

import org.openbel.framework.common.BELRuntimeException;
import org.openbel.framework.common.Strings;
import org.openbel.framework.common.cfg.RuntimeConfiguration;

/**
 * {@link PhaseFourOptions} defines compiler options for the execution of phase
 * four.
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class PhaseFourOptions extends RuntimeConfiguration {

    private boolean noOrthology;

    /**
     * Singleton instance initialized statically at load time.
     */
    private static final PhaseFourOptions self;
    static {
        try {
            self = new PhaseFourOptions();
        } catch (IOException e) {
            final String err = RC_READ_FAILURE;
            throw new BELRuntimeException(err, UNRECOVERABLE_ERROR, e);
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
     * @return {@link PhaseFourOptions} the phase four options
     */
    public static PhaseFourOptions phaseFourOptions() {
        return self;
    }

    /**
     * Return whether the {@link Strings#PHASE4_NO_ORTHOLOGY_LONG_OPTION}
     * option is set.
     *
     * @return {@code true} if orthology knowledge should not be added,
     * {@code false} if orthology knowledge should be added
     */
    public boolean isNoOrthology() {
        return noOrthology;
    }

    /**
     * Sets the {@link Strings#PHASE4_NO_ORTHOLOGY_LONG_OPTION} option.
     *
     * @param noOrthology {@code true} if orthology knowledge should not be
     * added, {@code false} if orthology knowledge should be added
     */
    public void setNoOrthology(boolean noOrthology) {
        this.noOrthology = noOrthology;
    }

    /**
     * Prints the default configuration for phase four.
     *
     * @param args Ignored command-line arguments
     */
    public static void main(String... args) {
        out.println("Phase three defaults are:");
        out.println(phaseFourOptions().defaultConfiguration());
    }
}
