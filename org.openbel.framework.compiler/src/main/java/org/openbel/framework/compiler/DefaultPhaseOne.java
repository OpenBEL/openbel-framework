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
package org.openbel.framework.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openbel.bel.xbel.model.JAXBElement;
import org.openbel.framework.common.AnnotationDefinitionResolutionException;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.BELModelObject;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.core.ConversionError;
import org.openbel.framework.core.compiler.SemanticFailure;
import org.openbel.framework.core.compiler.SymbolWarning;
import org.openbel.framework.core.compiler.ValidationError;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protonetwork.ProtoNetworkBuilder;
import org.openbel.framework.core.protonetwork.ProtoNetworkService;

/**
 * BEL compiler phase one interface.
 * <p>
 * Phase one consists of XXX stages, executed in sequence. The output of phase
 * one is a {@link ProtoNetwork proto-network}.
 * </p>
 */
public interface DefaultPhaseOne {

    /**
     * Executes stage one validation of an XBEL file.
     * <p>
     * This stage consists of:
     * <ol>
     * <li>Syntactic validation of the file against the XBEL schema</li>
     * <li>Conversion of the {@link JAXBElement JAXB-based XML model} to the
     * common model (defined by both {@link BELModelObject} and
     * {@link BELObject})</li>
     * <li>Syntactic validation of the converted model against its external
     * resources</li>
     * </ol>
     * </p>
     *
     * @param f XBEL XML file
     * @return A non-null list of validation errors
     * @throws ValidationError Thrown if the file fails validation
     */
    public Stage1Output stage1XBELValidation(File f);

    /**
     * Executes stage one validation of a BEL file.
     * <p>
     * This stage consists of:
     * <ol>
     * <li>Syntactic validation of the BEL file by executing the
     * {@link BELParser}</li>
     * <li>Conversion of the {@link org.openbel.bel.model.BELObject BELScript model} to the
     * common model (defined by both {@link BELModelObject} and
     * {@link BELObject})</li>
     * <li>Syntactic validation of the converted model against its external
     * resources</li>
     * </ol>
     * </p>
     *
     * @param f {@link File}, the BELScript file
     * @return A non-null list of validation errors
     * @throws ValidationError Thrown if the file fails validation
     */
    public Stage1Output stage1BELValidation(File f);

    /**
     * Executes stage two namespace compilation of a {@link Document BEL
     * document}.
     * <p>
     * This stage consists of:
     * <ol>
     * <li>Downloading the namespaces used by the document</li>
     * <li>Indexing the namespaces used by the document</li>
     * </ol>
     * </p>
     *
     * @param d BEL document
     * @throws ResourceDownloadError Thrown if an error occurred downloading a
     * namespace
     * @throws IndexingFailure Thrown if an error occurred while indexing a
     * namespace
     */
    public void stage2NamespaceCompilation(Document d) throws IndexingFailure,
            ResourceDownloadError;

    /**
     * Executes stage three symbol verification of a {@link Document BEL
     * document}.
     * <p>
     * This stage consists of:
     * <ol>
     * <li>Lookup of all namespace values specified by the document</li>
     * </ol>
     * </p>
     *
     * @param d BEL document
     * @throws SymbolWarning Thrown if a symbol failed verification or an error
     * occurred during symbol verification processing
     * @throws ResourceDownloadError Thrown if an error occurred downloading a
     * namespace
     * @throws IndexingFailure Thrown if an error occurred while indexing a
     * namespace
     */
    public void stage3SymbolVerification(Document d) throws SymbolWarning,
            IndexingFailure, ResourceDownloadError;

    /**
     * Executes stage four semantic verification of a {@link Document BEL
     * document}.
     * <p>
     * This stage consists of:
     * <ol>
     * <li>Semantic verification of parameters</li>
     * <li>Semantic verification of terms</li>
     * <li>Semantic verification of statements</li>
     * </ol>
     * </p>
     *
     * @param d BEL document
     * @throws SemanticFailure Thrown when a semantic failure occurs verifying
     * the provided document or an error occurred during the verification
     * process
     * @throws IndexingFailure Thrown if a term parameter's namespace could not
     * be accessed
     */
    public void stage4SemanticVerification(Document d) throws SemanticFailure,
            IndexingFailure;

    /**
     * Executes stage five building of a {@link ProtoNetwork proto-network} for
     * a {@link Document BEL document}.
     * <p>
     * This stage consists of:
     * <ol>
     * <li>{@link ProtoNetworkService#compile(Document) compiling} the BEL
     * document into a proto-network</li>
     * </ol>
     * </p>
     *
     * @param d BEL document
     * @return {@link ProtoNetwork}
     */
    public ProtoNetwork stage5Building(Document d);

