package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.rmq.handler.RmqIncomingMessage;
import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.ReleaseSession;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.protobuf.CallCloseReq;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.uangel.ais.rmq.type.RmqMsgType.REASON_CODE_NO_SESSION;
import static com.uangel.ais.rmq.type.RmqMsgType.REASON_NO_SESSION;

/**
 * @author dajin kim
 */
public class RmqCallCloseReq extends RmqIncomingMessage<CallCloseReq> {
    static final Logger log = LoggerFactory.getLogger(RmqCallCloseReq.class);
    private static final CallManager callManager = CallManager.getInstance();
    private static final AisConfig config = AppInstance.getInstance().getConfig();

    public RmqCallCloseReq(Message msg) {
        super(msg);
    }

    @Override
    public void handle() {
        // 세션 정리

        RmqMsgSender sender = RmqMsgSender.getInstance();

        String callId = body.getCallId();
        CallInfo callInfo = callManager.getCallInfo(callId);
        if (callInfo == null) {
            log.warn("() ({}) () CallCloseReq Fail to Find Session", callId);
            // Send Fail Response
            sender.sendCallCloseRes(getTId(), REASON_CODE_NO_SESSION, REASON_NO_SESSION, callId);
            return;
        }

        // Send Success Response (세션 정리 전 Res 먼저)
        sender.sendCallCloseRes(getTId(), callInfo);

        ReleaseSession releaseSession = new ReleaseSession();
        releaseSession.release(callInfo, config.getAiwfErr());

    }
}
