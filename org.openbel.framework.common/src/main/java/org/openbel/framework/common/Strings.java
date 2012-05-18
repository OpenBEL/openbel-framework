/**
 * Copyright (C) 2012 Selventa, Inc.
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
package org.openbel.framework.common;

import static org.openbel.framework.common.cfg.SystemConfiguration.RESOURCE_INDEX_URL_DESC;

/**
 * This class defines the set of strings used throughout BEL.
 * <p>
 * TODO: restore formatting sanity - some of these string definitions go out to
 * hundreds of columns.
 * </p>
 */
public class Strings {

    /**
     * {@value}
     */
    public static final String ABUNDANCE = "abundance";

    /**
     * {@value}
     */
    public static final String ABUNDANCE_ABBREV = "a";

    /**
     * {@value}
     */
    public static final String ALL_DOCUMENTS_FAILED_CONVERSION =
            "All documents failed conversion";

    /**
     * {@value}
     */
    public static final String ALL_DOCUMENTS_FAILED_VALIDATION =
            "All documents failed validation";

    /**
     * {@value}
     */
    public static final String ANNOTATION_WARNING =
            "%s is not found in annotation type %s";

    /**
     * {@value}
     */
    public static final String BAD_INPUT_PATH = "Bad input path: ";

    /**
     * {@value}
     */
    public static final String BAD_OUTPUT_DIR = "Bad output directory";

    /**
     * {@value}
     */
    public static final String BIOLOGICAL_PROCESS = "biologicalProcess";

    /**
     * {@value}
     */
    public static final String BIOLOGICAL_PROCESS_ABBREV = "bp";

    /**
     * {@value}
     */
    public final static String CANT_WRITE_TO_PATH = "Can't write to path: ";

    /**
     * {@value}
     */
    public static final String CATALYTIC_ACTIVITY = "catalyticActivity";

    /**
     * {@value}
     */
    public static final String CATALYTIC_ACTIVITY_ABBREV = "cat";

    /**
     * {@value}
     */
    public static final String CELL_SECRETION = "cellSecretion";

    /**
     * {@value}
     */
    public static final String CELL_SECRETION_ABBREV = "sec";

    /**
     * {@value}
     */
    public static final String CELL_SURFACE_EXPRESSION =
            "cellSurfaceExpression";

    /**
     * {@value}
     */
    public static final String CELL_SURFACE_EXPRESSION_ABBREV = "surf";

    /**
     * {@value}
     */
    public static final String CHAPERONE_ACTIVITY = "chaperoneActivity";

    /**
     * {@value}
     */
    public static final String CHAPERONE_ACTIVITY_ABBREV = "chap";

    /**
     * {@value}
     */
    public static final String CHECK_PEDANTIC = "Enables pedantic mode. "
            + "This mode treats warnings as errors and will return a failure "
            + "code if a syntax or semantic warning is detected in the input "
            + "file.\nThe default is to disable pedantic mode.";

    /**
     * {@value}
     */
    public static final String CHECK_PEDANTIC_PERMISSIVE_ERROR =
            "Choose one of --pedantic or --permissive for document checking.";

    /**
     * {@value}
     */
    public static final String CHECK_PERMISSIVE = "Enables permissive mode. "
            + "This mode will return a success code if syntactic or semantic "
            + "warnings are detected in the input file.\nThe default is to "
            + "disable permissive mode.";

    /**
     * {@value}
     */
    public static final String CHECK_QUIET = "Enables quiet mode. "
            + "This mode supresses warnings and errors sent to the output.\n"
            + "The default is to disable quiet mode.";

    /**
     * {@value}
     */
    public static final String CHECK_SUMMARY = "Optional. This switch will "
            + "output the number of warnings and errors detected in the "
            + "input file. The summary will still be written if the --quiet "
            + "switch is set.";
    /**
     * {@value}
     */
    public static final String COMPLEX_ABUNDANCE = "complexAbundance";

    /**
     * {@value}
     */
    public static final String COMPLEX_ABUNDANCE_ABBREV = "complex";

    /**
     * {@value}
     */
    public static final String COMPOSITE_ABUNDANCE = "compositeAbundance";

