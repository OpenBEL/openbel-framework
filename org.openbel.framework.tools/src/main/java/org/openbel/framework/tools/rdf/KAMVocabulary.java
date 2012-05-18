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
package org.openbel.framework.tools.rdf;

import static org.openbel.framework.common.BELUtilities.constrainedHashMap;

import java.util.Map;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Defines the KAM Vocabulary (RDF-based).
 * 
 * TODO Convert to Owl2-based Vocabulary.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class KAMVocabulary {
    /**
     * Holds {@link FunctionEnum} to RDF {@link Resource}
     */
    private static final Map<FunctionEnum, Resource> F_TO_R;

    /**
     * Holds {@link RelationshipType} to RDF {@link Resource}
     */
    private static final Map<RelationshipType, Resource> R_TO_R;

    /**
     * <p>
     * The RDF model that holds the vocabulary terms
     * </p>
     */
    private static Model m_model = ModelFactory.createDefaultModel();

    /**
     * <p>
     * The namespace of the vocabulary as a string
     * </p>
     */
    public static final String NS =
            "http://resource.belframework.org/belframework/1.0/schema/1.0/kam#";

    /**
     * <p>
     * The namespace of the vocabulary as a string
     * </p>
     * 
     * @see #NS
     */
    public static String getURI() {
        return NS;
    }

    /**
     * <p>
     * The namespace of the vocabulary as a resource
     * </p>
     */
    public static final Resource NAMESPACE = m_model.createResource(NS);

    public static final Property composedOf =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#composedOf");

    public static final Property definedBy =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#definedBy");

    public static final Property defines =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#defines");

    public static final Property hasAnnotation =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasAnnotation");

    public static final Property hasArguments =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasArguments");

    public static final Property hasAuthor =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasAuthor");

    public static final Property hasContactInfo =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasContactInfo");

    public static final Property hasCopyright =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasCopyright");

    public static final Property hasDescription =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasDescription");

    public static final Property hasDisclaimer =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasDisclaimer");

    public static final Property hasFunction =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasFunction");

    public static final Property hasId =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasId");

    public static final Property hasLabel =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasLabel");

    public static final Property hasLicense =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasLicense");

    public static final Property hasList =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasList");

    public static final Property hasName =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasName");

    public static final Property hasNamespace =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasNamespace");

    public static final Property hasObjectNode =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasObjectNode");

    public static final Property hasObjectTerm =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasObjectTerm");

    public static final Property hasPattern =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasPattern");

    public static final Property hasPrefix =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasPrefix");

    public static final Property hasRelationship =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasRelationship");

    public static final Property hasResourceLocation =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasResourceLocation");

    public static final Property hasSubjectNode =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasSubjectNode");

    public static final Property hasSubjectTerm =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasSubjectTerm");

    public static final Property hasType =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasType");

    public static final Property hasURL =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasURL");

    public static final Property hasUsage =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasUsage");

    public static final Property hasValue =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasValue");

    public static final Property hasVersion =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#hasVersion");

    public static final Property isRepresentedBy =
            m_model
                    .createProperty("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#isRepresentedBy");

    /**
     * <p>
     * An abundance in BEL.
     * </p>
     */
    public static final Resource Abundance =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Abundance");

    /**
     * <p>
     * A actsIn relationship in BEL.
     * </p>
     */
    public static final Resource ActsIn =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#ActsIn");

    /**
     * <p>
     * A analogous relationship in BEL.
     * </p>
     */
    public static final Resource Analogous =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Analogous");

    /**
     * <p>
     * An annotation.
     * </p>
     */
    public static final Resource Annotation =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Annotation");

    /**
     * <p>
     * An annotation definition.
     * </p>
     */
    public static final Resource AnnotationDefinition =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#AnnotationDefinition");

    /**
     * <p>
     * A association relationship in BEL.
     * </p>
     */
    public static final Resource Association =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Association");

    /**
     * <p>
     * A biological process in BEL.
     * </p>
     */
    public static final Resource BiologicalProcess =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#BiologicalProcess");

    /**
     * <p>
     * A biomarkerFor relationship in BEL.
     * </p>
     */
    public static final Resource BiomarkerFor =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#BiomarkerFor");

    /**
     * <p>
     * A catalytic activity in BEL.
     * </p>
     */
    public static final Resource CatalyticActivity =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#CatalyticActivity");

    /**
     * <p>
     * A causesNoChange relationship in BEL.
     * </p>
     */
    public static final Resource CausesNoChange =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#CausesNoChange");

    /**
     * <p>
     * A cell secretion in BEL.
     * </p>
     */
    public static final Resource CellSecretion =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#CellSecretion");

    /**
     * <p>
     * A cell surface expression in BEL.
     * </p>
     */
    public static final Resource CellSurfaceExpression =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#CellSurfaceExpression");

    /**
     * <p>
     * A chaperone activity in BEL.
     * </p>
     */
    public static final Resource ChaperoneActivity =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#ChaperoneActivity");

    /**
     * <p>
     * A complex abundance in BEL.
     * </p>
     */
    public static final Resource ComplexAbundance =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#ComplexAbundance");

    /**
     * <p>
     * A composite abundance in BEL.
     * </p>
     */
    public static final Resource CompositeAbundance =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#CompositeAbundance");

    /**
     * <p>
     * A decreases relationship in BEL.
     * </p>
     */
    public static final Resource Decreases =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Decreases");

    /**
     * <p>
     * A degredation in BEL.
     * </p>
     */
    public static final Resource Degradation =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Degradation");

    /**
     * <p>
     * A directlyDecreases relationship in BEL.
     * </p>
     */
    public static final Resource DirectlyDecreases =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#DirectlyDecreases");

    /**
     * <p>
     * A directlyIncreases relationship in BEL.
     * </p>
     */
    public static final Resource DirectlyIncreases =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#DirectlyIncreases");

    /**
     * <p>
     * A document that provides BEL knowledge that this KAM is composed of.
     * </p>
     */
    public static final Resource BelDocument =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#BelDocument");

    /**
     * <p>
     * A function in the KAM.
     * </p>
     */
    public static final Resource Function =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Function");

    /**
     * <p>
     * A fusion in BEL.
     * </p>
     */
    public static final Resource Fusion =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Fusion");

    /**
     * <p>
     * A gene abundance in BEL.
     * </p>
     */
    public static final Resource GeneAbundance =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#GeneAbundance");

    /**
     * <p>
     * A gtp bound activity in BEL.
     * </p>
     */
    public static final Resource GtpBoundActivity =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#GtpBoundActivity");

    /**
     * <p>
     * A hasComponent relationship in BEL.
     * </p>
     */
    public static final Resource HasComponent =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#HasComponent");

    /**
     * <p>
     * A hasComponents relationship in BEL.
     * </p>
     */
    public static final Resource HasComponents =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#HasComponents");

    /**
     * <p>
     * A hasMember relationship in BEL.
     * </p>
     */
    public static final Resource HasMember =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#HasMember");

    /**
     * <p>
     * A hasMembers relationship in BEL.
     * </p>
     */
    //public static final Resource HasMembers = m_model
    //        .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#HasMembers");

    /**
     * <p>
     * A hasModification relationship in BEL.
     * </p>
     */
    public static final Resource HasModification =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#HasModification");

    /**
     * <p>
     * A hasProduct relationship in BEL.
     * </p>
     */
    public static final Resource HasProduct =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#HasProduct");

    /**
     * <p>
     * A hasVariant relationship in BEL.
     * </p>
     */
    public static final Resource HasVariant =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#HasVariant");

    /**
     * <p>
     * A includes relationship in BEL.
     * </p>
     */
    public static final Resource Includes =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Includes");

    /**
     * <p>
     * A increases relationship in BEL.
     * </p>
     */
    public static final Resource Increases =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Increases");

    /**
     * <p>
     * A isA relationship in BEL.
     * </p>
     */
    public static final Resource IsA =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#IsA");

    /**
     * <p>
     * A KAM.
     * </p>
     */
    public static final Resource KAM =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#KAM");

    /**
     * <p>
     * An edge in the KAM.
     * </p>
     */
    public static final Resource KAMEdge =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#KAMEdge");

    /**
     * <p>
     * A node in the KAM.
     * </p>
     */
    public static final Resource KAMNode =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#KAMNode");

    /**
     * <p>
     * A kinase activity in BEL.
     * </p>
     */
    public static final Resource KinaseActivity =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#KinaseActivity");

    /**
     * <p>
     * A list in BEL.
     * </p>
     */
    //public static final Resource List = m_model
    //        .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#List");

    /**
     * <p>
     * A micro RNA abundance in BEL.
     * </p>
     */
    public static final Resource MicroRNAAbundance =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#MicroRNAAbundance");

    /**
     * <p>
     * A molecular activity in BEL.
     * </p>
     */
    public static final Resource MolecularActivity =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#MolecularActivity");

    /**
     * <p>
     * An namespace.
     * </p>
     */
    public static final Resource Namespace =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Namespace");

    /**
     * <p>
     * A negativeCorrelation relationship in BEL.
     * </p>
     */
    public static final Resource NegativeCorrelation =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#NegativeCorrelation");

    /**
     * <p>
     * A orthologous relationship in BEL.
     * </p>
     */
    public static final Resource Orthologous =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Orthologous");

    /**
     * <p>
     * A paremeter in the KAM.
     * </p>
     */
    public static final Resource Parameter =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Parameter");

    /**
     * <p>
     * A pathology in BEL.
     * </p>
     */
    public static final Resource Pathology =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Pathology");

    /**
     * <p>
     * A peptidase activity in BEL.
     * </p>
     */
    public static final Resource PeptidaseActivity =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#PeptidaseActivity");

    /**
     * <p>
     * A phosphatase activity in BEL.
     * </p>
     */
    public static final Resource PhosphataseActivity =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#PhosphataseActivity");

    /**
     * <p>
     * A positiveCorrelation relationship in BEL.
     * </p>
     */
    public static final Resource PositiveCorrelation =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#PositiveCorrelation");

    /**
     * <p>
     * A products in BEL.
     * </p>
     */
    public static final Resource Products =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Products");

    /**
     * <p>
     * A prognosticBiomarkerFor relationship in BEL.
     * </p>
     */
    public static final Resource PrognosticBiomarkerFor =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#PrognosticBiomarkerFor");

    /**
     * <p>
     * A protein abundance in BEL.
     * </p>
     */
    public static final Resource ProteinAbundance =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#ProteinAbundance");

    /**
     * <p>
     * A protein modification in BEL.
     * </p>
     */
    public static final Resource ProteinModification =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#ProteinModification");

    /**
     * <p>
     * A rateLimitingStepOf relationship in BEL.
     * </p>
     */
    public static final Resource RateLimitingStepOf =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#RateLimitingStepOf");

    /**
     * <p>
     * A reactantIn relationship in BEL.
     * </p>
     */
    public static final Resource ReactantIn =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#ReactantIn");

    /**
     * <p>
     * A reactants in BEL.
     * </p>
     */
    public static final Resource Reactants =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Reactants");

    /**
     * <p>
     * A reaction in BEL.
     * </p>
     */
    public static final Resource Reaction =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Reaction");

    /**
     * <p>
     * A relationship in the KAM.
     * </p>
     */
    public static final Resource Relationship =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Relationship");

    /**
     * <p>
     * A ribosylation activity in BEL.
     * </p>
     */
    public static final Resource RibosylationActivity =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#RibosylationActivity");

    /**
     * <p>
     * An RNA abundance in BEL.
     * </p>
     */
    public static final Resource RnaAbundance =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#RnaAbundance");

    /**
     * <p>
     * An edge in the KAM.
     * </p>
     */
    public static final Resource Statement =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Statement");

    /**
     * <p>
     * A subProcessOf relationship in BEL.
     * </p>
     */
    public static final Resource SubProcessOf =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#SubProcessOf");

    /**
     * <p>
     * A substitution in BEL.
     * </p>
     */
    public static final Resource Substitution =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Substitution");

    /**
     * <p>
     * A term in the KAM.
     * </p>
     */
    public static final Resource Term =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Term");

    /**
     * <p>
     * A transcribedTo relationship in BEL.
     * </p>
     */
    public static final Resource TranscribedTo =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#TranscribedTo");

    /**
     * <p>
     * A transcriptional activity in BEL.
     * </p>
     */
    public static final Resource TranscriptionalActivity =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#TranscriptionalActivity");

    /**
     * <p>
     * A translatedTo relationship in BEL.
     * </p>
     */
    public static final Resource TranslatedTo =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#TranslatedTo");

    /**
     * <p>
     * A translocates relationship in BEL.
     * </p>
     */
    public static final Resource Translocates =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Translocates");

    /**
     * <p>
     * A translocation in BEL.
     * </p>
     */
    public static final Resource Translocation =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Translocation");

    /**
     * <p>
     * A transport activity in BEL.
     * </p>
     */
    public static final Resource TransportActivity =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#TransportActivity");

    /**
     * <p>
     * A truncation in BEL.
     * </p>
     */
    public static final Resource Truncation =
            m_model
                    .createResource("http://resource.belframework.org/belframework/1.0/schema/1.0/kam#Truncation");

    /**
     * Retrieves the RDF {@link Resource} for the BEL {@link FunctionEnum}.
     * 
     * @param function
     *            {@link FunctionEnum}, the BEL function
     * @return {@link Resource} the rdf resource for the Function type
     */
    public static final Resource resourceForFunction(FunctionEnum function) {
        return F_TO_R.get(function);
    }

    /**
     * Retrieves the RDF {@link Resource} for the BEL {@link RelationshipType}.
     * 
     * @param rtype
     *            {@link RelationshipType}, the BEL relationship
     * @return {@link Resource} the rdf resource for the Relationship type
     */
    public static final Resource
            resourceForRelationship(RelationshipType rtype) {
        return R_TO_R.get(rtype);
    }

    // statically-initialize F_TO_R and R_TO_R
    static {
        FunctionEnum[] funcs = FunctionEnum.values();
        F_TO_R = constrainedHashMap(funcs.length);
        for (FunctionEnum func : funcs) {
            switch (func) {
            case ABUNDANCE:
                F_TO_R.put(func, Abundance);
                break;
            case BIOLOGICAL_PROCESS:
                F_TO_R.put(func, BiologicalProcess);
                break;
            case CATALYTIC_ACTIVITY:
                F_TO_R.put(func, CatalyticActivity);
                break;
            case CELL_SECRETION:
                F_TO_R.put(func, CellSecretion);
                break;
            case CELL_SURFACE_EXPRESSION:
                F_TO_R.put(func, CellSurfaceExpression);
                break;
            case CHAPERONE_ACTIVITY:
                F_TO_R.put(func, ChaperoneActivity);
                break;
            case COMPLEX_ABUNDANCE:
                F_TO_R.put(func, ComplexAbundance);
                break;
            case COMPOSITE_ABUNDANCE:
                F_TO_R.put(func, CompositeAbundance);
                break;
            case DEGRADATION:
                F_TO_R.put(func, Degradation);
                break;
            case FUSION:
                F_TO_R.put(func, Fusion);
                break;
            case GENE_ABUNDANCE:
                F_TO_R.put(func, GeneAbundance);
                break;
            case GTP_BOUND_ACTIVITY:
                F_TO_R.put(func, GtpBoundActivity);
                break;
            case KINASE_ACTIVITY:
                F_TO_R.put(func, KinaseActivity);
                break;
            case MICRORNA_ABUNDANCE:
                F_TO_R.put(func, MicroRNAAbundance);
                break;
            case MOLECULAR_ACTIVITY:
                F_TO_R.put(func, MolecularActivity);
                break;
            case PATHOLOGY:
                F_TO_R.put(func, Pathology);
                break;
            case PEPTIDASE_ACTIVITY:
                F_TO_R.put(func, PeptidaseActivity);
                break;
            case PHOSPHATASE_ACTIVITY:
                F_TO_R.put(func, PhosphataseActivity);
                break;
            case PRODUCTS:
                F_TO_R.put(func, Products);
                break;
            case PROTEIN_ABUNDANCE:
                F_TO_R.put(func, ProteinAbundance);
                break;
            case PROTEIN_MODIFICATION:
                F_TO_R.put(func, ProteinModification);
                break;
            case REACTANTS:
                F_TO_R.put(func, Reactants);
                break;
            case REACTION:
                F_TO_R.put(func, Reaction);
                break;
            case RIBOSYLATION_ACTIVITY:
                F_TO_R.put(func, RibosylationActivity);
                break;
            case RNA_ABUNDANCE:
                F_TO_R.put(func, RnaAbundance);
                break;
            case SUBSTITUTION:
                F_TO_R.put(func, Substitution);
                break;
            case LIST:
                // List is not a KAM function as these are always expanded
                // F_TO_R.put(func, List);
                break;
            case TRANSCRIPTIONAL_ACTIVITY:
                F_TO_R.put(func, TranscriptionalActivity);
                break;
            case TRANSLOCATION:
                F_TO_R.put(func, Translocation);
                break;
            case TRANSPORT_ACTIVITY:
                F_TO_R.put(func, TransportActivity);
                break;
            case TRUNCATION:
                F_TO_R.put(func, Truncation);
                break;
            default:
                throw new UnsupportedOperationException(func.name()
                        + " is not yet supported.");
            }
        }

        RelationshipType[] rtypes = RelationshipType.values();
        R_TO_R = constrainedHashMap(rtypes.length);
        for (RelationshipType rtype : rtypes) {
            switch (rtype) {
            case ACTS_IN:
                R_TO_R.put(rtype, ActsIn);
                break;
            case ANALOGOUS:
                R_TO_R.put(rtype, Analogous);
                break;
            case ASSOCIATION:
                R_TO_R.put(rtype, Association);
                break;
            case BIOMARKER_FOR:
                R_TO_R.put(rtype, BiomarkerFor);
                break;
            case CAUSES_NO_CHANGE:
                R_TO_R.put(rtype, CausesNoChange);
                break;
            case DECREASES:
                R_TO_R.put(rtype, Decreases);
                break;
            case DIRECTLY_DECREASES:
                R_TO_R.put(rtype, DirectlyDecreases);
                break;
            case DIRECTLY_INCREASES:
                R_TO_R.put(rtype, DirectlyIncreases);
                break;
            case HAS_COMPONENT:
                R_TO_R.put(rtype, HasComponent);
                break;
            case HAS_COMPONENTS:
                R_TO_R.put(rtype, HasComponents);
                break;
            case HAS_MEMBER:
                R_TO_R.put(rtype, HasMember);
                break;
            case HAS_MEMBERS:
                // Not a KAM relationship as these are always expanded to HAS_MEMBER relationships
                // R_TO_R.put(rtype, HasMembers);
                break;
            case HAS_MODIFICATION:
                R_TO_R.put(rtype, HasModification);
                break;
            case HAS_PRODUCT:
                R_TO_R.put(rtype, HasProduct);
                break;
            case HAS_VARIANT:
                R_TO_R.put(rtype, HasVariant);
                break;
            case INCLUDES:
                R_TO_R.put(rtype, Includes);
                break;
            case INCREASES:
                R_TO_R.put(rtype, Increases);
                break;
            case IS_A:
                R_TO_R.put(rtype, IsA);
                break;
            case NEGATIVE_CORRELATION:
                R_TO_R.put(rtype, NegativeCorrelation);
                break;
            case ORTHOLOGOUS:
                R_TO_R.put(rtype, Orthologous);
                break;
            case POSITIVE_CORRELATION:
                R_TO_R.put(rtype, PositiveCorrelation);
                break;
            case PROGNOSTIC_BIOMARKER_FOR:
                R_TO_R.put(rtype, PrognosticBiomarkerFor);
                break;
            case RATE_LIMITING_STEP_OF:
                R_TO_R.put(rtype, RateLimitingStepOf);
                break;
            case REACTANT_IN:
                R_TO_R.put(rtype, ReactantIn);
                break;
            case SUB_PROCESS_OF:
                R_TO_R.put(rtype, SubProcessOf);
                break;
            case TRANSCRIBED_TO:
                R_TO_R.put(rtype, TranscribedTo);
                break;
            case TRANSLATED_TO:
                R_TO_R.put(rtype, TranslatedTo);
                break;
            case TRANSLOCATES:
                R_TO_R.put(rtype, Translocates);
                break;
            default:
                throw new UnsupportedOperationException(rtype.name()
                        + " is not yet supported.");
            }
        }
    }
}
