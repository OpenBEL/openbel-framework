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
package org.openbel.framework.common.model;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.enums.FunctionEnum.FUSION;
import static org.openbel.framework.common.enums.FunctionEnum.isProteinDecorator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;

/**
 * BEL terms represent biological entities.
 * <p>
 * They contain a number of {@link Term terms} and {@link Parameter parameters}.
 * Terms are parameter-iterable:
 * 
 * <pre>
 * <code>
 * for (final Parameter parameter : term) {
 *     // ...
 * }
 * </code>
 * </pre>
 * 
 * </p>
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Term implements BELObject, Iterable<Parameter> {
    private static final long serialVersionUID = 8921981074652515078L;

    private final FunctionEnum function;
    private List<Parameter> parameters;
    private List<Term> terms;
    private List<BELObject> functionArgs;

    /**
     * Creates a term with the required function and optional arguments
     * property.
     * 
     * @param f {@link FunctionEnum}
     * @param functionArgs List of BEL object function arguments
     * @throws InvalidArgument Thrown if {@code f} is null
     */
    public Term(FunctionEnum f, List<BELObject> functionArgs) {
        if (f == null) throw new InvalidArgument("function enum is null");
        this.function = f;
        setFunctionArgs(functionArgs);
    }

    /**
     * Creates a term with the required function.
     * 
     * @param f {@link FunctionEnum}
     * @throws InvalidArgument Thrown if {@code f} is null
     */
    public Term(FunctionEnum f) {
        if (f == null) throw new InvalidArgument("function enum is null");
        this.function = f;
    }

    /**
     * Returns the term's function.
     * 
     * @return {@link FunctionEnum}, the function type
     */
    public FunctionEnum getFunctionEnum() {
        return function;
    }

    /**
     * Returns the term's list of parameters.
     * 
     * @return List of parameters, which may be null
     */
    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * Returns the term's nested list of terms.
     * 
     * @return List of terms, which may be null
     */
    public List<Term> getTerms() {
        return terms;
    }

    /**
     * Returns the number of function arguments.
     * <p>
     * The arguments to a term's function can either be a {@link Parameter
     * parameter} or another {@link Term term} (considered an <i>inner</i>
     * term). This means the {@code number_of_arguments} is equal to the
     * {@code (number_of_parameters + number_of_terms)}. I.e.,
     * 
     * <pre>
     * <code>
     * getNumberOfArguments() == (getNumberOfParameters() + getNumberOfTerms())
     * </code>
     * </pre>
     * 
     * </p>
     * 
     * @return int
     * @see #getNumberOfParameters()
     * @see #getNumberOfTerms()
     */
    public int getNumberOfArguments() {
        if (functionArgs != null) return functionArgs.size();
        return 0;
    }

    /**
     * Returns the number of parameters.
     * <p>
     * The arguments to a term's function can either be a {@link Parameter
     * parameter} or another {@link Term term} (considered an <i>inner</i>
     * term). This means the {@code number_of_parameters} is equal to the
     * {@code (number_of_arguments - number_of_terms)}. I.e.,
     * 
     * <pre>
     * <code>
     * getNumberOfParameters() == (getNumberOfArguments() - getNumberOfTerms())
     * </code>
     * </pre>
     * 
     * </p>
     * 
     * @return int
     * @see #getNumberOfArguments()
     * @see #getNumberOfTerms()
     */
    public int getNumberOfParameters() {
        return parameters == null ? 0 : parameters.size();
    }

    /**
     * Returns the number of terms.
     * <p>
     * The arguments to a term's function can either be a {@link Parameter
     * parameter} or another {@link Term term} (considered an <i>inner</i>
     * term). This means the {@code number_of_terms} is equal to the
     * {@code (number_of_arguments - number_of_parameters)}. I.e.,
     * 
     * <pre>
     * <code>
     * getNumberOfTerms() == (getNumberOfArguments() - getNumberOfParameters())
     * </code>
     * </pre>
     * 
     * </p>
     * 
     * @return int
     * @see #getNumberOfArguments()
     * @see #getNumberOfParameters()
     */
    public int getNumberOfTerms() {
        return terms == null ? 0 : terms.size();
    }

    /**
     * Returns the term's arguments, <b>in order</b>, to its associated
     * function.
     * <p>
     * The term's {@link #getParameters() parameters} and {@link #getTerms()
     * nested terms} compose the arguments to its associated BEL function. This
     * method encapsulates the ordering of these arguments for the term's
     * function.
     * </p>
     * 
     * @return List of {@link BELObject BEL objects}, which may be null, in
     * order for its associated {@link #getFunctionEnum() function}
     */
    public List<BELObject> getFunctionArguments() {
        return functionArgs;
    }

    /**
     * Adds a function argument to the end of the function argument list.
     * <p>
     * The argument will be additionally added to the terms or parameters,
     * depending on its type.
     * </p>
     * 
     * @param arg BEL object function argument
     * @throws InvalidArgument Thrown if {@code arg} is null
     * @throws UnsupportedOperationException Thrown if {@code arg} is not a
     * {@link Term} or {@link Parameter}
     */
    public void addFunctionArgument(final BELObject arg) {
        if (arg == null) throw new InvalidArgument("arg is null");
        if (!(arg instanceof Term) && !(arg instanceof Parameter)) {
            String err = arg.getClass().getName();
            err = err.concat(" is not a valid function argument");
            throw new UnsupportedOperationException(err);
        }

        if (functionArgs == null) {
            functionArgs = new ArrayList<BELObject>();
            terms = new ArrayList<Term>();
            parameters = new ArrayList<Parameter>();
        }
        functionArgs.add(arg);

        if (arg instanceof Term) {
            terms.add((Term) arg);
        } else {
            parameters.add((Parameter) arg);
        }
    }

    /**
     * Sets the term's arguments. This list backs the term's ordered function
     * argument, term, and parameter lists.
     * 
     * @param args List of BEL objects
     * @see #getFunctionArguments()
     */
    public void setFunctionArguments(List<BELObject> args) {
        setFunctionArgs(args);
    }

    /**
     * Returns a list of all parameters contained by both this term and any
     * nested terms.
     * 
     * @return Non-null list of parameters
     */
    public List<Parameter> getAllParameters() {
        List<Parameter> ret = new ArrayList<Parameter>();

        if (parameters != null)
            ret.addAll(parameters);

        if (terms != null) {
            for (final Term t : terms) {
                ret.addAll(t.getAllParameters());
            }
        }

        return ret;
    }

    /**
     * Returns an {@link Iterator iterator} over the document's
     * {@link Parameter parameters}.
     * <p>
     * The following code is guaranteed to be safe (no
     * {@link NullPointerException null pointer exceptions} will be thrown):
     * 
     * <pre>
     * <code>
     * for (final Parameter parameter : term) {
     * }
     * </code>
     * </pre>
     * 
     * </p>
     * 
     * @return {@link Iterator} of {@link Parameter parameters}
     */
    @Override
    public Iterator<Parameter> iterator() {
        return getAllParameters().iterator();
    }

    /**
     * Returns a list of all terms contained by and within this term.
     * 
     * @return Non-null list of terms
     */
    public List<Term> getAllTerms() {
        List<Term> ret = new ArrayList<Term>();

        if (terms != null) {
            ret.addAll(terms);
            for (final Term term : terms) {
                ret.addAll(term.getAllTerms());
            }
        }

        return ret;
    }

    /**
     * Returns all of this term's {@link Parameter} objects, in a left to right
     * sequence, contained within both the outer and inner terms.
     * <p>
     * Parameters are found in a recursive depth-first search using the
     * implementation in {@link #findParameters(List, Term)}.
     * </p>
     * 
     * @return {@link List} of all {@link Parameter} objects for this term,
     * which can be empty but will not be null
     */
    public List<Parameter> getAllParametersLeftToRight() {
        List<Parameter> params = new ArrayList<Parameter>();
        findParameters(params, this);
        return params;
    }

    /**
     * Returns {@code true} if this term is parameterized, e.g., has parameters;
     * {@code false} otherwise.
     * 
     * @return boolean
     */
    public boolean isParameterized() {
        if (getParameters() == null) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Term [");

        builder.append("function=");
        builder.append(function);
        builder.append(", ");

        if (functionArgs != null) {
            builder.append("functionArgs=");
            builder.append(functionArgs);
            builder.append(", ");
        }

        if (parameters != null) {
            builder.append("parameters=");
            builder.append(parameters);
            builder.append(", ");
        }

        if (terms != null) {
            builder.append("terms=");
            builder.append(terms);
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result *= prime;
        result += function.hashCode();

        result *= prime;
        if (functionArgs != null) result += functionArgs.hashCode();

        result *= prime;
        if (parameters != null) result += parameters.hashCode();

        result *= prime;
        if (terms != null) result += terms.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Term)) return false;

        final Term t = (Term) o;

        if (function != t.function) return false;

        if (functionArgs == null) {
            if (t.functionArgs != null) return false;
        } else if (!functionArgs.equals(t.functionArgs)) return false;

        if (parameters == null) {
            if (t.parameters != null) return false;
        } else if (!parameters.equals(t.parameters)) return false;

        if (terms == null) {
            if (t.terms != null) return false;
        } else if (!terms.equals(t.terms)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toBELLongForm() {
        return toBEL(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toBELShortForm() {
        return toBEL(true);
    }

    private String toBEL(boolean shortForm) {
        StringBuilder belBuilder = new StringBuilder();
        String fs = shortForm
                ? (function.getAbbreviation() != null
                        // some functions have no short form
                        ? function.getAbbreviation()
                        : function.getDisplayValue()
                )
                : function.getDisplayValue();
        belBuilder.append(fs).append("(");
        if (hasItems(functionArgs)) {
            for (BELObject functionArg : functionArgs) {
                if (shortForm) {
                    belBuilder.append(functionArg.toBELShortForm());
                } else {
                    belBuilder.append(functionArg.toBELLongForm());
                }
                belBuilder.append(",");
            }
            belBuilder.deleteCharAt(belBuilder.length() - 1);
        }
        belBuilder.append(")");
        return belBuilder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Term clone() {
        List<BELObject> functionArgs2 = null;
        if (functionArgs != null) {
            functionArgs2 = sizedArrayList(functionArgs.size());
            for (final BELObject bo : functionArgs)
                functionArgs2.add(bo.clone());
        }
        return new Term(function, functionArgs2);
    }

    /**
     * Find all parameters in <tt>term</tt> in a left-to-right, depth-first
     * fashion. The term's function arguments are read in order and the
     * following rules apply:
     * <ul>
     * <li>If the function argument is a parameter:
     * <ul>
     * <li>Add it to the parameter list (<tt>ps</tt>).</li>
     * </ul>
     * </li>
     * <li>If the function argument is a term:
     * <ul>
     * <li>Recurse into {@link #findParameters(List, Term)} for the nested term.
     * </li>
     * </ul>
     * </ul>
     * <p>
     * The results are captured in the <tt>params</tt> {@link List}.
     * </p>
     * 
     * @param params {@link List} of {@link Parameter}, the list of parameters
     * found from the top-level term so far
     */
    protected void findParameters(final List<Parameter> parms, final Term trm) {
        List<BELObject> tfa = trm.getFunctionArguments();
        if (hasItems(tfa)) {
            for (BELObject bmo : tfa) {
                if (Parameter.class.isAssignableFrom(bmo.getClass())) {
                    Parameter p = (Parameter) bmo;
                    parms.add(p);
                } else {
                    Term inner = (Term) bmo;
                    if (!isProteinDecorator(inner.function)) {
                        findParameters(parms, inner);
                    } else if (FUSION.equals(inner.function)
                            && hasItems(inner.getParameters())) {
                        parms.add(inner.getParameters().get(0));
                    }
                }
            }
        }
    }

    /**
     * Sets the function arguments, terms, and parameters. A null argument will
     * result in null function arguments, terms, and parameters.
     * 
     * @param args List of BEL objects, or null
     * @throws UnsupportedOperationException Thrown if a {@link BELObject}
     * within {@code args} is not a {@link Term} or {@link Parameter}
     */
    private void setFunctionArgs(final List<BELObject> args) {
        if (args != null) {
            this.functionArgs = args;
            this.terms = new ArrayList<Term>();
            this.parameters = new ArrayList<Parameter>();
            for (final BELObject arg : functionArgs) {
                if (arg instanceof Term) {
                    terms.add((Term) arg);
                } else if (arg instanceof Parameter) {
                    parameters.add((Parameter) arg);
                } else {
                    String err = arg.getClass().getName();
                    err = err.concat(" is not a valid function argument");
                    throw new UnsupportedOperationException(err);
                }
            }
        } else {
            this.functionArgs = null;
            this.terms = null;
            this.parameters = null;
        }
    }
}