    /**
     * {@value}
     */
    public static final String COMPOSITE_ABUNDANCE_ABBREV = "composite";

    /**
     * {@value}
     */
    public static final String DEBUG_HELP =
            "Enables debug mode.\nThe default is to disable debug mode.";

    /**
     * {@value}
     */
    public static final String DEGRADATION = "degradation";

    /**
     * {@value}
     */
    public static final String DEGRADATION_ABBREV = "deg";

    /**
     * {@value}
     */
    public static final String DIRECTORY_CREATION_FAILED =
            "Failed to create directory: ";

    /**
     * {@value}
     */
    public static final String DIRECTORY_DELETION_FAILED =
            "Failed to delete directory: ";

    /**
     * {@value}
     */
    public static final String DOCUMENT_CONTAINS_ERRORS =
            "Document contains %d error(s)";

    /**
     * {@value}
     */
    public static final String DOCUMENT_CONTAINS_WARNINGS =
            "Document contains %d warning(s)";

    /**
     * {@value}
     */
    public final static String ERROR_DOWNLOADING_RESOURCE =
            "Error downloading resource";

    /**
     * {@value}
     */
    public static final String EXPAND_NAMED_COMPLEXES_HELP =
            "Modifies Phase III to inject named complex members and associated hasComponent edges when a member of a named complex is defined in a BEL Document.\nThe default is to not inject named complex nodes and edges.";

    /**
     * {@value}
     */
    public static final String EXPAND_PROTEIN_FAMILIES_HELP =
            "Modifies Phase III to inject protein family nodes, members, and associated hasMember edges when a member of a protein family is defined in a BEL Document.\nThe default is to not inject protein family nodes and edges.";

    /**
     * {@value}
     */
    public static final String EXPECTED_ONE_NETWORK =
            "Expected one merged network";

    /**
     * {@value}
     */
    public static final String EXPORT_BEL_SCRIPT =
            "Write all BEL Documents using BEL Script format.\n" +
                    "The default is to write as XBEL format.";

    /**
     * {@value}
     */
    public static final String EXPORT_DOCUMENT_GROUPS =
            "Optional. A list of Document Groups in the BEL Document Store to export the BEL Document(s) from.";

    /**
     * {@value}
     */
    public static final String EXPORT_DOCUMENTS =
            "Optional. A list of BEL Documents to export.";

    /**
     * {@value}
     */
    public static final String EXPORT_NO_PRESERVE =
            "If present, if a BEL Document with the same name exists in the output directory it will be overwritten.\n"
                    +
                    "The default is to preserve the BEL Document in the output directory and to generate a warning.";

    /**
     * {@value}
     */
    public static final String EXPORT_OUTPUT =
            "The path to a directory where the BEL Document file(s) will be written. This directory must exist and be writable";

    /**
     * {@value}
     */
    public static final String EXPORT_XBEL =
            "Write all BEL Documents using XBEL format. This is the default.";

    /**
     * {@value}
     */
    public static final String FUSION = "fusion";

    /**
     * {@value}
     */
    public static final String FUSION_ABBREV = "fus";

    /**
     * {@value}
     */
    public static final String GENE_ABUNDANCE = "geneAbundance";

    /**
     * {@value}
     */
    public static final String GENE_ABUNDANCE_ABBREV = "g";

    /**
     * {@value}
     */
    public static final String GENERAL_FAILURE = "General failure";

    /**
     * {@value}
     */
    public static final String GS_INJECTION_DISABLED =
            "Gene scaffolding injection disabled";

    /**
     * {@value}
     */
    public static final String GTP_BOUND_ACTIVITY = "gtpBoundActivity";

    /**
     * {@value}
     */
    public static final String GTP_BOUND_ACTIVITY_ABBREV = "gtp";

    /**
     * {@value}
     */
    public static final String HASH =
            "Generate a checksum for the specified file.";

    /**
     * {@value}
     */
    public static final String IMPORT_DOCUMENT_GROUP =
            "Optional name of a Document Group in the Document Store to add the BEL Document(s) to. "
                    +
                    "If not specified, the BEL Document(s) will be added without being assigned to any Document Groups.";

