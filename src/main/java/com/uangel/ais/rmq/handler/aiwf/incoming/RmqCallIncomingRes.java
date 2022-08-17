package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.rmq.common.RmqMsgType;
import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.RmqState;
import com.uangel.protobuf.CallIncomingRes;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqCallIncomingRes {
    static final Logger log = LoggerFactory.getLogger(RmqCallIncomingRes.class);

    public RmqCallIncomingRes() {
        // nothing
    }

    public void handle(Message msg) {

        Header header = msg.getHeader();
        CallIncomingRes res = msg.getCallIncomingRes();
        // res check isEmpty


        // get CallInfo -> lock -> Check RmqState
        String callId = res.getCallId();
        CallInfo callInfo = CallManager.getInstance().getCallInfo(callId);

        if (callInfo == null) {

            // Error Response
            // CallStop

            return;
        }

        try {
            callInfo.lock();
            if (!RmqState.INCOMING.equals(callInfo.getRmqState())) {
                // CLOSE 받거나 STOP 전송한 경우 이후 Flow 중지
                log.warn("{}Received CallIncomingRes, MisMatch RmqState [{}]", callInfo.getLogHeader(), callInfo.getRmqState());
                return;
            }
        } finally {
            callInfo.unlock();
        }

        if (RmqMsgType.isRmqFail(header.getReasonCode())) {
            // CallIncomingRes Fail
            // Error Response
            // CallStop
            return;
        }

        RmqMsgSender.getInstance().sendOffer(callInfo);
    }
}
