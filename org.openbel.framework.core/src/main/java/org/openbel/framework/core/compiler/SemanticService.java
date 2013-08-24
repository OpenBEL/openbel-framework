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
package org.openbel.framework.core.compiler;

import java.util.List;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;

/**
 * Performs BEL semantic verification.
 */
public interface SemanticService {

    /**
     * Checks the provided parameterized term has the correct signature for its
     * {@link Term#getFunctionEnum() function enum}.
     * <p>
     * This semantic check ensures the function signature matches what is
     * provided by the function associated with the term {@code t}, a term with
     * parameters.
     * </p>
     *
     * @param t Term
     * @throws SemanticWarning Thrown if the supplied term fails the semantic
     * verification check or an error occurred during semantic verification
     * @see org.openbel.framework.common.lang.Function BEL language
     * functions
     * @see org.openbel.framework.common.lang.Signature BEL function
     * signatures
     */
    public void checkParameterizedTerm(Term t) throws SemanticWarning;

    /**
     * Checks the non-parameterized term has the correct signature for its
     * {@link Term#getFunctionEnum() function enum}.
     * <p>
     * This semantic check ensures the function signature matches what is
     * provided by the function associated with the term {@code t}, a term
     * without parameters.
     * </p>
     *
     * @param t Term
     * @throws SemanticWarning Thrown if the supplied term fails the semantic
     * verification check or an error occurred during semantic verification
     * @see org.openbel.framework.common.lang.Function BEL language
     * functions
     * @see org.openbel.framework.common.lang.Signature BEL function
     * signatures
     */
    public void checkTerm(Term t) throws SemanticWarning;

    /**
     * Checks the provided statement contains a relationship when necessary.
     * <p>
     * This semantic check ensures a statement provides a relationship when an
     * object is present and does not provide a relationship when an object is
     * absent. The attribute indicates the relationship between subject and
     * object.
     * </p>
     *
     * @param s Statement
     * @throws SemanticWarning Thrown if the supplied statement fails the
     * semantic verification check or an error occurred during semantic
     * verification
     * @see Statement
     */
    public void checkRelationship(Statement s) throws SemanticWarning;

    /**
     * Checks the provided statement for improper use of {@link FunctionEnum#LIST LIST}.
     * <p>
     * This semantic check ensures that a statement that has an object using
     * the {@link FunctionEnum#LIST LIST} function must have a
     * relationship type of {@link RelationshipType#HAS_COMPONENTS HAS_COMPONENTS}
     * or {@link RelationshipType#HAS_MEMBERS HAS_MEMBERS}.  All statements using
     * LIST without having a proper relationship type will be pruned from the
     * document and a {@link SemanticWarning semantic warning} will be thrown.
     * </p>
     *
     * @param s Statement
     * @throws SemanticWarning Thrown if the supplied statement fails the
     * semantic verification check or an error occurs during semantic
     * verification
     * @see Statement
     */
    public void checkListUsage(Statement s, final Document doc)
            throws SemanticWarning;

    /**
     * Checks the provided statement is constrained to the set of
     * {@link RelationshipType#isCausal() causal} relationships, if and only if
     * the statement {@link Statement#hasNestedStatement() is nested}.
     *
     * @param s Statement
     * @throws SemanticWarning Thrown if the supplied statement fails the
     * semantic verification check or an error occurred during semantic
     * verification
     * @see Statement
     */
    public void checkNested(Statement s) throws SemanticWarning;

    /**
     * Checks that the provided {@link Statement statement }is only nested at
     * most one time.  If the {@link Statement statement} is nested more than
     * once then it will be pruned from the {@link Document document} and a
     * {@link SemanticWarning semantic warning} will be thrown.
     *
     * @param stmt the {@link Statement statement} to check for multi nesting
     * @param doc the {@link Document document} containing the
     * {@link Statement statement}
     * @throws SemanticWarning Thrown if the {@link Statement statement}
     * provided is nested more than once
     */
    public void checkMultiNested(final Statement stmt, final Document doc)
            throws SemanticWarning;

    /**
     * Checks the provided abundance term term is semantically valid. If the
     * provided term is not one of:
     * <ul>
     * <li>{@link FunctionEnum#GENE_ABUNDANCE gene abundance}</li>
     * <li>{@link FunctionEnum#PROTEIN_ABUNDANCE protein abundance}</li>
     * <li>{@link FunctionEnum#RNA_ABUNDANCE RNA abundance}</li>
     * </ul>
     * this method returns immediately.
     * <p>
     * This check ensures valid function arguments are being used beyond the
     * normal function and namespace value argument types. Refer to the BEL
     * function specification for more information.
     * </p>
     *
     * @param t Term
     * @throws SemanticWarning Thrown if the supplied term fails the semantic
     * verification check or an error occurred during semantic verification
     * @see org.openbel.framework.common.lang.Function BEL language
     * functions
     * @see org.openbel.framework.common.lang.Signature BEL function
     * signatures
     */
    public List<SemanticWarning> checkAbundanceSubsets(Term t);
}
