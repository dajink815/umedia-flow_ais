package com.uangel.ais.session;

import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.session.type.CallType;
import com.uangel.ais.signal.process.outgoing.SipOutgoingModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.message.Request;

import java.util.EnumSet;

/**
 * @author dajin kim
 */
public class ReleaseSession {
    static final Logger log = LoggerFactory.getLogger(ReleaseSession.class);

    public ReleaseSession() {
        // nothing
    }

    // Connect 이전 MainState, BYE/Error/Cancel 상태는 SIP 처리 Thread 에서 정리
    private static final EnumSet<CallState> prevConnectState = EnumSet.of(
            CallState.INVITE,
            CallState.TRYING,
            CallState.RINGING
    );

    public void release(CallInfo callInfo, int releaseCode) {
        if (callInfo == null) {
            log.warn("ReleaseSession CallInfo is Null");
            return;
        }

        try {
            callInfo.lock();

            CallType callType = callInfo.getCallType();
            CallState callState = callInfo.getCallState();
            log.warn("{}ReleaseSession : {}, {} (State:{} {})", callInfo.getLogHeader(),
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
            } else if (prevConnectState.contains(callState)) {
                // INBOUND 만 고려
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
