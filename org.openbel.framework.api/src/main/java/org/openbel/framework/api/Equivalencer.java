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
package org.openbel.framework.api;

import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.common.BELUtilities.sizedHashMap;

import java.util.List;
import java.util.Map;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.core.equivalence.DefaultParameterEquivalencer;
import org.openbel.framework.core.equivalence.ParameterEquivalencer;

/**
 * Equivalencer defines a utility to equivalencing namespace values.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Equivalencer {
    private ParameterEquivalencer paramEquivalencer;

    /**
     * Finds a parameter, equivalent to the <tt>sourceParameter</tt>, and found
     * in the {@link Namespace} identified by <tt>targetNamespace</tt>.
     * <p>
     * Note: The first time any method in {@link Equivalencer} is called the
     * equivalencing engine is instantiated, but only once. The equivalencing
     * engine loads the BEL equivalence files associated with the framework and
     * uses them do perform equivalencing.
     * </p>
     *
     * @param sourceNamespace {@link Namespace}, the namespace of the source
     *            parameter
     * @param sourceValue {@link String}, the source parameter value
     * @param targetNamespace {@link Namespace}, the namespace of the equivalent
     *            parameter to find
     * @return the equivalent parameter found in <tt>targetNamespace</tt>, or
     *         null if no equivalent parameter is found in that namespace
     * @throw InvalidArgument Thrown if <tt>sourceNamespace</tt>,
     *        <tt>sourceParameter</tt>, or <tt>targetNamespace</tt> is null
     * @throws EquivalencerException Thrown if an exception occurs finding the
     *             equivalent parameter
     */
    public String equivalence(final Namespace sourceNamespace,
            final String sourceValue, final Namespace targetNamespace)
            throws EquivalencerException {

        if (sourceNamespace == null) {
            throw new InvalidArgument("sourceNamespace", sourceNamespace);
        }

        if (noLength(sourceValue)) {
            throw new InvalidArgument("sourceValue", sourceValue);
        }

        if (targetNamespace == null) {
            throw new InvalidArgument("targetNamespace", targetNamespace);
        }

        loadEquivalencingEngine();

        // convert namespace and value to parameter (common model).
        final Parameter sp = new Parameter(sourceNamespace, sourceValue);

        try {
            Parameter parameter = this.paramEquivalencer.findEquivalence(sp,
                    targetNamespace);
            return parameter == null ? null : parameter.getValue();
        } catch (InvalidArgument e) {
            //TODO change exception when paramEquivalencer is changed
            return null;
        } catch (Exception e) {
            final String fmt = "Unable to find equivalences for '%s'";
            final String msg = format(fmt, sp.toBELShortForm());
            throw new EquivalencerException(msg, e);
        }
    }

    /**
     * Finds all parameters equivalent to the <tt>sourceParameter</tt>.
     * <p>
     * Note: The first time any method in {@link Equivalencer} is called the
     * equivalencing engine is instantiated, but only once. The equivalencing
     * engine loads the BEL equivalence files associated with the framework and
     * uses them do perform equivalencing.
     * </p>
     *
     * @param sourceNamespace {@link Namespace}, the namespace of the source
     *            parameter
     * @param sourceValue {@link String}, the source parameter value
     * @return the equivalent parameters or an empty map if no equivalent
     *         parameter is found
     * @throws EquivalencerException Thrown if an exception occurs finding all
     *             equivalences
     */
    public Map<Namespace, String> equivalence(final Namespace sourceNamespace,
            final String sourceValue) throws EquivalencerException {
        if (sourceNamespace == null) {
            throw new InvalidArgument("sourceNamespace", sourceNamespace);
        }

        if (noLength(sourceValue)) {
            throw new InvalidArgument("sourceValue", sourceValue);
        }

        loadEquivalencingEngine();

        final Parameter sp = new Parameter(sourceNamespace, sourceValue);

        try {
            final List<Parameter> equivalences = this.paramEquivalencer
                    .findEquivalences(sp);
            Map<Namespace, String> equivalenceMap = sizedHashMap(equivalences
                    .size());

            for (final Parameter equivalence : equivalences) {
                equivalenceMap.put(equivalence.getNamespace(),
                        equivalence.getValue());
            }

            return equivalenceMap;
        } catch (InvalidArgument e) {
            //TODO change exception when paramEquivalencer is changed
            return null;
        } catch (Exception e) {
            final String fmt = "Unable to find equivalences for '%s'";
            final String msg = format(fmt, sp.toBELShortForm());
            throw new EquivalencerException(msg, e);
        }
    }

    /**
     * Finds a parameter, equivalent to the <tt>sourceUUID</tt>, and found in
     * the {@link Namespace} identified by <tt>targetNamespace</tt>.
     * <p>
     * Note: The first time any method in {@link Equivalencer} is called the
     * equivalencing engine is instantiated, but only once. The equivalencing
     * engine loads the BEL equivalence files associated with the framework and
     * uses them do perform equivalencing.
     * </p>
     *
     * @param sourceUUID
     * @param targetNamespace
     * @return the equivalent parameters or an empty map if no equivalent
     *         parameter is found
     * @throws EquivalencerException Thrown if an exception occurs finding all
     *             equivalences
     */
    public String equivalence(SkinnyUUID sourceUUID, Namespace targetNamespace)
            throws EquivalencerException {
        if (sourceUUID == null) {
            throw new InvalidArgument("sourceUUID", sourceUUID);
        }

        if (targetNamespace == null) {
            throw new InvalidArgument("targetNamespace", targetNamespace);
        }

        loadEquivalencingEngine();

        try {
            Parameter parameter = this.paramEquivalencer.findEquivalence(
                    sourceUUID, targetNamespace);
            return parameter == null ? null : parameter.getValue();
        } catch (InvalidArgument e) {
            //TODO change exception when paramEquivalencer is changed
            return null;
        } catch (Exception e) {
            final String fmt = "Unable to find equivalences for '%s'";
            final String msg = format(fmt, sourceUUID.toString());
            throw new EquivalencerException(msg, e);
        }
    }

    /**
     * Finds all parameters equivalent to the <tt>sourceParameter</tt>.
     * <p>
     * Note: The first time any method in {@link Equivalencer} is called the
     * equivalencing engine is instantiated, but only once. The equivalencing
     * engine loads the BEL equivalence files associated with the framework and
     * uses them do perform equivalencing.
     * </p>
     *
     * @param sourceUUID
     * @return the equivalent parameters or an empty map if no equivalent
     *         parameter is found
     * @throws EquivalencerException Thrown if an exception occurs finding all
     *             equivalences
     */
    public Map<Namespace, String> equivalence(SkinnyUUID sourceUUID)
            throws EquivalencerException {
        if (sourceUUID == null) {
            throw new InvalidArgument("sourceUUID", sourceUUID);
        }

        loadEquivalencingEngine();

        try {
            final List<Parameter> equivalences = this.paramEquivalencer
                    .findEquivalences(sourceUUID);
            Map<Namespace, String> equivalenceMap = sizedHashMap(equivalences
                    .size());

            for (final Parameter equivalence : equivalences) {
                equivalenceMap.put(equivalence.getNamespace(),
                        equivalence.getValue());
            }

            return equivalenceMap;
        } catch (InvalidArgument e) {
            //TODO change exception when paramEquivalencer is changed
            return null;
        } catch (Exception e) {
            final String fmt = "Unable to find equivalences for '%s'";
            final String msg = format(fmt, sourceUUID.toString());
            throw new EquivalencerException(msg, e);
        }
    }

    /**
     * Obtain the equivalence UUID for the given {@link Namespace namespace} and value.
     * @param namespace {@link Namespace namespace}, must not be null
     * @param value {@link String value}, must not be null
     * @return {@link SkinnyUUID UUID} representing the value or <code>null</code> if the
     * namespace/value combination has no equivalents. null is also returned if the
     * namespace has no equivalences defined.
     * @throws EquivalencerException Thrown if an exception occurs finding all
     *             equivalences
     */
    public SkinnyUUID getUUID(Namespace namespace, String value)
            throws EquivalencerException {
        loadEquivalencingEngine();
        final Parameter p = new Parameter(namespace, value);

        try {
            return paramEquivalencer.getUUID(p);
        } catch (InvalidArgument e) {
            //TODO change when paramEquivalencer exception is changed
            return null;
        } catch (Exception e) {
            final String fmt = "Unable to find UUID for '%s'";
            final String msg = format(fmt, p.toBELShortForm());
            throw new EquivalencerException(msg, e);
        }
    }

    /**
     * Loads the {@link ParameterEquivalencer} if it has not already been
     * loaded.
     *
     * @throws EquivalencerException Thrown if an error occurred loading the
     *             equivalencing engine.
     */
    private void loadEquivalencingEngine() throws EquivalencerException {
        if (paramEquivalencer == null) {
            try {
                paramEquivalencer = DefaultParameterEquivalencer.getInstance();
            } catch (Exception e) {
                throw new EquivalencerException(
                        "Unable to load equivalencing engine.", e);
            }
        }
    }
}
