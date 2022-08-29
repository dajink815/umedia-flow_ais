package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.ReleaseSession;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.protobuf.CallCloseReq;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.uangel.ais.rmq.type.RmqMsgType.REASON_CODE_NO_SESSION;
import static com.uangel.ais.rmq.type.RmqMsgType.REASON_NO_SESSION;

/**
 * @author dajin kim
 */
public class RmqCallCloseReq {
    static final Logger log = LoggerFactory.getLogger(RmqCallCloseReq.class);
    private static final CallManager callManager = CallManager.getInstance();

    public RmqCallCloseReq() {
        // nothing
    }

    public void handle(Message msg) {
        // 세션 정리

        Header header = msg.getHeader();
        CallCloseReq req = msg.getCallCloseReq();
        // req check isEmpty

        RmqMsgSender sender = RmqMsgSender.getInstance();

        String callId = req.getCallId();
        CallInfo callInfo = callManager.getCallInfo(callId);
        if (callInfo == null) {
            log.warn("() ({}) () CallCloseReq Fail Find Session", callId);
            // Send Fail Response
            sender.sendCallCloseRes(header.getTId(), REASON_CODE_NO_SESSION, REASON_NO_SESSION, callId);
            return;
        }

        // Send Success Response (세션 정리 전 Res 먼저)
        sender.sendCallCloseRes(header.getTId(), callInfo);

        // todo ReleaseCode
        ReleaseSession releaseSession = new ReleaseSession();
        releaseSession.release(callInfo, 500);

    }
}
