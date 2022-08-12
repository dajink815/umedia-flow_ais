package com.uangel.ais.rmq.handler;

import com.uangel.ais.rmq.common.RmqMsgType;
import com.uangel.ais.rmq.handler.aim.outgoing.*;
import com.uangel.ais.rmq.handler.aiwf.outgoing.*;
import com.uangel.ais.session.model.CallInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqMsgSender {
    static final Logger log = LoggerFactory.getLogger(RmqMsgSender.class);
    private static RmqMsgSender sender;

    private RmqMsgSender() {
        // nothing
    }

    public static RmqMsgSender getInstance() {
        if (sender == null)
            sender = new RmqMsgSender();
        return sender;
    }

    // AIM
    public void sendMLoginRes(String tId) {
        RmqMLoginRes res = new RmqMLoginRes();
        res.send(tId, RmqMsgType.M_LOGIN_RES);
    }

    public void sendMHbRes(String tId) {
        RmqMHbRes res = new RmqMHbRes();
        res.send(tId, RmqMsgType.M_HB_RES);
    }

    public void sendOffer(CallInfo callInfo) {
        RmqOfferReq req = new RmqOfferReq();
        req.send(callInfo, RmqMsgType.OFFER_REQ);
    }

    public void sendNego(CallInfo callInfo) {
        RmqNegoReq req = new RmqNegoReq();
        req.send(callInfo, RmqMsgType.NEGO_REQ);
    }

    public void sendHangup(CallInfo callInfo) {
        RmqHangupReq req = new RmqHangupReq();
        req.send(callInfo, RmqMsgType.HANGUP_REQ);
    }

    // todo CallInfo Null 메시지 전송 케이스?

    // AIWF
    public void sendWHbRes(String tId) {
        RmqWHbRes res = new RmqWHbRes();
        res.send(tId, RmqMsgType.W_HB_RES);
    }

    public void sendCallIncoming(CallInfo callInfo) {
        RmqCallIncomingReq req = new RmqCallIncomingReq();
        req.send(callInfo, RmqMsgType.CALL_INCOMING_REQ);
    }

    public void sendCallStart(CallInfo callInfo) {
        RmqCallStartReq req = new RmqCallStartReq();
        req.send(callInfo, RmqMsgType.CALL_START_REQ);
    }

    public void sendCallCloseRes(String tId, CallInfo callInfo) {
        RmqCallCloseRes res = new RmqCallCloseRes();
        res.send(tId, callInfo, RmqMsgType.CALL_CLOSE_RES);
    }

    // CallCloseRes Fail?

    // CallStop 종료 사유 전달? reasonCode 로? bodyField 추가?
    public void sendCallStop(CallInfo callInfo) {
        RmqCallStopReq req = new RmqCallStopReq();
        req.send(callInfo, RmqMsgType.CALL_STOP_REQ);
    }

    public void sendCallStop(CallInfo callInfo, int reasonCode, String reason) {
        RmqCallStopReq req = new RmqCallStopReq();
        req.send(callInfo, RmqMsgType.CALL_STOP_REQ, reasonCode, reason);
    }
}
