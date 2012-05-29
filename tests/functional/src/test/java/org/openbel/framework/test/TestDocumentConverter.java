package org.openbel.framework.test;

import static org.openbel.framework.common.BELUtilities.asPath;
import static javax.xml.xpath.XPathConstants.NODESET;
import static junit.framework.Assert.assertTrue;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.custommonkey.xmlunit.ElementQualifier;
import org.custommonkey.xmlunit.Transform;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.openbel.framework.tools.DocumentConverter;

public class TestDocumentConverter {

    private static final String BEL_NAMESPACE_URI = "http://belframework.org/schema/1.0/xbel";

    private static File corpusDir, cwd, nornalizeXbelXslt;

    @BeforeClass
    public static void setup() throws URISyntaxException {
        corpusDir = new File(asPath("..", "docs", "xbel", "corpus"));
        assertTrue(corpusDir.isDirectory());

        cwd = new File(System.getProperty("user.dir"));
        assertTrue(cwd.isDirectory());

        nornalizeXbelXslt = new File(TestDocumentConverter.class.getClassLoader()
                .getResource("normalize-xbel.xslt").toURI());
    }

    /*
     * Checks that Document Converter's conversion of docs/xbel/corpus/small_corpus.bel to
     * XBEL format is similar to docs/xbel/corpus/small_corpus.xbel.
     */
    @Test
    public void testThatConvertedSmallCorpusIsCorrect() throws IOException, SAXException,
            TransformerException, XPathExpressionException {

        final String msg = "testThatConvertedSmallCorpusIsCorrect";

        File testXbelFile = null;
        InputStream controlInputStream = null, testInputStream = null;
        boolean deleteTemp = false;
        try {
            final File smallCorpusBelFile = new File(corpusDir, "small_corpus.bel");
            assertTrue(smallCorpusBelFile.isFile());

            final File controlXbelFile = new File(corpusDir, "small_corpus.xbel");
            assertTrue(controlXbelFile.isFile());

            testXbelFile = File.createTempFile("test", ".xbel", cwd);
            assertTrue(testXbelFile.isFile());

            // Convert small_corpus.bel to XBEL format
            final String[] args = new String[] {
                    smallCorpusBelFile.getAbsolutePath(),
                    "-t", "BEL",
                    "-o", testXbelFile.getAbsolutePath(), "--no-preserve",
                    "--verbose", "--debug"
            };
            System.out.println("Running DocumentConverter with arguments " + Arrays.toString(args));
            DocumentConverter.main(args);

            controlInputStream = new FileInputStream(controlXbelFile);
            testInputStream = new FileInputStream(testXbelFile);

            final XPathFactory factory = XPathFactory.newInstance();
            final XPathMemo xpath = new XPathMemo(factory.newXPath());
            final Document control = transformControl(xpath, controlInputStream);
            final Document test = transformTest(testInputStream);

            final Diff diff = new Diff(control, test,
                    null, new XbelQualifier());

            assertXMLEqual(msg, diff, true);
            deleteTemp = true;

        } finally {
            if (controlInputStream != null) {
                try {
                    controlInputStream.close();
                } catch (IOException ex) {}
            }
            if (testInputStream != null) {
                try {
                    testInputStream.close();
                } catch (IOException ex) {}
            }
            if (testXbelFile != null && deleteTemp) {
                testXbelFile.delete();
            }
        }
    }

