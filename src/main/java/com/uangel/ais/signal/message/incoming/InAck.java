package com.uangel.ais.signal.message.incoming;

import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.module.SipMessageParser;

/**
 * @author dajin kim
 */
public class InAck {
    static final Logger log = LoggerFactory.getLogger(InAck.class);
    private static final CallManager callManager = CallManager.getInstance();

    public InAck() {
        // nothing
    }

    public void receive(RequestEvent requestEvent) {
        Request request = requestEvent.getRequest();
        String callId = SipMessageParser.getCallId(request);

        CallInfo callInfo = callManager.getCallInfo(callId);
        if (callInfo == null) {
            // 이미 세션이 정리된 상태

            return;
        }

        try {
            callInfo.lock();
            if (!CallState.CONNECT.equals(callInfo.getCallState())) {
                log.warn("{}Received Ack, MisMatch CallState [{}]", callInfo.getLogHeader(), callInfo.getCallState());
                return;
            }
        } finally {
            callInfo.unlock();
        }

        RmqMsgSender.getInstance().sendCallStart(callInfo);
    }


}
