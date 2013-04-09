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
package org.openbel.framework.core.df.external;

import static org.openbel.framework.core.df.cache.ResourceType.ANNOTATIONS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.AnnotationDefinitionResolutionException;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.CommonModelFactory;
import org.openbel.framework.core.df.beldata.annotation.AnnotationHeader;
import org.openbel.framework.core.df.beldata.annotation.AnnotationHeaderProcessor;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.CachedResource;
import org.openbel.framework.core.df.cache.DefaultCacheLookupService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * CacheableAnnotationDefinitionServiceImpl implements a service to resolve BEL
 * annotation files and cache the data for subsequent calls.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class CacheableAnnotationDefinitionServiceImpl implements
        CacheableAnnotationDefinitionService {

    /**
     * Defines the resource cache service dependency.
     */
    private final CacheableResourceService resourceCache;

    /**
     * Defines the cache lookup service dependency.
     */
    private final CacheLookupService cacheLookupService;

    public CacheableAnnotationDefinitionServiceImpl(
            CacheableResourceService resourceCache,
            CacheLookupService cacheLookupService) {
        this.resourceCache = resourceCache;
        this.cacheLookupService = cacheLookupService;
    }

    public CacheableAnnotationDefinitionServiceImpl() {
        this.resourceCache = new DefaultCacheableResourceService();
        this.cacheLookupService = new DefaultCacheLookupService();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationDefinition resolveAnnotationDefinition(
            String resourceLocation)
            throws AnnotationDefinitionResolutionException {
        ResolvedResource resolveResource;
        try {
            resolveResource = resourceCache.resolveResource(
                    ANNOTATIONS, resourceLocation);
        } catch (ResourceDownloadError e) {
            throw new AnnotationDefinitionResolutionException(resourceLocation,
                    e.getMessage());
        }
        File annotationCacheCopy = resolveResource.getCacheResourceCopy();

        CachedResource cacheResource = cacheLookupService.findInCache(
                ANNOTATIONS, resourceLocation);

        AnnotationHeaderProcessor annotationHeaderProcessor =
                new AnnotationHeaderProcessor();

        AnnotationHeader annotationHeader;
        try {
            annotationHeader =
                    annotationHeaderProcessor
                            .processAnnotationHeader(resourceLocation,
                                    annotationCacheCopy,
                                    cacheResource
                                            .getLocalFile());
        } catch (IndexingFailure e) {
            throw new AnnotationDefinitionResolutionException(resourceLocation,
                    e.getMessage());
        }

        AnnotationDefinition annotationDefinition = CommonModelFactory
                .getInstance().createAnnotationDefinition(annotationHeader.
                        getAnnotationBlock().getKeyword());
        annotationDefinition.setDescription(annotationHeader
                .getAnnotationBlock().getDescriptionString());
        annotationDefinition.setUsage(annotationHeader.getAnnotationBlock()
                .getUsageString());
        annotationDefinition.setType(annotationHeader.getAnnotationBlock()
                .getAnnotationType());
        annotationDefinition.setURL(resourceLocation);

        long characterOffset = annotationHeaderProcessor
                .getCharacterStopOffset();

        try {
            switch (annotationDefinition.getType()) {
            case ENUMERATION:
                annotationDefinition.setEnums(parseEnumData(
                        annotationCacheCopy,
                        annotationHeader.getProcessingBlock()
                                .getDelimiterString(),
                        characterOffset));
                break;
            case REGULAR_EXPRESSION:
                annotationDefinition.setValue(parseRegularExpression(
                        annotationCacheCopy, characterOffset));
            }
        } catch (IOException e) {
            throw new AnnotationDefinitionResolutionException(resourceLocation,
                    e.getMessage());
        }

        return annotationDefinition;
    }

    /**
     * Reads each line after the [Values] block as enumeration values for the
     * annotation definition.
     *
     * @param annotationCacheCopy {@link File}, the annotation file to read
     * from
     * @param valueDelimiter {@link String}, the delimiter that separates
     * annotation value lines
     * @param characterOffset <tt>long</tt>, the character offset of the
     * [Values] block in <tt>annotationCacheCopy</tt>
     * @return {@link List} of {@link String} that represents the enumeration
     * data
     * @throws IOException Thrown if an IO error occurred reading the BEL
     * annotation file
     */
    private List<String> parseEnumData(File annotationCacheCopy,
            String valueDelimiter, long characterOffset)
            throws IOException {
        List<String> enumData = new ArrayList<String>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(annotationCacheCopy));
            reader.skip(characterOffset);

            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] lineTokens = StringUtils.splitByWholeSeparator(line,
                        valueDelimiter);

                if (lineTokens != null && lineTokens.length >= 1) {
                    enumData.add(lineTokens[0]);
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {}
            }
        }

        return enumData;
    }

    /**
     * Reads the first line after the annotation header as a regular expression
     * for the annotation definition.
     *
     * @param annotationCacheCopy
     *            {@link File}, the annotation cache copy to parse
     * @param characterOffset
     *            <tt>long</tt>, the character offset to start reading from
     * @return {@link String} the parsed regular expression
     * @throws IOException
     *             Thrown if an IO error occurred reading the annotation file
     */
    private String parseRegularExpression(File annotationCacheCopy,
            long characterOffset) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(annotationCacheCopy));
            reader.skip(characterOffset);
            return reader.readLine();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {}
            }
        }
    }
}
