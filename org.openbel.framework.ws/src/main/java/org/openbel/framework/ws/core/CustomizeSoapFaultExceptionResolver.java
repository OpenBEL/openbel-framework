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
package org.openbel.framework.ws.core;

import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.*;

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

        String exMsg        = ex.getMessage();
        String firstMsg     = getFirstMessage(ex);
        String exStacktrace = getFirstStacktrace(ex);
        if (hasLength(firstMsg)) {
            if (!exMsg.equals(firstMsg) && !exMsg.contains(firstMsg)) {
                exMsg = exMsg.concat("\ncaused by:\n\n" + firstMsg);
            }
        }
        if (hasLength(exStacktrace)) {
            exMsg = exMsg.concat(format("\n\nstacktrace:\n\n%s\n", exStacktrace));
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
            faultstring = format(FAULT_STRING_FORMAT, wsopName, exMsg);
        }

        logError(wsopName, exMsg);

        final SoapMessage response = (SoapMessage) msgctx.getResponse();
        final SoapBody body = response.getSoapBody();
        body.addServerOrReceiverFault(faultstring, Locale.ENGLISH);
        return true;
    }

    private void logError(String leader, final String msg) {
        if (leader == null) leader = "request";

        logger.error(format("SOAP error encountered with {0}: {1}", leader, msg));
    }
}
