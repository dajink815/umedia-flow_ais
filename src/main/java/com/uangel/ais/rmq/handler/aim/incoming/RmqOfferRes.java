package com.uangel.ais.rmq.handler.aim.incoming;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.rmq.handler.RmqIncomingMessage;
import com.uangel.ais.rmq.type.RmqMsgType;
import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.ReleaseSession;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.RmqState;
import com.uangel.ais.signal.process.outgoing.SipOutgoingModule;
import com.uangel.protobuf.Message;
import com.uangel.protobuf.OfferRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqOfferRes extends RmqIncomingMessage<OfferRes> {
    static final Logger log = LoggerFactory.getLogger(RmqOfferRes.class);
    private static final AisConfig config = AppInstance.getInstance().getConfig();

    public RmqOfferRes(Message msg) {
        super(msg);
    }

    @Override
    public void handle() {

        // get CallInfo -> lock -> Check RmqState
        String callId = body.getCallId();
        CallInfo callInfo = CallManager.getInstance().getCallInfo(callId);

        if (callInfo == null) {
            // 중간에 세션 정리된 상태
            log.warn("() ({}) () OfferRes Fail to Find Session", callId);
            return;
        }

        try {
            callInfo.lock();
            if (!RmqState.OFFER_REQ.equals(callInfo.getRmqState())) {
                // CLOSE 받거나 STOP 전송한 경우 이후 Flow 중지
                log.warn("{}Received OfferRes, MisMatch RmqState [{}]", callInfo.getLogHeader(), callInfo.getRmqState());
                return;
            }
            callInfo.setRmqState(RmqState.OFFER_RES);
        } finally {
            callInfo.unlock();
        }

        // offerRes Fail -> Error Response -> hangup, CallStop
        if (RmqMsgType.isRmqFail(getReasonCode())) {
            log.warn("{}OfferRes Fail - {} ({})", callInfo.getLogHeader(), getReason(), getReasonCode());
            ReleaseSession releaseSession = new ReleaseSession();
            releaseSession.release(callInfo, config.getAimErr());
            return;
        }

        // set SDP -> nego_req & InviteOk
        callInfo.setSdp(body.getSdp());

        SipOutgoingModule.getInstance().outRinging(callInfo);
        RmqMsgSender.getInstance().sendNego(callInfo);
    }
}
