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
package org.openbel.framework.common;

/**
 * AnnotationDefinitionResolutionException encapsulates a
 * {@link BELWarningException} designating an error in resolving an annotation
 * definition.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class AnnotationDefinitionResolutionException extends
        BELWarningException {
    private static final long serialVersionUID = -651969189868837125L;

    /**
     * Construct the exception with the annotation definition resource
     * location, a user msg, and the exception's cause.
     *
     * @param resourceLocation {@link String}, the resource location
     * @param msg {@link String}, the exception msg
     * @param cause {@link Throwable}, the exception's cause
     */
    public AnnotationDefinitionResolutionException(String resourceLocation,
            String msg, Throwable cause) {
        super(resourceLocation, msg, cause);
    }

    /**
     * Construct the exception with the annotation definition resource
     * location and a user msg.
     *
     * @param resourceLocation {@link String}, the resource location
     * @param msg {@link String}, the exception msg
     */
    public AnnotationDefinitionResolutionException(String resourceLocation,
            String msg) {
        super(resourceLocation, msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("RESOLUTION FAILURE ");
        final String name = getName();
        if (name != null) {
            bldr.append(" in ");
            bldr.append(name);
        }
        bldr.append("\n\treason: ");

        final String msg = getMessage();
        if (msg != null) {
            bldr.append(msg);
        } else {
            bldr.append("Unknown");
        }

        bldr.append("\n");
        return bldr.toString();
    }
}
