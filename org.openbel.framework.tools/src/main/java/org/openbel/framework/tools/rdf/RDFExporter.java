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

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.nulls;
import static org.openbel.framework.tools.rdf.KAMVocabulary.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.api.KamStore;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.core.kamstore.data.jdbc.KAMCatalogDao.KamInfo;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.Annotation;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.AnnotationType;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.BelDocumentInfo;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.BelStatement;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.core.kamstore.model.Kam;
import org.openbel.framework.core.kamstore.model.KamStoreException;
import org.openbel.framework.core.kamstore.model.Kam.KamEdge;
import org.openbel.framework.core.kamstore.model.Kam.KamNode;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * RDFExporter builds an RDF object model from the KAM data using the
 * Jena framework and then serializes it to RDF in N3 format.
 * 
 * Data left to capture:
 * TODO Support nested terms in a Term's arguments.  Currently only support Parameter resources.
 * TODO Support nested statement as a statement's object.  Currently only support object Term.
 * 
 * @see <a href="http://jena.sourceforge.net/">http://jena.sourceforge.net/</a>
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public final class RDFExporter {

    /**
     * Jena format {@link String} for N3 notation.
     */
    private static final String N3_FORMAT = "N3";

    private static final InputStream kvis;
    static {
        kvis = RDFExporter.class.getResourceAsStream("kam.rdf");
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private RDFExporter() {
    }

    /**
     * Exports an rdf {@link Model} from the KAM data.
     * 
     * @param kam {@link Kam} the kam, which cannot be null
     * @param kamInfo {@link KamInfo} the kam info, which cannot be null
     * @param kamStore {@link KAMStore} the kamstore, which cannot be null
     * @param outPath {@link String} the output path to write XGMML file to,
     * which can be null, in which case the kam's name will be used and it will
     * be written to the current directory (user.dir).
     * @throws IOException Thrown if an IO error occurred while writing the RDF
     * @throws KamStoreException Thrown if an error occurred pulling data from
     * the kam store
     * @throws InvalidArgument Thrown if either the kam, kamInfo, kamStore, or
     * outputPath arguments were null
     */
    public static void exportRdf(final Kam kam, KamInfo kamInfo,
            final KamStore kamStore,
            String outputPath) throws IOException, InvalidArgument,
            KamStoreException {
        if (nulls(kam, kamInfo, kamStore, outputPath)) {
            throw new InvalidArgument("argument(s) were null");
        }

        // create new ontology model and read in KAM Vocabulary model
        OntModel model = ModelFactory.createOntologyModel();
        model.read(kvis, null, "RDF/XML");

        // add kam resource
        Resource kamResource =
                model.createResource(new AnonId(UUID.randomUUID().toString()));

        // kam type KAM
        kamResource.addProperty(RDF.type, KAM);

        // kam hasName "kam"^^xsd:string
        kamResource.addProperty(hasName,
                model.createTypedLiteral(kamInfo.getName()));

        // kam hasDescription "kam"^^xsd:string
        kamResource.addProperty(hasDescription,
                model.createTypedLiteral(kamInfo.getDescription()));

        Map<Integer, Resource> documents = new HashMap<Integer, Resource>();
        Map<Integer, Resource> annotationDefinitions =
                new HashMap<Integer, Resource>();
        Map<Integer, Resource> annotations = new HashMap<Integer, Resource>();

        List<BelDocumentInfo> docs = kamStore.getBelDocumentInfos(kamInfo);
        for (BelDocumentInfo doc : docs) {
            Resource docResource =
                    model.createResource(new AnonId(UUID.randomUUID()
                            .toString()));
            docResource.addProperty(RDF.type, BelDocument);

            // handle document fields
            if (doc.getAuthors() != null) {
                docResource.addProperty(hasAuthor,
                        model.createTypedLiteral(doc.getAuthors()));
            }

            if (doc.getContactInfo() != null) {
                docResource.addProperty(hasContactInfo,
                        model.createTypedLiteral(doc.getContactInfo()));
            }

            if (doc.getCopyright() != null) {
                docResource.addProperty(hasCopyright,
                        model.createTypedLiteral(doc.getCopyright()));
            }

            if (doc.getDescription() != null) {
                docResource.addProperty(hasDescription,
                        model.createTypedLiteral(doc.getDescription()));
            }

            if (doc.getDisclaimer() != null) {
                docResource.addProperty(hasDisclaimer,
                        model.createTypedLiteral(doc.getDisclaimer()));
            }

            if (doc.getLicenseInfo() != null) {
                docResource.addProperty(hasLicense,
                        model.createTypedLiteral(doc.getLicenseInfo()));
            }

            if (doc.getName() != null) {
                docResource.addProperty(hasName,
                        model.createTypedLiteral(doc.getName()));
            }

            if (doc.getVersion() != null) {
                docResource.addProperty(hasVersion,
                        model.createTypedLiteral(doc.getVersion()));
            }

            List<Namespace> namespaces = doc.getNamespaces();
            for (Namespace namespace : namespaces) {
                // add namespace resource by it's URL
                Resource namespaceResource = model.createResource(namespace
                        .getResourceLocation());
                namespaceResource.addProperty(RDF.type, Namespace);

                // associate prefix and resource location
                namespaceResource.addProperty(hasPrefix,
                        model.createTypedLiteral(namespace.getPrefix()));
                namespaceResource.addProperty(hasResourceLocation,
                        model.createTypedLiteral(namespace
                                .getResourceLocation()));

                // associate document with namespace as a defines relationship
                docResource.addProperty(defines, namespaceResource);
            }

            List<AnnotationType> annotationTypes = doc.getAnnotationTypes();
            for (AnnotationType annotationType : annotationTypes) {
                final Resource annotationDefResource;

                switch (annotationType.getAnnotationDefinitionType()) {
                case ENUMERATION:
                    annotationDefResource =
                            model
                                    .createResource(new AnonId(UUID
                                            .randomUUID().toString()));

                    // associate metadata of annotation type
                    annotationDefResource.addProperty(hasName,
                            model.createTypedLiteral(annotationType.getName()));
                    if (StringUtils.isNotBlank(annotationType.getDescription())) {
                        annotationDefResource.addProperty(hasDescription, model
                                .createTypedLiteral(annotationType
                                        .getDescription()));
                    }
                    annotationDefResource.addProperty(hasType, model
                            .createTypedLiteral(annotationType
                                    .getAnnotationDefinitionType()
                                    .getDisplayValue()));

                    if (StringUtils.isNotBlank(annotationType.getDescription())) {
                        annotationDefResource.addProperty(hasUsage, model
                                .createTypedLiteral(annotationType.getUsage()));
                    }

                    // read list domain for internal list annotation
                    List<String> domain =
                            kamStore.getAnnotationTypeDomainValues(kamInfo,
                                    annotationType);

                    if (hasItems(domain)) {
                        RDFList enumres = model.createList();
                        enumres.setStrict(true);

                        // iterate domain to pre-build RDF resources and associate domain value
                        Resource[] itemResources = new Resource[domain.size()];
                        String[] domainValues =
                                domain.toArray(new String[domain.size()]);
                        for (int i = 0; i < domainValues.length; i++) {
                            itemResources[i] = model
                                    .createResource(new AnonId(UUID
                                            .randomUUID().toString()));
                            itemResources[i].addProperty(RDF.type, RDF.List);
                            itemResources[i].addProperty(RDF.first,
                                    model.createTypedLiteral(domainValues[i]));
                        }

                        // iterate item resources to link them
                        for (int i = 0; i < itemResources.length; i++) {
                            if ((i + 1) < itemResources.length) {
                                // link to next domain value
                                itemResources[i].addProperty(RDF.rest,
                                        itemResources[i + 1]);
                            } else {
                                // link to RDF.nil indicating end of list
                                itemResources[i].addProperty(RDF.rest, RDF.nil);
                            }

                            enumres = enumres.with(itemResources[i]);
                        }

                        // associate annotation to the domain list
                        annotationDefResource.addProperty(hasList, enumres);
                    }

                    break;
                case REGULAR_EXPRESSION:
                    // read list domain for internal pattern annotation
                    domain =
                            kamStore.getAnnotationTypeDomainValues(kamInfo,
                                    annotationType);

                    if (domain.size() != 1) {
                        throw new IllegalStateException(
                                "Expecting a single, Regular Expression pattern, but received: "
                                        + domain);
                    }

                    annotationDefResource =
                            model
                                    .createResource(new AnonId(UUID
                                            .randomUUID().toString()));

                    // associate metadata of annotation type
                    annotationDefResource.addProperty(hasName,
                            model.createTypedLiteral(annotationType.getName()));
                    annotationDefResource.addProperty(hasDescription,
                            model.createTypedLiteral(annotationType
                                    .getDescription()));
                    annotationDefResource.addProperty(hasType, model
                            .createTypedLiteral(annotationType
                                    .getAnnotationDefinitionType()
                                    .getDisplayValue()));
                    annotationDefResource
                            .addProperty(hasUsage, model
                                    .createTypedLiteral(annotationType
                                            .getUsage()));

                    String patternDomain = domain.get(0);

                    // associate annotation to the domain pattern
                    annotationDefResource.addProperty(hasPattern,
                            model.createTypedLiteral(patternDomain));
                    break;
                case URL:
                    if (annotationType.getUrl() == null) {
                        throw new IllegalStateException(
                                "Expecting non-null URL for external annotation");
                    }

                    // associate annotation to the URL where the domain is defined
                    annotationDefResource =
                            model.createResource(annotationType.getUrl());
                    annotationDefResource.addProperty(hasName,
                            model.createTypedLiteral(annotationType.getName()));
                    annotationDefResource.addProperty(hasURL,
                            model.createTypedLiteral(annotationType.getUrl()));
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "Annotation definition type not supported: "
                                    + annotationType
                                            .getAnnotationDefinitionType());
                }

                annotationDefResource.addProperty(RDF.type,
                        AnnotationDefinition);

                // associate document with annotation definition as a defines relationship
                docResource.addProperty(defines, annotationDefResource);

                annotationDefinitions.put(annotationType.getId(),
                        annotationDefResource);
            }

            kamResource.addProperty(composedOf, docResource);
            documents.put(doc.getId(), docResource);
        }

        Map<KamNode, Resource> kamNodeResources =
                new HashMap<Kam.KamNode, Resource>();
        Map<Integer, Resource> termResources = new HashMap<Integer, Resource>();

        // handle kam nodes first
        Iterator<KamNode> kamNodes = kam.getNodes().iterator();
        while (kamNodes.hasNext()) {
            KamNode kamNode = kamNodes.next();

            // TODO: This is a sanity check that should be removed as LIST is not a KAM function type
            if (FunctionEnum.LIST == kamNode.getFunctionType()) {
                System.err.println("Invalid KAM node type found: "
                        + kamNode.getFunctionType().getDisplayValue());
                continue;
            }

            Resource functionResource = model.getResource(KAMVocabulary
                    .resourceForFunction(kamNode.getFunctionType()).getURI());

            // Retrieve seen KAMNode Resource or create a new Blank Node for it.
            Resource kamNodeResource;
            if (kamNodeResources.containsKey(kamNode)) {
                kamNodeResource = kamNodeResources.get(kamNode);
            } else {
                kamNodeResource = model.createResource(new AnonId(UUID
                        .randomUUID().toString()));

                // associate kam node with kam resource
                kamResource.addProperty(composedOf, kamNodeResource);
            }

            // node type KAMNode
            kamNodeResource.addProperty(RDF.type, KAMNode);

            // node hasFunction Function
            kamNodeResource.addProperty(hasFunction, functionResource);

            // node hasId "1"^^xsd:int
            kamNodeResource.addLiteral(hasId,
                    model.createTypedLiteral(kamNode.getId()));

            // node hasLabel "p(EG:207)"^^xsd:string
            kamNodeResource.addLiteral(hasLabel,
                    model.createTypedLiteral(kamNode.getLabel()));

            // hold on to kam node resources
            kamNodeResources.put(kamNode, kamNodeResource);

            // handle terms for this KAMNode
            // TODO Support nested terms instead of just Parameters!
            List<BelTerm> terms = kamStore.getSupportingTerms(kamNode);
            for (BelTerm term : terms) {
                Resource termResource = model.createResource(new AnonId(UUID
                        .randomUUID().toString()));

                termResource.addProperty(RDF.type, Term);
                termResource.addProperty(hasId,
                        model.createTypedLiteral(term.getId()));
                termResource.addProperty(hasLabel,
                        model.createTypedLiteral(term.getLabel()));
                termResource.addProperty(hasFunction,
                        resourceForFunction(kamNode.getFunctionType()));

                RDFList argumentres = model.createList();

                List<TermParameter> termParameters =
                        kamStore.getTermParameters(kamInfo, term);
                TermParameter[] tparray = termParameters
                        .toArray(new TermParameter[termParameters.size()]);
                Resource[] tpresarray = new Resource[tparray.length];

                // iterate term parameters to pre-build RDF resources and associate Parameter
                for (int i = 0; i < tparray.length; i++) {
                    tpresarray[i] = model.createResource(new AnonId(UUID
                            .randomUUID().toString()));
                    tpresarray[i].addProperty(RDF.type, RDF.List);

                    Resource parameterResource =
                            model
                                    .createResource(new AnonId(UUID
                                            .randomUUID().toString()));
                    parameterResource.addProperty(RDF.type, Parameter);
                    parameterResource
                            .addProperty(hasValue, model
                                    .createTypedLiteral(tparray[i]
                                            .getParameterValue()));

                    if (tparray[i].getNamespace() != null) {
                        parameterResource.addProperty(hasNamespace, model
                                .getResource(tparray[i].getNamespace()
                                        .getResourceLocation()));
                    }

                    tpresarray[i].addProperty(RDF.first, parameterResource);
                }

                // iterate argument resources to link them
                for (int i = 0; i < tpresarray.length; i++) {
                    if ((i + 1) < tpresarray.length) {
                        // link to next term argument
                        tpresarray[i].addProperty(RDF.rest, tpresarray[i + 1]);
                    } else {
                        // link to RDF.nil indicating end of list
                        tpresarray[i].addProperty(RDF.rest, RDF.nil);
                    }

                    argumentres = argumentres.with(tpresarray[i]);
                }

                // associate term resource to the arguments list
                termResource.addProperty(hasArguments, argumentres);

                // node is represented by the term
                kamNodeResource.addProperty(isRepresentedBy, termResource);

                // hold on to term resources
                termResources.put(term.getId(), termResource);
            }
        }

        // handle kam edges second
        Iterator<KamEdge> kamEdges = kam.getEdges().iterator();
        while (kamEdges.hasNext()) {
            KamEdge kamEdge = kamEdges.next();

            Resource relationshipResource =
                    KAMVocabulary.resourceForRelationship(kamEdge
                            .getRelationshipType());

            Resource kamEdgeResource =
                    model.createResource(new AnonId(UUID.randomUUID()
                            .toString()));

            // associate kam edge with kam resource
            kamResource.addProperty(composedOf, kamEdgeResource);

            // edge type KAMEdge
            kamEdgeResource.addProperty(RDF.type, KAMEdge);

            // edge hasRelationship Relationship
            kamEdgeResource.addProperty(hasRelationship, relationshipResource);

            // edge hasId "1"^^xsd:int
            kamEdgeResource.addLiteral(hasId,
                    model.createTypedLiteral(kamEdge.getId()));

            Resource sourceResource =
                    kamNodeResources.get(kamEdge.getSourceNode());
            Resource targetResource =
                    kamNodeResources.get(kamEdge.getTargetNode());

            // TODO: This is a sanity check that should be removed
            if (null == sourceResource || null == targetResource) {
                System.err
                        .println("Can't locate source or target node resource for edge: "
                                + kamEdge.toString());
                continue;
            }

            // edge hasSubjectNode KAMNode
            kamEdgeResource.addProperty(hasSubjectNode, sourceResource);

            // edge hasObjectNode KAMNode
            kamEdgeResource.addProperty(hasObjectNode, targetResource);

            // TODO Handle object statements
            // TODO Handle statement annotation associations
            List<BelStatement> statements =
                    kamStore.getSupportingEvidence(kamEdge);
            for (BelStatement statement : statements) {
                if (null == statement.getObject()) {
                    continue;
                }

                if (!BelTerm.class.isAssignableFrom(statement.getObject()
                        .getClass())) {
                    continue;
                }

                Resource statementResource =
                        model.createResource(new AnonId(UUID.randomUUID()
                                .toString()));

                statementResource.addProperty(RDF.type, Statement);
                statementResource.addProperty(hasId,
                        model.createTypedLiteral(statement.getId()));

                // associate statement with document
                Resource docres =
                        documents.get(statement.getBelDocumentInfo().getId());
                docres.addProperty(defines, statementResource);

                Resource subjectTermResource =
                        termResources.get(statement.getSubject().getId());

                if (subjectTermResource == null) {
                    throw new IllegalStateException(
                            "subject term for statement is not known.");
                }

                statementResource.addProperty(hasSubjectTerm,
                        subjectTermResource);

                if (statement.getObject() != null) {
                    Resource objectTermResource =
                            termResources.get(statement.getObject().getId());

                    if (objectTermResource == null) {
                        throw new IllegalStateException(
                                "object term for statement is not known.");
                    }

                    // associate to relationship and object term
                    statementResource.addProperty(hasRelationship,
                            resourceForRelationship(statement
                                    .getRelationshipType()));
                    statementResource.addProperty(hasObjectTerm,
                            objectTermResource);

                    // associate edge to this subject,rel,object statement
                    kamEdgeResource.addProperty(isRepresentedBy,
                            statementResource);
                }

                // associate annotations to statement
                List<Annotation> statementAnnotations =
                        statement.getAnnotationList();
                for (Annotation statementAnnotation : statementAnnotations) {
                    Resource ares =
                            annotations.get(statementAnnotation.getId());
                    if (ares == null) {
                        ares =
                                model.createResource(new AnonId(UUID
                                        .randomUUID().toString()));
                        annotations.put(statementAnnotation.getId(), ares);
                    }

                    // associate annotation value to annotation
                    ares.addProperty(hasValue, model
                            .createTypedLiteral(statementAnnotation.getValue()));

                    Integer adefId =
                            statementAnnotation.getAnnotationType().getId();
                    Resource adefResource = annotationDefinitions.get(adefId);

                    // associate annotation with the annotation definition it is defined by
                    ares.addProperty(definedBy, adefResource);

                    // associate statement with its annotation
                    statementResource.addProperty(hasAnnotation, ares);
                }
            }
        }

        model.write(new FileWriter(outputPath), N3_FORMAT);
    }
}