    /**
     * {@value}
     */
    public static final String IMPORT_NO_PRESERVE =
            "If present, if a BEL Document with the same name and version exists in the Document Store it will be overwritten.\n"
                    +
                    "The default is to preserve the existing BEL Document in the Document Store and generate a warning.";

    /**
     * {@value}
     */
    public static final String IMPORT_PEDANTIC =
            "Enables pedantic mode. This mode treats warnings as errors and will terminate the import of a BEL Document "
                    +
                    "if a syntax or semantic warning is detected in the input file.\n"
                    +
                    "The default is to disable pedantic mode.";

    /**
     * {@value}
     */
    public static final String IMPORT_PEDANTIC_PERMISSIVE_ERROR =
            "Choose one of --pedantic or --permissive for document import.";

    /**
     * {@value}
     */
    public static final String IMPORT_PERMISSIVE =
            "Enables permissive mode. This mode will allow the import of documents which contain syntax errors at the BEL Statement level.\n"
                    +
                    "The default is to disable permissive mode.";

    /**
     * {@value}
     */
    public static final String INJECTIONS_DISABLED = "Injections disabled";

    /**
     * {@value}
     */
    public static final String INPUT_FILE_UNREADABLE =
            "Input file cannot be read: ";

    /**
     * {@value}
     */
    public static final String INVALID_ANNOTATIONS =
            "%d invalid annotation(s)";

    /**
     * {@value}
     */
    public static final String INVALID_NAMESPACE_RESOURCE_LOCATION =
            "Invalid namespace resource location '%s'";

    /**
     * {@value}
     */
    public static final String INVALID_NAMESPACE_VALUE =
            "Invalid namespace value '%s'";

    /**
     * {@value}
     */
    public static final String INVALID_SYMBOLS =
            "%d invalid symbol(s)";

    /**
     * {@value}
     */
    public final static String IO_ERROR = "I/O error";

    /**
     * {@value}
     */
    public static final String KAM_CATALOG_LOAD_FAILED =
            "Error loading into KAM catalog";

    /**
     * {@value}
     */
    public static final String KAM_CONNECTION_FAILURE =
            "Error connecting to KAM store";

    /**
     * {@value}
     */
    public static final String KAM_DESCRIPTION_HELP =
            "A description for the KAM that will be created. Put the description in quotes. It may be necessary "
                    +
                    "to escape the quotes (i.e. \\\") for the full description to be recognized by BELC.";

    /**
     * {@value}
     */
    public static final String KAM_LOAD_FAILURE = "Error loading KAM";

    /**
     * {@value}
     */
    public static final String KAM_NAME = "KAM Store name";

    /**
     * {@value}
     */
    public static final String KAM_NAME_HELP = "Name of the KAM to create."
            + " If a KAM with same name exists it will be overwritten if the"
            + " --no-preserve option is set.";

    /**
     * {@value}
     */
    public static final String KAM_PASSWORD = "password for KAM Store";

    /**
     * {@value}
     */
    public static final String KAM_REQUEST_NO_HANDLE = "KAM handle is required";

    /**
     * {@value}
     */
    public static final String KAM_REQUEST_NO_KAM =
            "'kam' is required";

    /**
     * {@value}
     */
    public static final String KAM_REQUEST_NO_KAM_FOR_HANDLE =
            "no KAM found with handle \"%s\"";

    /**
     * {@value}
     */
    public static final String DIALECT_REQUEST_NO_DIALECT_FOR_HANDLE =
            "no Dialect found with handle \"%s\"";

    /**
     * {@value}
     */
    public static final String KAM_REQUEST_NO_KAM_FOR_NAME =
            "no KAM found with name \"%s\"";

    /**
     * {@value}
     */
    public static final String KAM_REQUEST_NO_NAME =
            "KAM 'name' is required";

    /**
     * {@value}
     */
    public static final String KAM_URL = "database URL";

    /**
     * {@value}
     */
    public static final String KAM_USER = "user for KAM Store";

    /**
     * {@value}
     */
    public static final String KINASE_ACTIVITY = "kinaseActivity";