    /**
     * Executes stage six expansion of a {@link Document BEL document}.
     * <p>
     * This stage consists of:
     * <ol>
     * <li>Expansion of statements</li>
     * <li>Expansion of terms</li>
     * </ol>
     *
     * <p>
     * This stage modifies the {@link ProtoNetwork proto network} by adding
     * additional proto nodes and proto edges.
     * </p>
     *
     * @param doc the original {@link Document document} needed as a dependency
     * when using {@link ProtoNetworkBuilder proto network builder}
     * @param pn the {@link ProtoNetwork proto network} to modify
     * @param stmtExpand determines if {@link ProtoNetwork proto network}
     * statements should be expanded to proto edges by inferring relationships
     */
    public void stage6Expansion(final Document doc, final ProtoNetwork pn,
            boolean stmtExpand);

    /**
     * Executes stage seven saving of a {@link ProtoNetwork proto-network}.
     * <p>
     * This stage consists of:
     * <ol>
     * <li>{@link ProtoNetworkService#write(String, ProtoNetwork) writing} the
     * {@link ProtoNetwork proto-network} to the {@code path} specified</li>
     * </ol>
     * </p>
     *
     * @param pn Proto-network
     * @param path Path to save proto-network
     * @throws ProtoNetworkError Thrown if an I/O exception occurred during
     * writing of the proto-network
     */
    public void stage7Saving(ProtoNetwork pn, File path)
            throws ProtoNetworkError;

    /**
     * Phase one, stage one output.
     */
    public static class Stage1Output {
        private final List<ValidationError> validationErrors;
        private final List<AnnotationDefinitionResolutionException> resex;
        private ConversionError conversionError;
        private SymbolWarning symbolWarning;
        private Document document;

        /**
         * Creates stage one output.
         */
        public Stage1Output() {
            validationErrors = new ArrayList<ValidationError>();
            resex = new ArrayList<AnnotationDefinitionResolutionException>();
        }

        /**
         * Returns the {@link ConversionError conversion error}, which may be
         * null.
         *
         * @return {@link ConversionError}
         */
        public ConversionError getConversionError() {
            return conversionError;
        }

        /**
         * Sets the {@link ConversionError conversion error}.
         *
         * @param e {@link ConversionError}
         */
        public void setConversionError(final ConversionError e) {
            this.conversionError = e;
        }

        /**
         * Returns the {@link SymbolWarning symbol warning}, which may be null.
         *
         * @return {@link SymbolWarning}
         */
        public SymbolWarning getSymbolWarning() {
            return symbolWarning;
        }

        /**
         * Sets the {@link SymbolWarning symbol warning}.
         *
         * @param s {@link SymbolWarning}
         */
        public void setSymbolWarning(final SymbolWarning s) {
            this.symbolWarning = s;
        }

        /**
         * Returns the {@link Document document}, which may be null.
         *
         * @return {@link Document}
         */
        public Document getDocument() {
            return document;
        }

        /**
         * Sets the {@link Document document}.
         *
         * @param d {@link Document}
         */
        public void setDocument(final Document d) {
            this.document = d;
        }

        /**
         * Adds the {@link ValidationError validation error}.
         *
         * @param e {@link ValidationError}
         */
        public void addValidationError(final ValidationError e) {
            validationErrors.add(e);
        }

        /**
         * Adds the {@link List list} of {@link ValidationError validation
         * errors}.
         *
         * @param errors A {@link List list} of {@link ValidationError
         * validation errors}
         */
        public void addValidationErrors(final List<ValidationError> errors) {
            validationErrors.addAll(errors);
        }

        /**
         * Adds the {@link AnnotationDefinitionResolutionException resolution
         * exception}.
         *
         * @param e {@link AnnotationDefinitionResolutionException}
         */
        public void addResolutionException(
                final AnnotationDefinitionResolutionException e) {
            resex.add(e);
        }

        /**
         * Adds the {@link List list} of
         * {@link AnnotationDefinitionResolutionException resolution exceptions}
         * .
         *
         * @param exceptions A {@link List list} of
         * {@link AnnotationDefinitionResolutionException resolution exceptions}
         */
        public void addResolutionExceptions(
                final List<AnnotationDefinitionResolutionException> exceptions) {
            resex.addAll(exceptions);
        }

        /**
         * Returns a {@link List list} of {@link ValidationError validation
         * errors}.
         *
         * @return {@link List} of {@link ValidationError validation errors},
         * which may be empty
         */
        public List<ValidationError> getValidationErrors() {
            return validationErrors;
        }

        /**
         * Returns {@code true} if {@link ValidationError validation errors} are
         * present, {@code false} otherwise.
         *
         * @return boolean
         */
        public boolean hasValidationErrors() {
            return validationErrors.size() != 0 ? true : false;
        }

        /**
         * Returns {@code true} if a {@link ConversionError conversion error} is
         * present, {@code false} otherwise.
         *
         * @return boolean
         */
        public boolean hasConversionError() {
            return conversionError == null ? false : true;
        }
    }
}
