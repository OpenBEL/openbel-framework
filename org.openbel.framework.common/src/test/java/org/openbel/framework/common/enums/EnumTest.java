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
package org.openbel.framework.common.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.openbel.framework.common.lang.Function;

/**
 * BEL common enum test cases.
 */
public class EnumTest {

    /**
     * Test all exit code enum values are unique.
     */
    @Test
    public void testExitCodeValues() {
        ExitCode[] codes = ExitCode.values();
        Set<Integer> set = new HashSet<Integer>();
        for (ExitCode e : codes) {
            set.add(e.getValue());
        }
        int expected = codes.length;
        int actual = set.size();
        assertEquals("values not distinct", expected, actual);
    }

    /**
     * Test all annotation type enum values are unique.
     */
    @Test
    public void testAnnotationTypeValues() {
        AnnotationType[] annos = AnnotationType.values();
        Set<Integer> set1 = new HashSet<Integer>();
        Set<String> set2 = new HashSet<String>();
        for (AnnotationType anno : annos) {
            set1.add(anno.getValue());
            set2.add(anno.getDisplayValue());
        }
        int expected = annos.length;
        int actual = set1.size();
        assertEquals("values not distinct", expected, actual);
        actual = set2.size();
        assertEquals("display values not distinct", expected, actual);
    }

    /**
     * Test all citation type enum values are unique.
     */
    @Test
    public void testCitationTypeValues() {
        CitationType[] cites = CitationType.values();
        Set<Integer> set1 = new HashSet<Integer>();
        Set<String> set2 = new HashSet<String>();
        for (CitationType cite : cites) {
            set1.add(cite.getValue());
            set2.add(cite.getDisplayValue());
        }
        int actual = cites.length;
        int expected = set1.size();
        assertEquals("values not distinct", expected, actual);
        expected = set2.size();
        assertEquals("display values not distinct", expected, actual);
    }

    /**
     * Test all function enum values are unique.
     */
    @Test
    public void testFunctionEnumValues() {
        FunctionEnum[] funcs = FunctionEnum.values();
        Set<Integer> set1 = new HashSet<Integer>();
        Set<String> set2 = new HashSet<String>();
        for (FunctionEnum func : funcs) {
            set1.add(func.getValue());
            set2.add(func.getDisplayValue());
        }
        int actual = funcs.length;
        int expected = set1.size();
        assertEquals("values not distinct", expected, actual);
        expected = set2.size();
        assertEquals("display values not distinct", expected, actual);
    }

    /**
     * Test all function enums have unique function classes.
     */
    @Test
    public void testFunctionEnumFunctions() {
        FunctionEnum[] funcs = FunctionEnum.values();
        Set<Class<?>> set1 = new HashSet<Class<?>>();
        for (FunctionEnum func : funcs) {
            Function fx = func.getFunction();
            assertNotNull(func + " does not have a function", fx);
            set1.add(fx.getClass());
        }
        int actual = funcs.length;
        int expected = set1.size();
        assertEquals("incorrect number of functions", expected, actual);
    }

    /**
     * Test all function enum abbreviations are unique.
     */
    @Test
    public void testFunctionEnumAbbreviations() {
        FunctionEnum[] funcs = FunctionEnum.values();
        Set<String> abbrs = new HashSet<String>();
        for (FunctionEnum func : funcs) {
            String abbr = func.getAbbreviation();
            if (abbr == null) {
                continue;
            }
            if (abbrs.contains(abbr)) {
                fail(func + " uses existing abbreviation: " + abbr);
            }
            abbrs.add(abbr);
        }
    }

    /**
     * Test all relationship type enum values are unique.
     */
    @Test
    public void testRelationshipValues() {
        RelationshipType[] rels = RelationshipType.values();
        Set<Integer> set1 = new HashSet<Integer>();
        Set<String> set2 = new HashSet<String>();
        for (RelationshipType rel : rels) {
            set1.add(rel.getValue());
            set2.add(rel.getDisplayValue());
        }
        int actual = rels.length;
        int expected = set1.size();
        assertEquals("values not distinct", expected, actual);
        expected = set2.size();
        assertEquals("display values not distinct", expected, actual);
    }

    /**
     * Test all relationship type enum abbreviations are unique.
     */
    @Test
    public void testRelationshipAbbreviations() {
        RelationshipType[] rels = RelationshipType.values();
        Set<String> abbrs = new HashSet<String>();
        for (RelationshipType rel : rels) {
            String abbr = rel.getAbbreviation();
            if (abbr == null) {
                continue;
            }
            if (abbrs.contains(abbr)) {
                fail(rel + " uses existing abbreviation: " + abbr);
            }
            abbrs.add(abbr);
        }
    }

