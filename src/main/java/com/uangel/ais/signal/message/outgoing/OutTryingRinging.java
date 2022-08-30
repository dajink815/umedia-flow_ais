package com.uangel.ais.signal.message.outgoing;

import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.signal.process.outgoing.OutResponse;
import com.uangel.ais.signal.process.outgoing.SipCreateHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.message.Response;

/**
 * @author dajin kim
 */
public class OutTryingRinging {
    static final Logger log = LoggerFactory.getLogger(OutTryingRinging.class);

    public OutTryingRinging() {
        // nothing
    }

    public Response sendTrying(RequestEvent requestEvent, ServerTransaction st) {
        // INVITE Request 이용해 response 송신
        OutResponse outResponse = new OutResponse();
        return outResponse.sendResponse(requestEvent.getRequest(), Response.TRYING, null, st);
    }

    public Response sendRinging(CallInfo callInfo) {
        if (callInfo == null) {
            log.warn("() () () OutTryingRinging.sendRinging - CallInfo is Null");
            return null;
        }

        ServerTransaction st = callInfo.getInviteSt();
        if (st == null) {
            log.warn("{}Server Transaction is null. Can't send Ringing response.", callInfo.getLogHeader());
            return null;
        }

        Response response;
        try {
            callInfo.setCallState(CallState.RINGING);

            // INVITE Request 이용해 response 송신
            OutResponse outResponse = new OutResponse();
            response = outResponse.createResponse(st.getRequest(), Response.RINGING, callInfo);

            if (response != null) {
                SipCreateHeader createHeader = new SipCreateHeader();
                response.addHeader(createHeader.createContactHeader());
                st.sendResponse(response);
            }
        } catch (Exception e) {
            log.error("OutTryingRinging.sendRinging.Exception ", e);
            response = null;
        }
        return response;
    }

}
