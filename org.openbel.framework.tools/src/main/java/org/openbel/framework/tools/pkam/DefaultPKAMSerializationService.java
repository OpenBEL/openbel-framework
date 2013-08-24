/**
 *  Copyright 2013 OpenBEL Consortium
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openbel.framework.tools.pkam;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.nulls;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.compiler.kam.KAMStoreSchemaService;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.DatabaseService;
import org.openbel.framework.core.df.encryption.EncryptionServiceException;
import org.openbel.framework.core.df.encryption.KamStoreEncryptionServiceImpl;
import org.openbel.framework.internal.KAMCatalogDao;
import org.openbel.framework.internal.KamDbObject;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * {@link DefaultPKAMSerializationService} implements service to serialize a
 * KamStore KAM database into Portable KAM format.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DefaultPKAMSerializationService implements
        PKAMSerializationService {
    private final static char FIELD_SEPARATOR = '\t';
    private final static Pattern TABLE_HEADER_PATTERN = Pattern
            .compile("\\[(\\w+)\\]");
    private final DatabaseService databaseService;
    private final KamStoreEncryptionServiceImpl encryptionService;
    private final KAMStoreSchemaService schemaService;

    /**
     * Constructs the DefaultPKAMSerializationService with the
     * {@link DatabaseService} dependency.
     *
     * @param databaseService {@link DatabaseService} dependency, which
     * cannot be null
     * @throws InvalidArgument Thrown if <tt>databaseService</tt> is null
     */
    public DefaultPKAMSerializationService(
            final DatabaseService databaseService,
            final KamStoreEncryptionServiceImpl encryptionService,
            final KAMStoreSchemaService schemaService) {
        if (nulls(databaseService, encryptionService, schemaService)) {
            throw new InvalidArgument("argument(s) are null");
        }

        this.databaseService = databaseService;
        this.encryptionService = encryptionService;
        this.schemaService = schemaService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeKAM(final String kamName, String filePath,
            final String password)
            throws PKAMSerializationFailure {
        if (nulls(kamName, filePath)) {
            throw new InvalidArgument("argument(s) were null");
        }

        final SystemConfiguration cfg =
                SystemConfiguration.getSystemConfiguration();

        KamInfo kamInfo = loadKAMInfo(kamName, filePath, cfg);

        PKAMWriter pkw = null;
        CSVWriter tabbedWriter = null;
        KAMExportDAO kdao = null;
        try {
            if (password == null) {
                // blank password, create PKAM writer without encryption
                pkw = new PKAMWriter(filePath);
            } else {
                // password is set, create PKAM writer with encryption
                pkw = new PKAMWriter(filePath, password, encryptionService);
            }

            tabbedWriter = new CSVWriter(pkw, FIELD_SEPARATOR,
                    CSVParser.DEFAULT_QUOTE_CHARACTER,
                    CSVParser.DEFAULT_ESCAPE_CHARACTER);

            final DBConnection kcc = createKAMConnection(kamName, cfg);
            kdao = new KAMExportDAO(kcc, kamInfo.getSchemaName(),
                    encryptionService);
            for (KAMStoreTables1_0 kamTable : KAMStoreTables1_0.values()) {
                tabbedWriter.writeNext(new String[] { "["
                        + kamTable.getTableName() + "]" });

                // write out column headers
                // tabbedWriter.writeNext(kamTable.getOtherColumnNames());

                // query all rows for the current kam table
                List<String[]> allRows = kdao.getAllRowsForTable(kamTable);

                // write data as tsv if there is any
                if (hasItems(allRows)) {
                    for (String[] row : allRows) {
                        tabbedWriter.writeNext(row);
                    }
                }
            }
        } catch (EncryptionServiceException e) {
            throw new PKAMSerializationFailure(kamName, e.getMessage());
        } catch (IOException e) {
            throw new PKAMSerializationFailure(kamName, e.getMessage());
        } catch (SQLException e) {
            throw new PKAMSerializationFailure(kamName, e.getMessage());
        } finally {
            // close tabbed-data writer
            if (tabbedWriter != null) {
                try {
                    tabbedWriter.close();
                } catch (IOException e1) {}
            }

            // close kam catalog connection
            if (kdao != null) {
                kdao.terminate();
            }
        }
    }

    private DBConnection createKAMConnection(final String kamName,
            final SystemConfiguration cfg) throws PKAMSerializationFailure {
        final DBConnection kcc;
        try {
            kcc = databaseService.dbConnection(
                    cfg.getKamURL(),
                    cfg.getKamUser(),
                    cfg.getKamPassword());
        } catch (SQLException e) {
            throw new PKAMSerializationFailure(kamName, e.getMessage());
        }
        return kcc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deserializeKAM(final String kamName, final String filePath,
            final String password, final boolean noPreserve)
            throws PKAMSerializationFailure {

        // load destination KAM
        final SystemConfiguration cfg =
                SystemConfiguration.getSystemConfiguration();

        // parse and insert KAM data from portable KAM file
        PKAMReader pkr = null;
        CSVReader tabbedReader = null;
        KAMImportDAO kamImportDAO = null;
        try {
            try {
                if (password == null) {
                    // blank password, create PKAM reader without decryption
                    pkr = new PKAMReader(filePath);
                } else {
                    // password is set, create PKAM reader with decryption
                    pkr = new PKAMReader(filePath, password, encryptionService);
                }
            } catch (Exception e) {
                throw new PKAMSerializationFailure(
                        filePath,
                        "Unable to process encrypted KAM, check that you provided the correct password.");
            }
            tabbedReader = new CSVReader(pkr, FIELD_SEPARATOR,
                    CSVParser.DEFAULT_QUOTE_CHARACTER,
                    CSVParser.DEFAULT_ESCAPE_CHARACTER);

            KAMStoreTables1_0 table;
            String[] data;
            while ((data = tabbedReader.readNext()) != null) {
                Matcher matcher = TABLE_HEADER_PATTERN.matcher(data[0]);
                if (data.length == 1 && matcher.matches()) {
                    table = KAMStoreTables1_0.getTableByName(matcher.group(1));

                    if (table == KAMStoreTables1_0.KAM_CATALOG_KAM) {
                        String[] kcRowData = tabbedReader.readNext();

                        final String newKAMSchema = createKAMSchema(cfg,
                                kamName, kcRowData, filePath, noPreserve);

                        final DBConnection kcc =
                                createKAMConnection(filePath, cfg);
                        kamImportDAO = new KAMImportDAO(kcc, newKAMSchema,
                                encryptionService);
                        continue;
                    }

                    if (kamImportDAO == null) {
                        throw new IllegalStateException(
                                "KAMImportDAO has not been constructed");
                    }

                    // commit previous table batch, if any
                    kamImportDAO.commitTableBatch();
                    kamImportDAO.startTableBatch(table);
                } else {
                    if (kamImportDAO == null) {
                        throw new IllegalStateException(
                                "KAMImportDAO has not been constructed");
                    }

                    kamImportDAO.importDataRow(data);
                }
            }

            if (kamImportDAO == null) {
                throw new IllegalStateException(
                        "KAMImportDAO has not been constructed");
            }

            // commit final table batch
            kamImportDAO.commitTableBatch();
        } catch (EncryptionServiceException e) {
            throw new PKAMSerializationFailure(filePath, e.getMessage());
        } catch (IOException e) {
            throw new PKAMSerializationFailure(filePath, e.getMessage());
        } catch (SQLException e) {
            throw new PKAMSerializationFailure(filePath, e.getMessage());
        } finally {
            // close tabbed-data reader
            if (tabbedReader != null) {
                try {
                    tabbedReader.close();
                } catch (IOException e) {}
            }

            if (kamImportDAO != null) {
                kamImportDAO.terminate();
            }
        }
    }

    private String createKAMSchema(final SystemConfiguration cfg,
            String kamName, final String[] rowData,
            final String filePath, final boolean noPreserve)
            throws PKAMSerializationFailure {
        if (kamName == null) {
            kamName = rowData[1];
        }

        final String kamDescription = rowData[2];
        final long lastCompiled = Long.parseLong(rowData[3]);

        // check to see if it exists
        KamInfo kamInfo = loadKAMInfo(kamName, filePath, cfg);

        // create a kam catalog entry if it does not exist
        final KamDbObject kamDb;
        if (kamInfo == null) {
            kamDb = new KamDbObject(null, kamName, kamDescription,
                    new Date(lastCompiled), null);
        } else {
            if (!noPreserve) {
                // FIXME Hack to throw noPreserve check error to be processed by the calling CLI
                throw new IllegalStateException();
            }

            kamDb = new KamDbObject(kamInfo.getId(), kamName, kamDescription,
                    new Date(lastCompiled), kamInfo.getSchemaName());
        }

        final String kamSchema;
        try {
            // create new kam entry since it does not exist
            kamSchema = schemaService.saveToKAMCatalog(kamDb);
        } catch (SQLException e) {
            throw new PKAMSerializationFailure(filePath, e.getMessage());
        }

        // setup KamStore schema
        DBConnection kcc = null;
        try {
            kcc = createKAMConnection(kamName, cfg);
            schemaService.setupKAMStoreSchema(kcc, kamSchema);
        } catch (SQLException e) {
            throw new PKAMSerializationFailure(filePath, e.getMessage());
        } catch (IOException e) {
            throw new PKAMSerializationFailure(filePath, e.getMessage());
        } finally {
            if (kcc != null) {
                kcc.close();
            }
        }

        return kamSchema;
    }

    private KamInfo loadKAMInfo(final String kamName, final String filePath,
            final SystemConfiguration cfg) throws PKAMSerializationFailure {
        final DBConnection kcc = createKAMConnection(kamName, cfg);

        KamInfo kamInfo;
        KAMCatalogDao kcdao = null;
        try {
            kcdao = new KAMCatalogDao(kcc,
                    cfg.getKamCatalogSchema(), cfg.getKamSchemaPrefix());
            kamInfo = kcdao.getKamInfoByName(kamName);
        } catch (SQLException e) {
            throw new PKAMSerializationFailure(filePath, e.getMessage());
        } finally {
            if (kcdao != null) {
                kcdao.terminate();
            }
        }

        return kamInfo;
    }
}
