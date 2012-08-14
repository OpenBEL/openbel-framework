package org.openbel.framework.test;

import static org.openbel.framework.common.cfg.SystemConfiguration.createSystemConfiguration;
import static java.lang.String.format;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;

import org.openbel.framework.api.Kam;
import org.openbel.framework.api.KAMStore;
import org.openbel.framework.api.KAMStoreException;
import org.openbel.framework.api.KAMStoreImpl;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.DatabaseService;
import org.openbel.framework.core.df.DatabaseServiceImpl;

public class KAMStoreTest {

    protected DBConnection dbc;
    protected KAMStore ks;
    protected Kam testKam;

    protected void setupKamStore(final String kamName) {
        try {
            final SystemConfiguration syscfg = createSystemConfiguration();
            final DatabaseService dbs = new DatabaseServiceImpl();
            dbc = dbs.dbConnection(syscfg.getKamURL(),
                    syscfg.getKamUser(), syscfg.getKamPassword());
            ks = new KAMStoreImpl(dbc);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not read system configuration.");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Could not create a database configuration to KAM.");
        }

        try {
            testKam = ks.getKam(kamName);
        } catch (InvalidArgument e) {
            // stupid
        } catch (KAMStoreException e) {
            e.printStackTrace();
            fail(format("The '%s' cannot be found in the KAMStore.",
                    kamName));
        }
    }

    protected void teardownKamStore() {
        if (testKam != null) ks.close(testKam);
        ks.teardown();
        dbc.close();
    }
}
