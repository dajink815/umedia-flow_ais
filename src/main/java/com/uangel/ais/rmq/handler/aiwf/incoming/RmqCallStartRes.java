package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.rmq.common.RmqMsgType;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.RmqState;
import com.uangel.ais.signal.process.outgoing.SipOutgoingModule;
import com.uangel.ais.util.SleepUtil;
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
        int reasonCode = header.getReasonCode();
        if (RmqMsgType.isRmqFail(reasonCode)) {
            log.error("({}) CallStartRes FAIL [{}]", callId, reasonCode);
        }

        // get CallInfo -> lock -> Check RmqState
        CallInfo callInfo = CallManager.getInstance().getCallInfo(callId);
        if (callInfo == null) {

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


        SleepUtil.trySleep(5000);
        SipOutgoingModule.getInstance().outBye(callInfo);
    }
}
