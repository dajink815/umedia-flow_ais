package com.uangel.ais.signal.message.incoming;

import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.signal.process.outgoing.OutResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ObjectInUseException;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.message.Response;
import stack.java.uangel.sip.module.SipMessageParser;

/**
 * @author dajin kim
 */
public class InBye {
    static final Logger log = LoggerFactory.getLogger(InBye.class);
    private static final CallManager callManager = CallManager.getInstance();

    public InBye() {
        // nothing
    }

    public void receive(RequestEvent requestEvent, ServerTransaction st) {
        Request request = requestEvent.getRequest();
        String callId = SipMessageParser.getCallId(request);

        CallInfo callInfo = callManager.getCallInfo(callId);

        try {
            OutResponse outResponse = new OutResponse();
            Response byeOk = outResponse.sendResponse(request, Response.OK, callInfo, st);
            log.info("Outgoing ByeOk Response [\r\n{}]", byeOk);
            st.terminate();
        } catch (ObjectInUseException e) {
            log.error("InBye.receive.Exception(Terminate ByeST) ", e);
        }


        if (callInfo == null) {

            return;
        }


        try {
            callInfo.lock();
            if (!CallState.CONNECT.equals(callInfo.getCallState())) {
                log.warn("{}Received Bye, MisMatch CallState [{}]", callInfo.getLogHeader(), callInfo.getCallState());
                return;
            }
        } finally {
            callInfo.unlock();
        }

        // hangup
        // CallStop

    }
}
