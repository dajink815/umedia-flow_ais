package com.uangel.ais.session;

import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.session.type.CallType;
import com.uangel.ais.signal.process.outgoing.SipOutgoingModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.message.Request;

/**
 * @author dajin kim
 */
public class ReleaseSession {
    static final Logger log = LoggerFactory.getLogger(ReleaseSession.class);

    public ReleaseSession() {
        // nothing
    }

    public void release(CallInfo callInfo, int releaseCode) {
        if (callInfo == null) {
            log.warn("ReleaseSession CallInfo is Null");
            // CallInfo Null 때 세션 정리?
            return;
        }

        try {
            callInfo.lock();

            CallType callType = callInfo.getCallType();
            CallState callState = callInfo.getCallState();
            log.info("{}ReleaseSession : {}, {} (State:{} {})", callInfo.getLogHeader(),
                    callType, releaseCode, callState, callInfo.getRmqState());

            // IDLE state return
            if (callState.equals(CallState.IDLE)) {
                return;
            }

            SipOutgoingModule sipOutgoing = SipOutgoingModule.getInstance();
            RmqMsgSender rmqSender = RmqMsgSender.getInstance();

            if (callState.equals(CallState.CONNECT)) {
                // BYE
                sipOutgoing.outBye(callInfo);
                rmqSender.sendHangup(callInfo);
                rmqSender.sendCallStop(callInfo);
            } else {
                // INBOUND 만 고려

                // todo Error Response 로 정리 맞는지 상태 확인

                sipOutgoing.outError(callInfo, releaseCode, Request.INVITE);
                rmqSender.sendHangup(callInfo);
                rmqSender.sendCallStop(callInfo);
            }

            // 바로 Terminate Transaction?


        } catch (Exception e) {
            log.error("ReleaseSession.release.Exception ", e);
        } finally {
            callInfo.unlock();
        }

    }


}
