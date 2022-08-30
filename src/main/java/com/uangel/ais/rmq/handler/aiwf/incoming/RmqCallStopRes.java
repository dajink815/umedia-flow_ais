package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.rmq.handler.RmqIncomingMessage;
import com.uangel.ais.rmq.type.RmqMsgType;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.session.state.RmqState;
import com.uangel.protobuf.CallStopRes;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqCallStopRes extends RmqIncomingMessage<CallStopRes> {
    static final Logger log = LoggerFactory.getLogger(RmqCallStopRes.class);

    public RmqCallStopRes(Message message) {
        super(message);
    }

    @Override
    public void handle() {

        String callId = body.getCallId();
        if (RmqMsgType.isRmqFail(getReasonCode())) {
            log.warn("() ({}) () CallStopRes Fail - {} ({})", callId, getReason(), getReasonCode());
        }

        // get CallInfo -> lock -> Check RmqState
        CallInfo callInfo = CallManager.getInstance().getCallInfo(callId);
        if (callInfo == null) {
            // 중간에 세션 정리된 상태
            log.warn("() ({}) () CalStopRes Fail to Find Session", callId);
            return;
        }

        try {
            callInfo.lock();

            if (!RmqState.STOP.equals(callInfo.getRmqState())) {
                log.warn("{}Received CallStopRes, MisMatch RmqState [{}]", callInfo.getLogHeader(), callInfo.getRmqState());
            }
            callInfo.setCallState(CallState.IDLE);
            callInfo.setRmqState(RmqState.IDLE);
        } finally {
            callInfo.unlock();
        }
    }
}
