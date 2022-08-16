package com.uangel.ais.signal.message.incoming;

import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.signal.process.outgoing.OutResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.message.Response;
import stack.java.uangel.sip.module.SipMessageParser;

/**
 * @author dajin kim
 */
public class InCancel {
    static final Logger log = LoggerFactory.getLogger(InCancel.class);
    private static final CallManager callManager = CallManager.getInstance();

    public InCancel() {
        // nothing
    }

    public void receive(RequestEvent requestEvent, ServerTransaction st) {
        Request request = requestEvent.getRequest();
        String callId = SipMessageParser.getCallId(request);

        CallInfo callInfo = callManager.getCallInfo(callId);
        if (callInfo == null) {
            // Log
            // Error Response
            return;
        }

        // Cancel State

        OutResponse outResponse = new OutResponse();

        // Send Cancel OK
        try {
            Response cancelOk = outResponse.sendResponse(request, Response.OK, callInfo, st);
            log.info("Outgoing CancelOk Response [\r\n{}]", cancelOk);
            st.terminate();
        } catch (Exception e) {
            log.error("InCancel.receive.Exception(SendCancelOk) ", e);
        }

        // Send 487 Response
        //try {
            ServerTransaction inviteSt = callInfo.getInviteSt();
            Response terminatedRes = outResponse.sendResponse(inviteSt.getRequest(), Response.REQUEST_TERMINATED, callInfo, inviteSt);
            log.info("Outgoing Terminated Response [\r\n{}]", terminatedRes);
            // Invite ServerTransaction
/*        } catch (Exception e) {
            log.error("InCancel.receive.Exception(Send487) ", e);
        }*/


        // hangup, CallStop

    }
}