    /*
     * Checks that Document Converter, when run twice (i.e. XBEL -> BEL -> XBEL), outputs a
     * a similar XBEL document to the original XBEL.  The check is performed for:
     * docs/xbel/corpus/{small,tiny,micro}_corpus.xbel.
     */
    @Test
    public void testThatXbelToBelToXbelIsIdentity() throws IOException, SAXException, TransformerException,
            XPathExpressionException {

        final String msg = "testThatXbelToBelToXbelIsIdentity";
        final XPathFactory factory = XPathFactory.newInstance();
        final XPathMemo xpath = new XPathMemo(factory.newXPath());

        final File smallCorpusFile = new File(corpusDir, "small_corpus.xbel");
        assertTrue(smallCorpusFile.isFile());
        testThatXbelToBelToXbelIsIdentity(xpath, smallCorpusFile, msg + "(small_corpus.xbel)");

        final File tinyCorpusFile = new File(corpusDir, "tiny_corpus.xbel");
        assertTrue(tinyCorpusFile.isFile());
        testThatXbelToBelToXbelIsIdentity(xpath, tinyCorpusFile, msg + "(tiny_corpus.xbel)");

        final File microCorpusFile = new File(corpusDir, "micro_corpus.xbel");
        assertTrue(microCorpusFile.isFile());
        testThatXbelToBelToXbelIsIdentity(xpath, microCorpusFile, msg + "(micro_corpus.xbel)");
    }

    private void testThatXbelToBelToXbelIsIdentity(final XPathMemo xpath, final File controlXbelFile,
            final String msg)
                    throws IOException, SAXException, TransformerException, XPathExpressionException {

        File testBelFile = null, testXbelFile = null;
        InputStream controlInputStream = null, testInputStream = null;
        boolean deleteTemp = false;
        try {
            // Convert from .xbel to .bel
            testBelFile = File.createTempFile("test", ".bel", cwd);
            assertTrue(testBelFile.isFile());
            final String[] args1 = new String[] {
                    controlXbelFile.getAbsolutePath(),
                    "-t", "XBEL",
                    "-o", testBelFile.getAbsolutePath(), "--no-preserve",
                    "--verbose", "--debug"
            };
            System.out.println("Running DocumentConverter with arguments " + Arrays.toString(args1));
            DocumentConverter.main(args1);

            // Convert from .bel to .xbel
            testXbelFile = File.createTempFile("test", ".xbel", cwd);
            assertTrue(testXbelFile.isFile());
            final String[] args2 = new String[] {
                    testBelFile.getAbsolutePath(),
                    "-t", "BEL",
                    "-o", testXbelFile.getAbsolutePath(), "--no-preserve",
                    "--verbose", "--debug"
            };
            System.out.println("Running DocumentConverter with arguments " + Arrays.toString(args2));
            DocumentConverter.main(args2);

            controlInputStream = new FileInputStream(controlXbelFile);
            testInputStream = new FileInputStream(testXbelFile);

            final Document control = transformControl(xpath, controlInputStream);
            final Document test = transformTest(testInputStream);

            final Diff diff = new Diff(control, test, null,
                    new XbelQualifier());

            assertXMLEqual(msg, diff, true);
            deleteTemp = true;

        } finally {
            if (controlInputStream != null) {
                try {
                    controlInputStream.close();
                } catch (IOException ex) {}
            }
            if (testInputStream != null) {
                try {
                    testInputStream.close();
                } catch (IOException ex) {}
            }
            if (testXbelFile != null && deleteTemp) {
                testXbelFile.delete();
            }
            if (testBelFile != null && deleteTemp) {
                testBelFile.delete();
            }
        }
    }

    /*
     * The next two methods apply transformations of the control
     * (one of docs/xbel/corpus/*.xbel) and test (output of Document Converter)
     * documents, respectively.  This is necessary to put both documents in a common form
     * so that they can be compared more easily.
     */

    private static Document transformControl(final XPathMemo xpath, final InputStream is)
            throws IOException, SAXException, TransformerException, XPathExpressionException {

        final Document d = XMLUnit.buildControlDocument(new InputSource(is));
        mergeAnnotationGroupsIntoLeaves(xpath, d);
        return new Transform(d, nornalizeXbelXslt).getResultDocument();
    }

    private static Document transformTest(final InputStream is)
            throws IOException, SAXException, TransformerException, XPathExpressionException {

        final Document d = XMLUnit.buildTestDocument(new InputSource(is));
        return new Transform(d, nornalizeXbelXslt).getResultDocument();
    }