    /**
     * {@value}
     */
    public static final String KINASE_ACTIVITY_ABBREV = "kin";

    /**
     * {@value}
     */
    public static final String LIST = "list";

    /**
     * {@value}
     */
    public static final String LIST_CACHE = "List the resources in the cache";

    /**
     * {@value}
     */
    public static final String LOAD_INDEX_FILE =
            "Read the index file specified by the URL and cache all resources "
                    +
                    "identified in the index file.";

    /**
     * {@value}
     */
    public static final String LOAD_INDEX_FROM_SYSCONFIG = "Read the index"
            + " file referenced using the " + RESOURCE_INDEX_URL_DESC + " of"
            + " the system configuration file and cache all resources"
            + " identified in the index file.";

    /**
     * {@value}
     */
    public static final String LOAD_RESOURCE = "Add the resource specified"
            + " by URL to the cache. If the resource "
            +
            "already exists in the cache it will be updated.";

    /**
     * {@value}
     */
    public static final String MICRO_RNA_ABUNDANCE = "microRNAAbundance";

    /**
     * {@value}
     */
    public static final String MICRO_RNA_ABUNDANCE_ABBREV = "m";

    /**
     * {@value}
     */
    public static final String MISSING_KAM_DESCRIPTION =
            "Missing KAM description";

    /**
     * {@value}
     */
    public static final String MISSING_KAM_NAME = "Missing KAM name";

    /**
     * {@value}
     */
    public final static String MISSING_SYSCFG = "Missing system configuration";

    /**
     * {@value}
     */
    public static final String MOLECULAR_ACTIVITY = "molecularActivity";

    /**
     * {@value}
     */
    public static final String MOLECULAR_ACTIVITY_ABBREV = "act";

    /**
     * {@value}
     */
    public static final String NC_INJECTION_DISABLED =
            "Named complex injection disabled";

    /**
     * {@value}
     */
    public static final String NETWORK_MERGE_FAILURE =
            "Failed merging proto-networks";

    /**
     * {@value}
     */
    public static final String NO_DOCUMENT_FILES = "No documents found";

    /**
     * {@value}
     */
    public static final String NO_GENE_SCAFFOLDING_HELP =
            "Modifies Phase III to omit expanding the composite network to include gene activation pathways for gene products identified in BEL Document input to the compiler.\nThe default is to expand gene activation pathways for identified gene products.";

    /**
     * {@value}
     */
    public static final String NO_GLOBAL_PROTO_NETWORK =
            "No merged network file";

    /**
     * {@value}
     */
    public static final String NO_NAMED_COMPLEXES_HELP =
            "Modifies Phase III to omit coupling named complexes to its components in the composite network.\nThe default is to automatically create edges from named complexes, in a BEL Document, to their components.";

    /**
     * {@value}
     */
    public static final String NO_NETWORKS_SAVED = "No proto-networks saved";

    /**
     * {@value}
     */
    public static final String NO_PHASE_THREE =
            "Executes Phase III in pass-through mode. This is the same as specifying --no-gene-scaffolding, --no-named-complexes, and --no-protein-families.";

    /**
     * {@value}
     */
    public static final String NO_PRESERVE_HELP =
            "If present, if a KAM with the same name exists it will be overwritten.\n"
                    +
                    "The default is to preserve the KAM and exit with an error if the existing KAM will be overwritten.";

    /**
     * {@value}
     */
    public static final String NO_PROTEIN_FAMILIES_HELP =
            "Modifies Phase III to omit coupling protein family members to existing nodes in the composite network.\nThe default is to automatically create edges from protein family nodes to their members.";

    /**
     * {@value}
     */
    public static final String NO_PROTO_NETWORKS = "No proto-networks";

    /**
     * {@value}
     */
    public static final String NO_XBEL_DOCUMENTS = "No XBEL documents";

    /**
     * {@value}
     */
    public static final String NO_XBEL_INPUT = "No XBEL input provided";

    /**
     * {@value}
     */
    public static final String NOT_A_DIRECTORY = "Not a directory";

    /**
     * {@value}
     */
    public final static String NOT_A_NUMBER = "Not a number: ";

