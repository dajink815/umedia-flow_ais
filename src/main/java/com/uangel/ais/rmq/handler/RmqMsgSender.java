package com.uangel.ais.rmq.handler;

import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.RmqState;
import com.uangel.protobuf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

/**
 * @author dajin kim
 */
public class RmqMsgSender extends RmqOutgoingMessage {
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
        sendToAim(new MessageBuilder()
                .setBody(MLoginRes.newBuilder().build())
                .settId(tId));
    }

    public void sendMHbRes(String tId) {
        sendToAim(new MessageBuilder()
                .setBody(MHbRes.newBuilder().build())
                .settId(tId));
    }

    public void sendOffer(CallInfo callInfo) {
        callInfo.setRmqState(RmqState.OFFER_REQ);
        callInfo.updateLastRmqTime();

        sendToAim(new MessageBuilder()
                .setBody(OfferReq.newBuilder()
                        .setCallId(callInfo.getCallId())
                        .setFromNo(callInfo.getFromMdn())
                        .setToNo(callInfo.getToMdn())
                        .setSdp(callInfo.getSdp()).build()));
    }

    public void sendNego(CallInfo callInfo) {
        callInfo.setRmqState(RmqState.NEGO_REQ);
        callInfo.updateLastRmqTime();

        sendToAim(new MessageBuilder()
                .setBody(NegoReq.newBuilder()
                        .setCallId(callInfo.getCallId())
                        .setSdp(callInfo.getSdp()).build()));

    }

    public void sendHangup(CallInfo callInfo) {
        if (notSendHangup.contains(callInfo.getRmqState())) {

            return;
        }

        sendToAim(new MessageBuilder()
                .setBody(HangupReq.newBuilder()
                        .setCallId(callInfo.getCallId()).build()));

    }

    // todo CallInfo Null 메시지 전송 케이스?

    // AIWF
    public void sendWHbRes(String tId) {
        sendToAiwf(new MessageBuilder()
                .setBody(WHbRes.newBuilder().build())
                .settId(tId));
    }

    public void sendCallIncoming(CallInfo callInfo) {
        callInfo.setRmqState(RmqState.INCOMING);
        callInfo.updateLastRmqTime();

        sendToAiwf(new MessageBuilder()
                .setBody(CallIncomingReq.newBuilder()
                        .setCallId(callInfo.getCallId())
                        .setFromNo(callInfo.getFromMdn())
                        .setToNo(callInfo.getToMdn()).build()));
    }

    public void sendCallStart(CallInfo callInfo) {
        // todo 호 종료중이면 Start 송신하지 않음
        callInfo.setRmqState(RmqState.START);
        callInfo.updateLastRmqTime();

        sendToAiwf(new MessageBuilder()
                .setBody(CallStartReq.newBuilder()
                        .setCallId(callInfo.getCallId()).build()));
    }

    public void sendCallCloseRes(String tId, CallInfo callInfo) {
        sendToAiwf(new MessageBuilder()
                .setBody(CallCloseRes.newBuilder()
                        .setCallId(callInfo.getCallId()).build())
                .settId(tId));
    }
    public void sendCallCloseRes(String tId, int reasonCode, String reason, String callId) {
        sendToAiwf(new MessageBuilder()
                .setBody(CallCloseRes.newBuilder()
                        .setCallId(callId).build())
                .settId(tId)
                .setReasonCode(reasonCode)
                .setReason(reason));
    }

    // CallCloseRes Fail?

    // todo CallStop 종료 사유 전달? reasonCode 로? bodyField 추가?
    public void sendCallStop(CallInfo callInfo) {
        callInfo.setRmqState(RmqState.STOP);
        callInfo.updateLastRmqTime();

        sendToAiwf(new MessageBuilder()
                .setBody(CallStopReq.newBuilder()
                        .setCallId(callInfo.getCallId()).build()));
    }
    public void sendCallStop(CallInfo callInfo, int reasonCode, String reason) {
        callInfo.setRmqState(RmqState.STOP);
        callInfo.updateLastRmqTime();

        sendToAiwf(new MessageBuilder()
                .setBody(CallStopReq.newBuilder()
                        .setCallId(callInfo.getCallId()).build())
                .setReasonCode(reasonCode)
                .setReason(reason));
    }
}