    /*
     * For each bel:statementGroup element, merge its bel:annotationGroup children into the
     * bel:annotationGroup children of any descendant bel:statement element.
     */
    private static void mergeAnnotationGroupsIntoLeaves(final XPathMemo xpath, final Document d)
            throws XPathExpressionException {

        xpath.getXPath().setNamespaceContext(new BelNamespaceContext());


        final XPathExpression statementExp = xpath.compile(
                "/bel:document//bel:statementGroup/bel:statement");

        final XPathExpression annotationGroupExp = xpath.compile("bel:annotationGroup");

        for (Node statement : new NodeListIterable((NodeList) statementExp.evaluate(d, NODESET))) {

            final NodeList annotationGroups = (NodeList) annotationGroupExp.evaluate(statement, NODESET);
            if (annotationGroups.getLength() == 0) {
                final Element annotationGroup = d.createElementNS(BEL_NAMESPACE_URI, "annotationGroup");
                statement.appendChild(annotationGroup);
                mergeAncestorAnnotationGroups(xpath, (Element) statement, annotationGroup);
            } else {
                for (Node annotationGroup : new NodeListIterable(annotationGroups)) {
                    mergeAncestorAnnotationGroups(
                            xpath, (Element) statement, (Element) annotationGroup);
                }
            }
        }
    }

    private static final void mergeAncestorAnnotationGroups(
            final XPathMemo xpath,
            final Element statement,
            final Element annotationGroup) throws XPathExpressionException {

        final XPathExpression annotationGroupExp = xpath.compile("bel:annotationGroup");

        Node node = statement;
        while ((node = node.getParentNode()) != null &&
                ! (node.getNodeName().equals("document") &&
                        node.getNamespaceURI().equals(BEL_NAMESPACE_URI))) {

            for (Node el : new NodeListIterable((NodeList) annotationGroupExp.evaluate(node, NODESET))) {
                mergeInto(el, annotationGroup);
            }
        }
    }

    private static final void mergeInto(final Node from, final Node to) {

        for (Node node : new NodeListIterable(from.getChildNodes())) {
            to.appendChild(node.cloneNode(true));
        }
    }

    /*
     * Declares to the XSLT transform that it should use "bel" as
     * the namespace prefix for the BEL namespace URI.
     */
    private static class BelNamespaceContext implements NamespaceContext {

        @Override
        public String getNamespaceURI(String prefix) {
            if ("bel".equals(prefix)) {
                return BEL_NAMESPACE_URI;
            }
            return null;
        }

        @Override
        public String getPrefix(String namespace) {
            if (BEL_NAMESPACE_URI.equals(namespace)) {
                return "bel";
            }
            return null;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Iterator getPrefixes(String namespace) {
            return null;
        }
    }

    /*
     * A wrapper around NodeList that allows it to be iterated over by a <code>foreach</code>
     * loop.
     */
    private static final class NodeListIterable implements Iterable<Node>, Iterator<Node> {
        private final NodeList nodeList;
        private final int nodeListSize;
        private int index;

        public NodeListIterable(final NodeList nodeList) {
            this.nodeList = nodeList;
            this.nodeListSize = nodeList.getLength();
            this.index = 0;
        }

        @Override
        public Iterator<Node> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return index < nodeListSize;
        }

        @Override
        public Node next() {
            return nodeList.item(index++);
        }

        @Override
        public void remove() {}
    }

    /*
     * A wrapper around <code>XPath</code> with a cache of compiled <code>XPathExpression</code>s.
     */
    private static final class XPathMemo {
        private XPath xpath;
        private Map<String, XPathExpression> memo;

        public XPathMemo(final XPath xpath) {
            this.xpath = xpath;
            this.memo = new HashMap<String, XPathExpression>();
        }

        public XPathExpression compile(final String expression) throws XPathExpressionException {
            if (memo.containsKey(expression)) {
                return memo.get(expression);
            }
            final XPathExpression exp = xpath.compile(expression);
            memo.put(expression, exp);
            return exp;
        }