    /**
     * {@value}
     */
    public static final String NOT_A_PHASE1_DIR = "Not a phase one directory";

    /**
     * {@value}
     */
    public static final String NOT_A_PHASE2_DIR = "Not a phase two directory";

    /**
     * {@value}
     */
    public static final String OOM = "Out of memory";

    /**
     * {@value}
     */
    public static final String OXIDOREDUCTASE_ACTIVITY =
            "oxidoreductaseActivity";

    /**
     * {@value}
     */
    public static final String OXIDOREDUCTASE_ACTIVITY_ABBREV = "oxi";

    /**
     * {@value}
     */
    public static final String PARSE_ERROR =
            "Error parsing command-line arguments";

    /**
     * {@value}
     */
    public static final String ERROR_INIT_LOGGING =
            "Error initializing logging (%s), attempting to continue";

    /**
     * {@value}
     */
    public static final String PATHOLOGY = "pathology";

    /**
     * {@value}
     */
    public static final String PATHOLOGY_ABBREV = "path";

    /**
     * {@value}
     */
    public final static String PEDANTIC_CONVERSION_FAILURE =
            "Conversion failure";

    /**
     * {@value}
     */
    public final static String PEDANTIC_NAMESPACE_INDEXING_FAILURE =
            "Namespace indexing failure";

    /**
     * {@value}
     */
    public final static String PEDANTIC_NAMESPACE_RESOLUTION_FAILURE =
            "Namespace resolution failure";

    /**
     * {@value}
     */
    public final static String PEDANTIC_PROTO_NETWORK_SAVE_FAILURE =
            "Proto-network save failure";

    /**
     * {@value}
     */
    public final static String PEDANTIC_SEMANTIC_VERIFICATION_FAILURE =
            "Semantic verification failure";

    /**
     * {@value}
     */
    public final static String PEDANTIC_STATEMENT_EXPANSION_FAILURE =
            "Statement expansion failure";

    /**
     * {@value}
     */
    public final static String PEDANTIC_SYMBOL_VERIFICATION_FAILURE =
            "Symbol verification failure";

    /**
     * {@value}
     */
    public final static String PEDANTIC_TERM_EXPANSION_FAILURE =
            "Term expansion failure";

    /**
     * {@value}
     */
    public final static String PEDANTIC_VALIDATION_FAILURE =
            "Validation failure";

    /**
     * {@value}
     */
    public static final String PEPTIDASE_ACTIVITY = "peptidaseActivity";

    /**
     * {@value}
     */
    public static final String PEPTIDASE_ACTIVITY_ABBREV = "pep";

    /**
     * {@value}
     */
    public static final String PF_INJECTION_DISABLED =
            "Protein family injection disabled";

    /**
     * {@value}
     */
    public static final String PHASE_1_DISABLE_NESTED_STMTS = "Modifies Phase"
            + " I to force the compiler not to create a relationship between"
            + " the subject term of a statement with a nested statement as an"
            + " object, to the object term of the nested statement.\n"
            + "The default is to couple nested statements.";

    /**
     * {@value}
     */
    public static final String PHASE_1_INPUT_FILE =
            "File name of an XBEL or BEL Script file to compile."
                    + " This option may be used multiple times.";

    /**
     * {@value}
     */
    public static final String PHASE_1_INPUT_PATH = "Path to a folder"
            + " containing one or more XBEL or BEL Script files to compile."
            + " This option may be used multiple times.";

    /**
     * {@value}
     */
    public static final String PHASE_1_NO_SEMANTICS = "Bypasses the semantic"
            + " checker. When set, this option will omit checking for semantic"
            + " errors in the input documents.\nThe default is to enable"
            + " semantic checking.";

    /**
     * {@value}
     */
    public static final String PHASE_1_NO_SYNTAX = "Bypasses the syntax"
            + " checker. When set, this option will omit checking for"
            + " syntax errors in the input documents. This is not recommended"
            + " as it can lead to unpredictable KAM topologies.\n"
            + "The default is to enable syntax checking.";

    /**
     * {@value}
     */
    public final static String PHASE1_STAGE1_HDR = "Validation";

    /**
     * {@value}
     */
    public final static String PHASE1_STAGE2_HDR = "Namespace compilation";

