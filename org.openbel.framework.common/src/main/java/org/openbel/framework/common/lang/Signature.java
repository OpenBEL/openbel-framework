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
package org.openbel.framework.common.lang;

import static java.lang.String.valueOf;
import static java.util.regex.Pattern.compile;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.common.enums.FunctionEnum.getFunctionEnum;
import static org.openbel.framework.common.enums.SemanticStatus.*;
import static org.openbel.framework.common.enums.ValueEncoding.getValueEncoding;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.ReturnType;
import org.openbel.framework.common.enums.SemanticStatus;
import org.openbel.framework.common.enums.ValueEncoding;

/**
 * Represents a BEL function signature.
 * <p>
 * A BEL function signature has the following format:
 * 
 * <pre>
 * <i>A</i>([<i>arg1</i>,<i>arg2</i>,...,<i>argN</i>{@code [...]}])<i>B</i>
 * </pre>
 * 
 * where <i>A</i> is a BEL function enum and <i>B</i> is a BEL return type. The
 * following rules concerning arguments are:
 * <ol>
 * <li>0 or more arguments are required</li>
 * <li>The last argument may be a variable length argument where one or more is
 * included with a function</li>
 * </ol>
 * The <i>arg</i> values refer to BEL function parameters and are represented in
 * one of two ways:
 * 
 * <pre>
 * F:<i>A</i>
 * </pre>
 * 
 * where <i>A</i> is a return type. Or in the second format:
 * 
 * <pre>
 * E:<i>B</i>
 * </pre>
 * 
 * where <i>B</i> is a value encoding. In both cases, what comes after the colon
 * defines what is a valid parameter at that position in the function.
 * </p>
 * 
 * @see FunctionEnum BEL function definitions
 * @see ReturnType BEL function return types
 * @see ValueEncoding BEL function parameter encodings
 */
public final class Signature implements Serializable {
    // TODO: provide signature-based support for additional argument types (not
    // a namespace value, not a function) to better support
    // modification/substitution/truncation/fusion
    public final static String WILDCARD_ENCODING = "*";
    private static final long serialVersionUID = -9134224994229618223L;
    private final static String BAD_SIG;
    private final static Pattern ARGS_REGEX;
    private final static Pattern PARAMS_REGEX;
    private final static String ENCODE_PREFIX = "E:";
    private final static String FUNC_PREFIX = "F:";
    private final static String VARARGS_SUFFIX = "...";
    static {
        BAD_SIG = "Bad signature";
        ARGS_REGEX = compile("[()]");
        PARAMS_REGEX = compile("[:]");
    }

    private final String value;
    private final int hash;
    private final String numArgs;

    /**
     * Creates a signature with the provided value.
     * 
     * @param value Signature value
     * @throws InvalidArgument Thrown if the signature is not in the correct
     * format
     */
    public Signature(final String value) {
        if (noLength(value) || !validFormat(value)) {
            final String err = BAD_SIG.concat(", " + value);
            throw new InvalidArgument(err);
        }
        this.value = value;
        hash = value.hashCode();

        numArgs = countArgs(value);
    }

    /**
     * Returns the number of arguments for this signature.
     * 
     * @return {@link String}
     */
    public String getNumberOfArguments() {
        return numArgs;
    }

    /**
     * Returns this signature's return type.
     * 
     * @return {@link ReturnType}
     */
    public ReturnType getReturnType() {
        return returnType(value);
    }

    /**
     * Returns this signature's function enum.
     * 
     * @return {@link FunctionEnum}
     */
    public FunctionEnum getFunction() {
        return function(value);
    }

    /**
     * Returns this signature's value.
     * 
     * @return {@link String}
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns {@code true} if the string {@code s} adheres to the signature
     * format, {@code false} otherwise.
     * 
     * @param s String
     * @return boolean
     */
    private boolean validFormat(final String s) {
        String[] args = ARGS_REGEX.split(s);
        if (noItems(args)) return false;
        if (args.length < 3) return false;

        // Check for the presence of a function name
        String name = args[0];
        if (name.isEmpty()) return false;
        if (!validFunction(name)) return false;

        // Check for the presence of a return type
        String returntype = args[args.length - 1];
        if (returntype.isEmpty()) return false;
        if (!validReturnType(returntype)) return false;

        // Check for zero or more arguments
        String arglist = args[1];
        if (arglist.isEmpty()) return true;

        // Split parameters on comma
        String[] argsplit = arglist.split(",");
        for (final String arg : argsplit) {
            if (!validParameter(arg)) return false;
        }
        return true;
    }

