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
package org.openbel.framework.ws.endpoint;

import static org.openbel.framework.common.enums.BELFrameworkVersion.VERSION_NUMBER;

import org.openbel.framework.ws.model.GetBELFrameworkVersionResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Core BEL framework web service endpoint.
 */
@Endpoint
public class CoreEndpoint extends WebServiceEndpoint {
    private final static String GBFV_REQ = "GetBELFrameworkVersionRequest";
    private final static GetBELFrameworkVersionResponse RESPONSE;
    static {
        RESPONSE = new GetBELFrameworkVersionResponse();
        RESPONSE.setVersion(VERSION_NUMBER);
    }

    /**
     * Returns {@link GetBELFrameworkVersionResponse}.
     *
     * @return {@link GetBELFrameworkVersionResponse}
     */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GBFV_REQ)
    public GetBELFrameworkVersionResponse getVersion() {
        return RESPONSE;
    }

}