    /**
     * {@value}
     */
    public final static String PHASE1_STAGE3_HDR = "Symbol verification";

    /**
     * {@value}
     */
    public final static String PHASE1_STAGE4_HDR = "Semantic verification";

    /**
     * {@value}
     */
    public final static String PHASE1_STAGE5_HDR = "Building proto-networks";

    /**
     * {@value}
     */
    public final static String PHASE1_STAGE6_HDR = "Expansion";

    /**
     * {@value}
     */
    public final static String PHASE1_STAGE7_HDR = "Saving proto-networks";

    /**
     * {@value}
     */
    public final static String PHASE2_STAGE1_HDR = "Merging proto-networks";

    /**
     * {@value}
     */
    public final static String PHASE2_STAGE2_HDR = "Loading equivalence index";

    /**
     * {@value}
     */
    public final static String PHASE2_STAGE3_HDR =
            "Equivalencing merged network";

    /**
     * {@value}
     */
    public final static String PHASE2_STAGE4_HDR = "Saving merged network";

    /**
     * {@value}
     */
    public final static String PHASE3_STAGE1_HDR =
            "Processing protein families";

    /**
     * {@value}
     */
    public final static String PHASE3_STAGE2_HDR = "Processing named complexes";

    /**
     * {@value}
     */
    public final static String PHASE3_STAGE3_HDR =
            "Processing gene scaffolding";

    /**
     * {@value}
     */
    public final static String PHASE3_STAGE4_HDR =
            "Equivalencing merged network";

    /**
     * {@value}
     */
    public final static String PHASE3_STAGE5_HDR = "Saving augmented network";

    /**
     * {@value}
     */
    public final static String PHASE4_STAGE1_HDR = "Building final KAM";

    /**
     * {@value}
     */
    public static final String PHOSPHATASE_ACTIVITY = "phosphataseActivity";

    /**
     * {@value}
     */
    public static final String PHOSPHATASE_ACTIVITY_ABBREV = "phos";

    /**
     * {@value}
     */
    public static final String PRODUCTS = "products";

    /**
     * {@value}
     */
    public static final String PROTEIN_ABUNDANCE = "proteinAbundance";

    /**
     * {@value}
     */
    public static final String PROTEIN_ABUNDANCE_ABBREV = "p";

    /**
     * {@value}
     */
    public static final String PROTEIN_MODIFICATION = "proteinModification";

    /**
     * {@value}
     */
    public static final String PROTEIN_MODIFICATION_ABBREV = "pmod";

    /**
     * {@value}
     */
    public static final String PURGE_CACHE =
            "Removes all resources from the cache";

    /**
     * {@value}
     */
    public final static String RC_READ_FAILURE =
            "Failed to read runtime configuration";

    /**
     * {@value}
     */
    public static final String REACTANTS = "reactants";

    /**
     * {@value}
     */
    public static final String REACTION = "reaction";

    /**
     * {@value}
     */
    public static final String REACTION_ABBREV = "rxn";

    /**
     * {@value}
     */
    public static final String RIBOSYLATION_ACTIVITY = "ribosylationActivity";

    /**
     * {@value}
     */
    public static final String RIBOSYLATION_ACTIVITY_ABBREV = "ribo";

    /**
     * {@value}
     */
    public static final String RNA_ABUNDANCE = "rnaAbundance";

    /**
     * {@value}
     */
    public static final String RNA_ABUNDANCE_ABBREV = "r";

    /**
     * {@value}
     */
    public static final String SEMANTIC_CHECKS_DISABLED =
            "Semantic checks disabled";

    /**
     * {@value}
     */
    public static final String SEMANTIC_CODE_NOT_AA =
            "\"%s\" is not a valid amino acid";

    /**
     * {@value}
     */
    public static final String SEMANTIC_LIST_IMPROPER_CONTEXT =
            "statement uses the LIST function in an improper context";

    /**
     * {@value}
     */
    public final static String SEMANTIC_NESTED_REQUIRE_CAUSAL =
            "nested statements require causal relationships";

    public final static String SEMANTIC_MULTI_NESTED =
            "statements with multiple nesting are not valid";

