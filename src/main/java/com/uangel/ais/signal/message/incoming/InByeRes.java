package com.uangel.ais.signal.message.incoming;

import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ObjectInUseException;
import stack.java.uangel.sip.ResponseEvent;
import stack.java.uangel.sip.message.Response;
import stack.java.uangel.sip.module.SipMessageParser;

/**
 * @author dajin kim
 */
public class InByeRes {
    static final Logger log = LoggerFactory.getLogger(InByeRes.class);
    private final CallManager callManager = CallManager.getInstance();

    public InByeRes() {
        // nothing
    }

    public void receive(ResponseEvent responseEvent) {
        Response response = responseEvent.getResponse();
        String callId = SipMessageParser.getCallId(response);
        CallInfo callInfo = callManager.getCallInfo(callId);

        // Terminate Transaction - CallInfo Null
        // (CallInfo 존재 하는 경우는 CallInfo 삭제할 때 정리)
        if (callInfo == null) {
            terminateCt(responseEvent);
            return;
        }

        // Check Status
        try {
            callInfo.lock();
            if (!callInfo.getCallState().equals(CallState.BYE)) {
                // error
                log.warn("{}Recv Bye {}, MisMatch CallState [{}]", callInfo.getLogHeader(), response.getStatusCode(), callInfo.getCallState());
            }
        } finally {
            callInfo.unlock();
        }

    }

    public static void terminateCt(ResponseEvent responseEvent) {
        try {
            responseEvent.getClientTransaction().terminate();
        } catch (ObjectInUseException e) {
            log.error("InByeRes.terminateCt.Exception ", e);
        }
    }
}
