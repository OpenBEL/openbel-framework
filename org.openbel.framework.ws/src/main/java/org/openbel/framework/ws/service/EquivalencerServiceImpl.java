/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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
package org.openbel.framework.ws.service;

import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.util.List;
import java.util.Map;

import org.openbel.framework.api.Equivalencer;
import org.openbel.framework.api.EquivalencerException;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.ws.model.EquivalenceId;
import org.openbel.framework.ws.model.Namespace;
import org.openbel.framework.ws.model.NamespaceValue;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.utils.ObjectFactorySingleton;
import org.springframework.stereotype.Service;

/**
 * Implements an equivalencer service that finds equivalences for namespace values.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
@Service
public class EquivalencerServiceImpl implements EquivalencerService {

    private static final ObjectFactory OBJECT_FACTORY = ObjectFactorySingleton
            .getInstance();
    private final Equivalencer equivalencer;

    public EquivalencerServiceImpl() {
        equivalencer = new Equivalencer();
    }

    public Equivalencer leak() {
        return equivalencer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamespaceValue findNamespaceEquivalence(
            NamespaceValue sourceNamespaceValue, Namespace targetNamespace)
            throws EquivalencerException {
        String equivalenceValue;
        SkinnyUUID uuid;
        if (sourceNamespaceValue.getEquivalence() != null) {
            uuid = convert(sourceNamespaceValue.getEquivalence());
            equivalenceValue =
                    equivalencer.equivalence(uuid, convert(targetNamespace));
        } else {
            final Namespace sourceNs = sourceNamespaceValue.getNamespace();
            final org.openbel.framework.common.model.Namespace sns =
                    convert(sourceNs);
            final String sourceValue = sourceNamespaceValue.getValue();
            equivalenceValue = equivalencer.equivalence(
                    convert(sourceNs),
                    sourceValue, convert(targetNamespace));
            uuid = equivalencer.getUUID(sns, sourceValue);
        }

        final NamespaceValue targetNsValue = new NamespaceValue();
        targetNsValue.setNamespace(targetNamespace);
        targetNsValue.setValue(equivalenceValue);

        // uuid based off of source
        targetNsValue.setEquivalence(convert(uuid));

        return targetNsValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NamespaceValue> findEquivalences(
            NamespaceValue sourceNamespaceValue) throws EquivalencerException {
        Map<org.openbel.framework.common.model.Namespace, String> equivalenceMap;
        SkinnyUUID uuid;
        if (sourceNamespaceValue.getEquivalence() != null) {
            uuid = convert(sourceNamespaceValue.getEquivalence());
            equivalenceMap = equivalencer.equivalence(uuid);
        } else {
            final Namespace sourceNs = sourceNamespaceValue.getNamespace();
            final String sourceValue = sourceNamespaceValue.getValue();
            final org.openbel.framework.common.model.Namespace sns =
                    convert(sourceNs);
            equivalenceMap = equivalencer.equivalence(sns, sourceValue);
            uuid = equivalencer.getUUID(sns, sourceValue);
        }

        EquivalenceId eq = convert(uuid);
        final List<NamespaceValue> equivalentNsValues =
                sizedArrayList(equivalenceMap
                        .size());
        for (Map.Entry<org.openbel.framework.common.model.Namespace, String> equivalence : equivalenceMap
                .entrySet()) {
            final NamespaceValue equivalentNsValue = new NamespaceValue();
            equivalentNsValue.setNamespace(convert(equivalence.getKey()));
            equivalentNsValue.setValue(equivalence.getValue());
            equivalentNsValue.setEquivalence(eq);
            equivalentNsValues.add(equivalentNsValue);
        }

        return equivalentNsValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EquivalenceId getEquivalenceId(final NamespaceValue nv)
            throws EquivalencerException {
        SkinnyUUID uuid =
                equivalencer.getUUID(convert(nv.getNamespace()), nv.getValue());
        return convert(uuid);
    }

    private org.openbel.framework.common.model.Namespace convert(
            final Namespace ns) {
        final String prefix = ns.getPrefix();
        final String resourceLocation = ns.getResourceLocation();
        return new org.openbel.framework.common.model.Namespace(prefix,
                resourceLocation);
    }

    private Namespace convert(
            final org.openbel.framework.common.model.Namespace ns) {
        final Namespace wsNs = new Namespace();
        wsNs.setPrefix(ns.getPrefix());
        wsNs.setResourceLocation(ns.getResourceLocation());
        return wsNs;
    }

    private SkinnyUUID convert(final EquivalenceId equiv) {
        return new SkinnyUUID(equiv.getMsb(), equiv.getLsb());
    }

    private EquivalenceId convert(final SkinnyUUID uuid) {
        if (uuid == null) {
            return null;
        }
        EquivalenceId eq = OBJECT_FACTORY.createEquivalenceId();
        eq.setMsb(uuid.getMostSignificantBits());
        eq.setLsb(uuid.getLeastSignificantBits());
        return eq;
    }
}
