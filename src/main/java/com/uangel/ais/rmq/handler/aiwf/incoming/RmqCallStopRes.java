package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.rmq.common.RmqMsgType;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.session.state.RmqState;
import com.uangel.protobuf.CallStopRes;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqCallStopRes {
    static final Logger log = LoggerFactory.getLogger(RmqCallStopRes.class);

    public RmqCallStopRes() {
        // nothing
    }

    public void handle(Message msg) {

        Header header = msg.getHeader();
        CallStopRes res = msg.getCallStopRes();
        // res check isEmpty

        String callId = res.getCallId();
        int reasonCode = header.getReasonCode();
        if (RmqMsgType.isRmqFail(reasonCode)) {
            log.error("({}) CallStopRes FAIL [{}]", callId, reasonCode);
        }

        // get CallInfo -> lock -> Check RmqState
        CallInfo callInfo = CallManager.getInstance().getCallInfo(callId);
        if (callInfo == null) {

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
