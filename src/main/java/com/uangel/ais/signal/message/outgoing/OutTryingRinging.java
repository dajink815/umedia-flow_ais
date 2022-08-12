package com.uangel.ais.signal.message.outgoing;

import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.signal.process.outgoing.OutResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.message.Response;
import stack.java.uangel.sip.module.SipMessageParser;

/**
 * @author dajin kim
 */
public class OutTryingRinging {
    static final Logger log = LoggerFactory.getLogger(OutTryingRinging.class);

    public OutTryingRinging() {
        // nothing
    }

    public Response sendTrying(RequestEvent requestEvent, ServerTransaction st) {
        Response response = null;

        try {
            // OutResponse 이용해 response 송신
            OutResponse outResponse = new OutResponse(requestEvent.getRequest());
            response = outResponse.sendResponse(Response.TRYING);

            if (response != null) {
                st.sendResponse(response);
            }
        } catch (Exception e) {
            log.error("OutTryingRinging.sendTrying.Exception ", e);
        }

        return response;
    }

    public Response sendRinging(CallInfo callInfo) {
        if (callInfo == null) {
            log.warn("() () () CallInfo is Null");
            return null;
        }

        ServerTransaction st = callInfo.getSt();
        if (st == null) {
            log.warn("{}Server Transaction is null. Can't send Ringing response.", callInfo.getLogHeader());
            return null;
        }

        Response response = null;

        // OutResponse 이용해 response 송신
        OutResponse outResponse = new OutResponse(st.getRequest());


        return response;
    }

}