        public XPath getXPath() {
            return xpath;
        }
    }

    /*
     * An xmlunit ElementQualifier used to determine whether XML nodes are comparable.
     */
    private static class XbelQualifier implements ElementQualifier {

        private static final ElementNameAndAttributeQualifier NAME_ATTR_QUAL
                = new ElementNameAndAttributeQualifier();

        @Override
        public boolean qualifyForComparison(final Element control, final Element test) {

            // qualifyForComparison() checks that the control and test elements have
            // the same structure (the same element names and attributes, the same
            // number of child nodes, and the corresponding child nodes have the same
            // structure).

            if (! NAME_ATTR_QUAL.qualifyForComparison(control, test)) {
                return false;
            } else if (! (control.hasChildNodes() && test.hasChildNodes())) {
                return (control.hasChildNodes() == test.hasChildNodes());
            }

            final Iterator<Node> controlChildren = new NodeListIterable(control.getChildNodes()),
                    testChildren = new NodeListIterable(test.getChildNodes());

            // StringBuilders to store the accumulated content of text/cdata section nodes.
            final StringBuilder controlTextBldr = new StringBuilder(),
                    testTextBldr = new StringBuilder();

            // State used to control the loop:
            boolean lastControlChildWasText = false, lastTestChildWasText = false;
            boolean wantNextControlChild = true;
            Node controlChild = null, testChild = null;

            // Simultaneously loop through the children of the control and test elements.
            // Check for anything that would prevent the control and test children from
            // qualifying for comparison.  The check for child element nodes is to run
            // qualifyForComparison() recursively on the children.
            for (;;) {
                if (wantNextControlChild) {
                    controlChild = controlChildren.next();

                    if (controlChild == null) {
                        wantNextControlChild = false;
                    } else {
                        final short nodeType = controlChild.getNodeType();
                        if (nodeType == Node.TEXT_NODE || nodeType == Node.CDATA_SECTION_NODE) {
                            // Collect the data in consecutive text and cdata sections
                            final String value = ((CharacterData) controlChild).getData();
                            if (value != null) {
                                controlTextBldr.append(value.trim());
                            }
                            lastControlChildWasText = true;

                        } else if (nodeType == Node.ELEMENT_NODE) {
                            wantNextControlChild = false;
                        }
                    }
                } else {
                    testChild = testChildren.next();

                    if (testChild == null) {
                        if (controlChild != null) {
                            return false;
                        } else if (lastControlChildWasText != lastTestChildWasText) {
                            return false;
                        } else if (lastTestChildWasText &&
                                ! controlTextBldr.toString().equals(testTextBldr.toString())) {
                            return false;
                        }

                        // If there are no more control or test child nodes then
                        // control and test qualify for comparison, so return true.
                        return true;
                    }
                    final short nodeType = testChild.getNodeType();
                    if (nodeType == Node.TEXT_NODE || nodeType == Node.CDATA_SECTION_NODE) {
                        // Collect the data in consecutive text and cdata sections
                        final String value = ((CharacterData) testChild).getData();
                        if (value != null) {
                            testTextBldr.append(value.trim());
                        }
                        lastTestChildWasText = true;
                    } else if (nodeType == Node.ELEMENT_NODE) {

                        if (lastControlChildWasText != lastTestChildWasText) {
                            return false;
                        } else if (lastTestChildWasText) {
                            if (! controlTextBldr.toString().equals(testTextBldr.toString())) {
                                return false;
                            }

                            controlTextBldr.delete(0, controlTextBldr.length());
                            testTextBldr.delete(0, testTextBldr.length());
                            lastTestChildWasText = false;
                        }

                        if (! qualifyForComparison((Element) controlChild, (Element) testChild)) {
                            return false;
                        }

                        wantNextControlChild = true;
                        lastControlChildWasText = false;
                    }
                }
            }
        }
    }
}
