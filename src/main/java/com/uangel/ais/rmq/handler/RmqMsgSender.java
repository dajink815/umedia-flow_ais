package com.uangel.ais.rmq.handler;

import com.uangel.ais.rmq.common.RmqMsgType;
import com.uangel.ais.rmq.handler.aim.outgoing.*;
import com.uangel.ais.rmq.handler.aiwf.outgoing.*;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.RmqState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

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

    private static final EnumSet<RmqState> notSendHangup = EnumSet.of(
            RmqState.NEW,
            RmqState.INCOMING,
            RmqState.STOP,
            RmqState.IDLE
    );
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
        callInfo.setRmqState(RmqState.OFFER_REQ);
        callInfo.updateLastRmqTime();

        RmqOfferReq req = new RmqOfferReq();
        req.send(callInfo, RmqMsgType.OFFER_REQ);
    }

    public void sendNego(CallInfo callInfo) {
        callInfo.setRmqState(RmqState.NEGO_REQ);
        callInfo.updateLastRmqTime();

        RmqNegoReq req = new RmqNegoReq();
        req.send(callInfo, RmqMsgType.NEGO_REQ);
    }

    public void sendHangup(CallInfo callInfo) {
        if (notSendHangup.contains(callInfo.getRmqState())) {

            return;
        }

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
        callInfo.setRmqState(RmqState.INCOMING);
        callInfo.updateLastRmqTime();

        RmqCallIncomingReq req = new RmqCallIncomingReq();
        req.send(callInfo, RmqMsgType.CALL_INCOMING_REQ);
    }

    public void sendCallStart(CallInfo callInfo) {
        // todo 호 종료중이면 Start 송신하지 않음
        callInfo.setRmqState(RmqState.START);
        callInfo.updateLastRmqTime();

        RmqCallStartReq req = new RmqCallStartReq();
        req.send(callInfo, RmqMsgType.CALL_START_REQ);
    }

    public void sendCallCloseRes(String tId, CallInfo callInfo) {
        RmqCallCloseRes res = new RmqCallCloseRes();
        res.send(tId, callInfo, RmqMsgType.CALL_CLOSE_RES);
    }
    public void sendCallCloseRes(String tId, int reasonCode, String reason, String callId) {
        RmqCallCloseRes res = new RmqCallCloseRes();
        res.send(tId, reasonCode, reason, callId, RmqMsgType.CALL_CLOSE_RES);
    }

    // CallCloseRes Fail?

    // todo CallStop 종료 사유 전달? reasonCode 로? bodyField 추가?
    public void sendCallStop(CallInfo callInfo) {
        callInfo.setRmqState(RmqState.STOP);
        callInfo.updateLastRmqTime();

        RmqCallStopReq req = new RmqCallStopReq();
        req.send(callInfo, RmqMsgType.CALL_STOP_REQ);
    }

    public void sendCallStop(CallInfo callInfo, int reasonCode, String reason) {
        callInfo.setRmqState(RmqState.STOP);
        callInfo.updateLastRmqTime();

        RmqCallStopReq req = new RmqCallStopReq();
        req.send(callInfo, RmqMsgType.CALL_STOP_REQ, reasonCode, reason);
    }
}
