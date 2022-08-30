package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.rmq.handler.RmqIncomingMessage;
import com.uangel.ais.rmq.type.RmqMsgType;
import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.ReleaseSession;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.RmqState;
import com.uangel.protobuf.CallIncomingRes;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqCallIncomingRes extends RmqIncomingMessage<CallIncomingRes> {
    static final Logger log = LoggerFactory.getLogger(RmqCallIncomingRes.class);
    private static final AisConfig config = AppInstance.getInstance().getConfig();

    public RmqCallIncomingRes(Message message) {
        super(message);
    }

    @Override
    public void handle() {

        // get CallInfo -> lock -> Check RmqState
        String callId = body.getCallId();
        CallInfo callInfo = CallManager.getInstance().getCallInfo(callId);

        if (callInfo == null) {
            // 중간에 세션 정리된 상태
            log.warn("() ({}) () IncomingCallRes Fail to Find Session", callId);
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

        if (RmqMsgType.isRmqFail(getReasonCode())) {
            log.warn("() ({}) () CallIncomingRes Fail - {} ({})", callId, getReason(), getReasonCode());
            ReleaseSession releaseSession = new ReleaseSession();
            releaseSession.release(callInfo, config.getAiwfErr());
            return;
        }

        RmqMsgSender.getInstance().sendOffer(callInfo);
    }
}
