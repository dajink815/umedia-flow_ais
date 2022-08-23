package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.ReleaseSession;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.MediaStartReq;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.message.Response;

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
        MediaStartReq req = msg.getMediaStartReq();
        // req check isEmpty

        RmqMsgSender sender = RmqMsgSender.getInstance();

        String callId = req.getCallId();
        CallInfo callInfo = callManager.getCallInfo(callId);
        if (callInfo == null) {
            log.warn("() ({}) () CallCloseReq Fail Find Session", callId);
            // Send Fail Response
            sender.sendCallCloseRes(header.getTId(), 100, "Fail", callId);
            return;
        }

        // todo ReleaseCode
        ReleaseSession releaseSession = new ReleaseSession();
        releaseSession.release(callInfo, 400);

        // Send Success Response
        sender.sendCallCloseRes(header.getTId(), callInfo);
    }
}