    /**
     * {@value}
     */
    public static final String SEMANTIC_NOT_AA = "expected a valid amino acid";

    /**
     * {@value}
     */
    public static final String SEMANTIC_NOT_CM =
            "expected a covalent modification";

    /**
     * {@value}
     */
    public static final String SEMANTIC_NOT_INT =
            "expected a positive whole number";

    /**
     * {@value}
     */
    public static final String SEMANTIC_POS_NOT_INT =
            "\"%s\" is not a positive whole number";

    /**
     * {@value}
     */
    public static final String SEMANTIC_PTYPE_NOT_CM =
            "\"%s\" is not a covalent modification";

    /**
     * {@value}
     */
    public final static String SEMANTIC_STATEMENT_PROVIDES_RELATIONSHIP =
            "statement provides relationship when object is missing";

    /**
     * {@value}
     */
    public final static String SEMANTIC_STATEMENT_REQUIRES_RELATIONSHIP =
            "statement requires relationship when object is present";

    /**
     * {@value}
     */
    public final static String SEMANTIC_TERM_FAILURE =
            "term failed semantic checks: %s";

    /**
     * {@value}
     */
    public final static String SHA_256 = "SHA-256";

    /**
     * {@value}
     */
    public static final String SUBSTITUTION = "substitution";

    /**
     * {@value}
     */
    public static final String SUBSTITUTION_ABBREV = "sub";

    /**
     * {@value}
     */
    public static final String SUCCESS = "Success";

    /**
     * {@value}
     */
    public static final String SYMBOL_CHECKS_DISABLED =
            "Symbol checks disabled";

    /**
     * {@value}
     */
    public final static String SYSCFG_READ_FAILURE =
            "Failed to read system configuration";

    /**
     * {@value}
     */
    public static final String SYSTEM_CONFIG_PATH = "Optional. Allows the"
            + " user to specify the path to a system configuration file"
            + " to use to specify the OpenBEL Framework configuration.\n"
            + "The default is to use the default systems configuration file"
            + " found in [installdir]/config.";

    /**
     * {@value}
     */
    public static final String TIME_HELP = "Enables timing mode. This mode"
            + " outputs times in seconds for each reported phase of the"
            + " compiler/assembler. Usually used in conjunction with"
            + " --verbose.\nThe default is to disable timing mode.";

    /**
     * {@value}
     */
    public static final String TRANSCRIPTIONAL_ACTIVITY =
            "transcriptionalActivity";

    /**
     * {@value}
     */
    public static final String TRANSCRIPTIONAL_ACTIVITY_ABBREV = "tscript";

    /**
     * {@value}
     */
    public static final String TRANSLOCATION = "translocation";

    /**
     * {@value}
     */
    public static final String TRANSLOCATION_ABBREV = "tloc";

    /**
     * {@value}
     */
    public static final String TRANSPORT_ACTIVITY = "transportActivity";

    /**
     * {@value}
     */
    public static final String TRANSPORT_ACTIVITY_ABBREV = "tport";

    /**
     * {@value}
     */
    public static final String TRUNCATION = "truncation";

    /**
     * {@value}
     */
    public static final String TRUNCATION_ABBREV = "trunc";

    /**
     * {@value}
     */
    public final static String UNRECOVERABLE_ERROR_MSG = "Unrecoverable error";

    /**
     * {@value}
     */
    public static final String UPDATE_CACHE =
            "Update cache with the latest resources";

    /**
     * {@value}
     */
    public static final String UTF_8 = "UTF-8";

    /**
     * {@value}
     */
    public static final String VERBOSE_HELP = "Enables verbose mode. This mode"
            + " outputs additional information as the application runs.\nThe"
            + " default is to disable verbose mode.";

    /**
     * {@value}
     */
    public static final String WARNINGS_AS_ERRORS = "Enables pedantic mode."
            + " This mode treats warnings as errors and will terminate the"
            + " compiler/assembler at the end of a phase if a warning is"
            + " generated in the phase.\nThe default is to disable pedantic"
            + " mode.";

    /**
     * Default private constructor.
     */
    private Strings() {

    }
}
