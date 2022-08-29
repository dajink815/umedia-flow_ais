package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.rmq.type.RmqMsgType;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.RmqState;
import com.uangel.protobuf.CallStartRes;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqCallStartRes {
    static final Logger log = LoggerFactory.getLogger(RmqCallStartRes.class);

    public RmqCallStartRes() {
        // nothing
    }

    public void handle(Message msg) {

        Header header = msg.getHeader();
        CallStartRes res = msg.getCallStartRes();
        // res check isEmpty

        String callId = res.getCallId();
        if (RmqMsgType.isRmqFail(header.getReasonCode())) {
            log.warn("() ({}) () CallStartRes Fail - {} ({})", callId, header.getReason(), header.getReasonCode());
        }

        // get CallInfo -> lock -> Check RmqState
        CallInfo callInfo = CallManager.getInstance().getCallInfo(callId);
        if (callInfo == null) {
            // 중간에 세션 정리된 상태
            log.warn("() ({}) () CallStartRes Fail Find Session", callId);
            return;
        }

        try {
            callInfo.lock();

            if (!RmqState.START.equals(callInfo.getRmqState())) {
                log.warn("{}Received CallStartRes, MisMatch RmqState [{}]", callInfo.getLogHeader(), callInfo.getRmqState());
            }
        } finally {
            callInfo.unlock();
        }
    }
}