    /**
     * Returns {@code true} if the string {@code s} adheres to the signature
     * parameter format, {@code false} otherwise.
     * 
     * @param s String
     * @return boolean
     */
    private boolean validParameter(final String s) {
        final String[] tokens = PARAMS_REGEX.split(s);
        if (tokens.length != 2) {
            return false;
        }

        final char first = s.charAt(0);
        String second = tokens[1];
        if (second.endsWith(VARARGS_SUFFIX)) {
            // value encoding / return type var args, mangle argument
            second = varargsRoot(second);
        }

        if (first == 'E') {
            if (isWildcard(second)) {
                return true;
            }

            // Verify value encoding exists as an enum
            if (getValueEncoding(second) != null) {
                return true;
            }

            // Fallback to verification based on all character discriminators
            for (final Character c : second.toCharArray()) {
                if (getValueEncoding(c) == null) {
                    return false;
                }
            }
            return true;
        } else if (first == 'F') {
            // Verify return type exists as an enum
            if (ReturnType.getReturnType(second) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if the string {@code s} is a valid function,
     * {@code false} otherwise.
     * 
     * @param s String
     * @return boolean
     */
    private boolean validFunction(final String s) {
        FunctionEnum fe = getFunctionEnum(s);
        if (fe != null) return true;
        return false;
    }

    /**
     * Returns {@code true} if the string {@code s} is a valid return type,
     * {@code false} otherwise.
     * 
     * @param s String
     * @return boolean
     */
    private boolean validReturnType(final String s) {
        ReturnType rt = ReturnType.getReturnType(s);
        if (rt != null) return true;
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * Returns the {@link SemanticStatus semantic status} of the supplied
     * signature against this one.
     * 
     * @param other {@link Signature}
     * @return SemanticStatus, which may be null
     */
    public SemanticStatus matches(final Signature other) {
        if (other == null) return null;

        // Check simple function/return types first
        if (getFunction() != other.getFunction()) return INVALID_FUNCTION;
        if (getReturnType() != other.getReturnType())
            return INVALID_RETURN_TYPE;

        String[] myargs = this.getArguments();
        String[] otherargs = other.getArguments();

        return argumentsMatch(myargs, otherargs);
    }

    /**
     * Returns {@code true} if the object {@code o} is this object,
     * {@code false} otherwise.
     * 
     * @return boolean
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Signature)) return false;

        final Signature s = (Signature) o;
        if (hash == s.hash) return true;

        return value.equals(s.value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(value);
        return builder.toString();
    }

    /**
     * Returns the argument tokens, split by {@code ,}.
     * 
     * @return {@link String} array
     */
    private String[] getArguments() {
        String[] args = ARGS_REGEX.split(value);
        return args[1].split(",");
    }

    /**
     * Returns the {@link SemanticStatus semantic status} of the supplied
     * argument lists.
     * 
     * @param args1 Argument list
     * @param args2 Argument list
     * @return SemanticStatus, which may be null
     */
    private SemanticStatus argumentsMatch(final String[] myargs,
            final String[] other) {
        String my_arg, arg;
        boolean varargs = false;
        for (int i = 0, j = 0;; i++, j++) {

            boolean my_last = (i == (myargs.length - 1));
            boolean other_last = (j == (other.length - 1));

            if (myargs[i].endsWith(VARARGS_SUFFIX)) {
                my_arg = varargsRoot(myargs[i]);
                // stay at position in argument list
                i = i - 1;
                varargs = true;
            } else {
                my_arg = myargs[i];
            }

            arg = other[j];
            final SemanticStatus status = argumentsMatch(my_arg, arg);
            if (status != VALID) return status;

            if (my_last && other_last) {
                break;
            } else if (my_last && !varargs) {
                // You have arguments left to match, I don't.
                return TOO_MANY_ARGUMENTS;
            } else if (other_last) {
                // I have arguments left to match, you don't.
                return TOO_FEW_ARGUMENTS;
            }
        }
        return VALID;
    }

    /**
     * Returns the {@link SemanticStatus semantic status} of the supplied
     * arguments.
     * 
     * @param arg1 Argument list
     * @param arg2 Argument list
     * @return SemanticStatus, which may be null
     */
    private SemanticStatus argumentsMatch(final String arg1, final String arg2) {

        if (arg1.startsWith(ENCODE_PREFIX)) {
            if (arg2.startsWith(FUNC_PREFIX)) {
                return INVALID_RETURN_TYPE_ARGUMENT;
            }
            if (!arg2.startsWith(ENCODE_PREFIX)) {
                return INVALID_ENCODING_ARGUMENT;
            }

            final String[] tokens1 = PARAMS_REGEX.split(arg1);
            if (tokens1.length != 2) {
                return INVALID_ENCODING_ARGUMENT;
            }

            final String[] tokens2 = PARAMS_REGEX.split(arg2);
            if (tokens2.length != 2) {
                return INVALID_ENCODING_ARGUMENT;
            }

            // If either token is the wildcard, any encoding is valid.
            if (isWildcard(tokens1[1]) || isWildcard(tokens2[1])) {
                return VALID;
            }

            final ValueEncoding ve1 = getValueEncoding(tokens1[1]);
            ValueEncoding ve2 = getValueEncoding(tokens2[1]);
            if (ve2 != null) {
                if (ve1.isAssignableFrom(ve2)) {
                    return VALID;
                }
                return INVALID_ENCODING_ARGUMENT;
            }
            // Fallback to verification based on all character discriminators
            for (final Character c : tokens2[1].toCharArray()) {
                ve2 = getValueEncoding(c);
                if (ve1.isAssignableFrom(ve2)) {
                    return VALID;
                }
            }
            return INVALID_ENCODING_ARGUMENT;
        }

        if (arg1.startsWith(FUNC_PREFIX)) {
            if (arg2.startsWith(ENCODE_PREFIX))
                return INVALID_RETURN_TYPE_ARGUMENT;
            if (!arg2.startsWith(FUNC_PREFIX))
                return INVALID_RETURN_TYPE_ARGUMENT;

            final String[] tokens1 = PARAMS_REGEX.split(arg1);
            if (tokens1.length != 2)
                return INVALID_RETURN_TYPE_ARGUMENT;

            final String[] tokens2 = PARAMS_REGEX.split(arg2);
            if (tokens2.length != 2)
                return INVALID_RETURN_TYPE_ARGUMENT;

            final ReturnType re1 = ReturnType.getReturnType(tokens1[1]);
            final ReturnType re2 = ReturnType.getReturnType(tokens2[1]);

            if (re1 != re2 && !re1.isAssignableFrom(re2))
                return INVALID_RETURN_TYPE_ARGUMENT;
            return VALID;
        }

        return null;
    }

    /**
     * Encodes the supplied value encoding as
     * 
     * <pre>
     * E:<i>encoding</i>
     * </pre>
     * 
     * @param encoding String encoding
     * @return {@link String}
     */
    public static String encode(final String encoding) {
        return ENCODE_PREFIX.concat(encoding);
    }

    /**
     * Encodes the supplied return type encoding as
     * 
     * <pre>
     * F:{@link ReturnType#getDisplayValue() DISPLAY_VALUE}
     * </pre>
     * 
     * @param rt {@link ReturnType}
     * @return {@link String}
     */
    public static String encode(final ReturnType rt) {
        return FUNC_PREFIX.concat(rt.getDisplayValue());
    }

    /**
     * Returns the function enum for the provided string.
     * 
     * @param s String
     * @return {@link FunctionEnum}
     */
    private static FunctionEnum function(final String s) {
        String[] args = ARGS_REGEX.split(s);
        return getFunctionEnum(args[0]);
    }

    /**
     * Returns the return type for the provided string.
     * 
     * @param s String
     * @return {@link ReturnType}
     */
    private static ReturnType returnType(final String s) {
        String[] args = ARGS_REGEX.split(s);
        return ReturnType.getReturnType(args[args.length - 1]);
    }

    /**
     * Returns the root string of a varargs argument.
     * <p>
     * For example, for the string {@code F:myFunction...}, this method returns
     * {@code F:myFunction}.
     * </p>
     * 
     * @param s {@link String}
     * @return {@link String}
     */
    private static String varargsRoot(final String s) {
        return s.substring(0, s.length() - (VARARGS_SUFFIX.length()));
    }

    /**
     * Returns the number of arguments for the provided string.
     * 
     * @param s {@link String}
     * @return {@link String}
     */
    private static String countArgs(final String s) {
        String[] args = ARGS_REGEX.split(s);
        if ("".equals(args[1])) return "0";

        String[] argarray = args[1].split(",");
        for (final String arg : argarray)
            if (arg.contains(VARARGS_SUFFIX)) return "1 or more";
        return valueOf(argarray.length);
    }

    /**
     * Returns {@code true} if {@code s} is the wildcard encoding, {@code false}
     * otherwise.
     * 
     * @param s {@link String}
     * @return boolean
     */
    private static boolean isWildcard(final String s) {
        return WILDCARD_ENCODING.equals(s);
    }
}
