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
package org.openbel.bel.xbel.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openbel.bel.xbel.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Evidence_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "evidence");
    private final static QName _Disclaimer_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "disclaimer");
    private final static QName _PatternAnnotation_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "patternAnnotation");
    private final static QName _Version_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "version");
    private final static QName _Copyright_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "copyright");
    private final static QName _Name_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "name");
    private final static QName _Description_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "description");
    private final static QName _ContactInfo_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "contactInfo");
    private final static QName _ListValue_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "listValue");
    private final static QName _Author_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "author");
    private final static QName _Comment_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "comment");
    private final static QName _License_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "license");
    private final static QName _Usage_QNAME = new QName(
            "http://belframework.org/schema/1.0/xbel", "usage");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openbel.bel.xbel.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XBELParameter }
     * 
     */
    public XBELParameter createXBELParameter() {
        return new XBELParameter();
    }

    /**
     * Create an instance of {@link XBELStatement }
     * 
     */
    public XBELStatement createXBELStatement() {
        return new XBELStatement();
    }

    /**
     * Create an instance of {@link XBELLicenseGroup }
     * 
     */
    public XBELLicenseGroup createXBELLicenseGroup() {
        return new XBELLicenseGroup();
    }

    /**
     * Create an instance of {@link XBELInternalAnnotationDefinition }
     * 
     */
    public XBELInternalAnnotationDefinition
            createXBELInternalAnnotationDefinition() {
        return new XBELInternalAnnotationDefinition();
    }

    /**
     * Create an instance of {@link XBELHeader }
     * 
     */
    public XBELHeader createXBELHeader() {
        return new XBELHeader();
    }

    /**
     * Create an instance of {@link XBELSubject }
     * 
     */
    public XBELSubject createXBELSubject() {
        return new XBELSubject();
    }

    /**
     * Create an instance of {@link XBELCitation.AuthorGroup }
     * 
     */
    public XBELCitation.AuthorGroup createXBELCitationAuthorGroup() {
        return new XBELCitation.AuthorGroup();
    }

    /**
     * Create an instance of {@link XBELObject }
     * 
     */
    public XBELObject createXBELObject() {
        return new XBELObject();
    }

    /**
     * Create an instance of {@link XBELTerm }
     * 
     */
    public XBELTerm createXBELTerm() {
        return new XBELTerm();
    }

    /**
     * Create an instance of {@link XBELAnnotationGroup }
     * 
     */
    public XBELAnnotationGroup createXBELAnnotationGroup() {
        return new XBELAnnotationGroup();
    }

    /**
     * Create an instance of {@link XBELStatementGroup }
     * 
     */
    public XBELStatementGroup createXBELStatementGroup() {
        return new XBELStatementGroup();
    }

    /**
     * Create an instance of {@link XBELDocument }
     * 
     */
    public XBELDocument createXBELDocument() {
        return new XBELDocument();
    }

    /**
     * Create an instance of {@link XBELExternalAnnotationDefinition }
     * 
     */
    public XBELExternalAnnotationDefinition
            createXBELExternalAnnotationDefinition() {
        return new XBELExternalAnnotationDefinition();
    }

    /**
     * Create an instance of {@link XBELAnnotation }
     * 
     */
    public XBELAnnotation createXBELAnnotation() {
        return new XBELAnnotation();
    }

    /**
     * Create an instance of {@link XBELNamespaceGroup }
     * 
     */
    public XBELNamespaceGroup createXBELNamespaceGroup() {
        return new XBELNamespaceGroup();
    }

    /**
     * Create an instance of {@link XBELAuthorGroup }
     * 
     */
    public XBELAuthorGroup createXBELAuthorGroup() {
        return new XBELAuthorGroup();
    }

    /**
     * Create an instance of {@link XBELNamespace }
     * 
     */
    public XBELNamespace createXBELNamespace() {
        return new XBELNamespace();
    }

    /**
     * Create an instance of {@link XBELCitation }
     * 
     */
    public XBELCitation createXBELCitation() {
        return new XBELCitation();
    }

    /**
     * Create an instance of {@link XBELAnnotationDefinitionGroup }
     * 
     */
    public XBELAnnotationDefinitionGroup createXBELAnnotationDefinitionGroup() {
        return new XBELAnnotationDefinitionGroup();
    }

    /**
     * Create an instance of {@link XBELListAnnotation }
     * 
     */
    public XBELListAnnotation createXBELListAnnotation() {
        return new XBELListAnnotation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "evidence")
    public
            JAXBElement<String> createEvidence(String value) {
        return new JAXBElement<String>(_Evidence_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "disclaimer")
    public
            JAXBElement<String> createDisclaimer(String value) {
        return new JAXBElement<String>(_Disclaimer_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "patternAnnotation")
    public
            JAXBElement<String> createPatternAnnotation(String value) {
        return new JAXBElement<String>(_PatternAnnotation_QNAME, String.class,
                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "version")
    public
            JAXBElement<String> createVersion(String value) {
        return new JAXBElement<String>(_Version_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "copyright")
    public
            JAXBElement<String> createCopyright(String value) {
        return new JAXBElement<String>(_Copyright_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "name")
    public
            JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "description")
    public
            JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "contactInfo")
    public
            JAXBElement<String> createContactInfo(String value) {
        return new JAXBElement<String>(_ContactInfo_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "listValue")
    public
            JAXBElement<String> createListValue(String value) {
        return new JAXBElement<String>(_ListValue_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "author")
    public
            JAXBElement<String> createAuthor(String value) {
        return new JAXBElement<String>(_Author_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "comment")
    public
            JAXBElement<String> createComment(String value) {
        return new JAXBElement<String>(_Comment_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "license")
    public
            JAXBElement<String> createLicense(String value) {
        return new JAXBElement<String>(_License_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://belframework.org/schema/1.0/xbel", name = "usage")
    public
            JAXBElement<String> createUsage(String value) {
        return new JAXBElement<String>(_Usage_QNAME, String.class, null, value);
    }

}
