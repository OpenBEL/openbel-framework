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

import static org.openbel.framework.common.BELUtilities.getFirstMessage;
import static org.openbel.framework.common.BELUtilities.hasLength;

import java.lang.reflect.Method;
import java.util.Locale;

import org.springframework.util.Assert;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointExceptionResolver;
import org.springframework.ws.server.endpoint.AbstractEndpointExceptionResolver;
import org.springframework.ws.server.endpoint.MethodEndpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapMessage;

/**
 * {@link CustomizeSoapFaultExceptionResolver} is a custom implementation of an
 * {@link EndpointExceptionResolver endpoint exception resolver} that prepends
 * the webservice operation name to the endpoint's exception message.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class CustomizeSoapFaultExceptionResolver extends
        AbstractEndpointExceptionResolver {

    /**
     * The suffix of the endpoint's request object - {@value}
     */
    private static final String ENDPOINT_REQUEST_SUFFIX = "Request";

    /**
     * The fault string format - {@value}
     */
    private static final String FAULT_STRING_FORMAT = "%s: %s";

    /**
     * {@inheritDoc} This implementation extracts the webservice operation name
     * from the {@link PayloadRoot payload root annotation} defines on the
     * {@link MethodEndpoint method endpoint}. The webservice operation name is
     * prepended to the soap fault's "faultstring".
     * <p>
     * If the webservice operation name cannot be determined the soap fault's
     * "faultstring" will just contain the {@link Exception exception}'s
     * message. This can occur when:
     * <ul>
     * <li>the endpoint, <tt>ep</tt>, is not an instance of
     * {@link MethodEndpoint method endpoint}</li>
     * <li>the {@link PayloadRoot} is <tt>null</tt></li>
     * <li>the "localPart" property of the {@link PayloadRoot payload root} does
     * not end with
     * {@link CustomizeSoapFaultExceptionResolver#ENDPOINT_REQUEST_SUFFIX}</li>
     * </p>
     */
    @Override
    protected boolean resolveExceptionInternal(
            MessageContext messageContext, Object ep, Exception ex) {
        // fail when the response is not a SoapMessage, because we would not be
        // able to attach fault to a soap response
        Assert.isInstanceOf(SoapMessage.class, messageContext.getResponse(),
                "CustomizeSoapFaultExceptionResolver requires a SoapMessage");

        String exMsg = ex.getMessage();
        String firstMsg = getFirstMessage(ex);
        if (hasLength(firstMsg)) {
            if (!exMsg.equals(firstMsg) && !exMsg.contains(firstMsg)) {
                exMsg = exMsg.concat(" (caused by: " + firstMsg + ")");
            }
        }

        // if following conditions, soap fault with ex message
        if (!MethodEndpoint.class.isInstance(ep)) {
            return createFault(messageContext, null, exMsg);
        }

        // find payload root's localPart or return soap fault with ex message
        final Method emtd = ((MethodEndpoint) ep).getMethod();
        String localPart = null;
        PayloadRoot proot = emtd.getAnnotation(PayloadRoot.class);
        if (proot != null) {
            localPart = proot.localPart();
        } else {
            return createFault(messageContext, null, exMsg);
        }

        // return soap fault with ex message if localPart does not end with
        // 'Request'
        if (!localPart.endsWith(ENDPOINT_REQUEST_SUFFIX)) {
            return createFault(messageContext, null, exMsg);
        }

        // extract endpoint operation name
        localPart = localPart.substring(0,
                localPart.indexOf(ENDPOINT_REQUEST_SUFFIX));
        return createFault(messageContext, localPart, exMsg);
    }

    /**
     * Create the soap fault and attach to the {@link SoapBody} of the
     * {@link SoapMessage soap response}.
     *
     * @param msgctx {@link MessageContext}, the message context for this
     * webservice message
     * @param wsopName {@link String}, the webservice operation name, which can
     * be <tt>null</tt>
     * @param exMsg {@link String}, the endpoint {@link Exception exception}
     * message
     * @return
     */
    private boolean createFault(final MessageContext msgctx,
            final String wsopName, final String exMsg) {
        final String faultstring;
        if (wsopName == null) {
            logger.warn("Sending SOAP fault without endpoint identification for: "
                    + msgctx.getRequest());
            faultstring = exMsg;
        } else {
            faultstring = String.format(FAULT_STRING_FORMAT, wsopName, exMsg);
        }

        final SoapMessage response = (SoapMessage) msgctx.getResponse();
        final SoapBody body = response.getSoapBody();
        body.addServerOrReceiverFault(faultstring, Locale.ENGLISH);
        return true;
    }
}
