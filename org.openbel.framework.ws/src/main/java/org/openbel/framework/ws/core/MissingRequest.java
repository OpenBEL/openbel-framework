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
package org.openbel.framework.ws.core;

import static org.openbel.framework.common.BELUtilities.noLength;

import org.openbel.framework.common.InvalidArgument;

/**
 * Web service request exception indicating a missing request. This exception
 * should only be used by web service endpoints.
 */
public class MissingRequest extends RequestException {
    private static final long serialVersionUID = -3126288683277483561L;

    /**
     * Creates a missing request exception for the provided
     * {@code requestName}.
     * <p>
     * This exception should only be thrown by web service endpoints!
     * </p>
     *
     * @param requestName Request name
     */
    public MissingRequest(String requestName) {
        super(makeMsg(requestName));
        if (noLength(requestName)) {
            throw new InvalidArgument("requestName", requestName);
        }
    }

    private static String makeMsg(String requestName) {
        final StringBuilder bldr = new StringBuilder();
        bldr.append("the request '");
        bldr.append(requestName);
        bldr.append("' is missing!");
        return bldr.toString();
    }
}