    /**
     * Test all return type enum values are unique.
     */
    @Test
    public void testReturnTypeValues() {
        ReturnType[] rts = ReturnType.values();
        Set<Integer> set1 = new HashSet<Integer>();
        Set<String> set2 = new HashSet<String>();
        for (ReturnType rt : rts) {
            set1.add(rt.getValue());
            set2.add(rt.getDisplayValue());
        }
        int actual = rts.length;
        int expected = set1.size();
        assertEquals("values not distinct", expected, actual);
        expected = set2.size();
        assertEquals("display values not distinct", expected, actual);
    }

    /**
     * Test all return type enums return valid booleans against
     * isAssignableFrom.
     */
    @Test
    public void testReturnTypeAssignables() {
        ReturnType[] rts = ReturnType.values();
        for (ReturnType rt : rts) {
            try {
                if (rt.isAssignableFrom(rt)) {

                }
            } catch (UnsupportedOperationException e) {
                fail(rt + " not handled by ReturnType enum isAssignableFrom");
            }
        }
    }

    /**
     * Test all semantic status enum values are unique.
     */
    @Test
    public void testSemanticStatusValues() {
        SemanticStatus[] status = SemanticStatus.values();
        Set<Integer> set1 = new HashSet<Integer>();
        Set<String> set2 = new HashSet<String>();
        for (SemanticStatus stat : status) {
            set1.add(stat.getValue());
            set2.add(stat.getDisplayValue());
        }
        int actual = status.length;
        int expected = set1.size();
        assertEquals("values not distinct", expected, actual);
        expected = set2.size();
        assertEquals("display values not distinct", expected, actual);
    }

    /**
     * Test all database type enum values are unique.
     */
    @Test
    public void testDatabaseTypeValues() {
        DatabaseType[] dbs = DatabaseType.values();
        Set<Integer> set1 = new HashSet<Integer>();
        Set<String> set2 = new HashSet<String>();
        for (DatabaseType db : dbs) {
            set1.add(db.getValue());
            set2.add(db.getDisplayValue());
        }
        int actual = dbs.length;
        int expected = set1.size();
        assertEquals("values not distinct", expected, actual);
        expected = set2.size();
        assertEquals("display values not distinct", expected, actual);
    }

    /**
     * Test all value encoding values are unique.
     */
    @Test
    public void testValueEncodingValues() {
        ValueEncoding[] ves = ValueEncoding.values();
        Set<Character> set1 = new HashSet<Character>();
        Set<String> set2 = new HashSet<String>();
        for (ValueEncoding ve : ves) {
            set1.add(ve.getValue());
            set2.add(ve.getDisplayValue());
        }
        int expected = ves.length;
        int actual = set1.size();
        assertEquals("values not distinct", expected, actual);
        actual = set2.size();
        assertEquals("display values not distinct", expected, actual);
    }

    /**
     * Test all amino acid value properties are unique.
     */
    @Test
    public void testAminoAcids() {
        AminoAcid[] aas = AminoAcid.values();
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        Set<String> set3 = new HashSet<String>();
        Set<Integer> set4 = new HashSet<Integer>();
        for (AminoAcid aa : aas) {
            set1.add(aa.getDisplayValue());
            set2.add(aa.getOneLetter());
            set3.add(aa.getThreeLetter());
            set4.add(aa.getValue());
        }
        int expected = aas.length;
        int actual = set1.size();
        assertEquals("display values not distinct", expected, actual);
        actual = set2.size();
        assertEquals("one-letter values not distinct", expected, actual);
        actual = set3.size();
        assertEquals("three-letter values not distinct", expected, actual);
        actual = set4.size();
        assertEquals("values not distinct", expected, actual);
    }

    /**
     * Test all covalent modification value property are unique.
     */
    @Test
    public void testCovalentModification() {
        CovalentModification[] cms = CovalentModification.values();
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        Set<Integer> set3 = new HashSet<Integer>();
        for (CovalentModification cm : cms) {
            set1.add(cm.getDisplayValue());
            set2.add(cm.getOneLetter());
            set3.add(cm.getValue());
        }
        int expected = cms.length;
        int actual = set1.size();
        assertEquals("display values not distinct", expected, actual);
        actual = set2.size();
        assertEquals("one-letter values not distinct", expected, actual);
        actual = set3.size();
        assertEquals("values not distinct", expected, actual);
    }
}
