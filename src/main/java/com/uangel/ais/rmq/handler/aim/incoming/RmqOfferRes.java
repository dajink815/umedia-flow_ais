package com.uangel.ais.rmq.handler.aim.incoming;

import com.uangel.ais.rmq.type.RmqMsgType;
import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.ReleaseSession;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.RmqState;
import com.uangel.ais.signal.process.outgoing.SipOutgoingModule;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import com.uangel.protobuf.OfferRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqOfferRes {
    static final Logger log = LoggerFactory.getLogger(RmqOfferRes.class);

    public RmqOfferRes() {
        // nothing
    }

    public void handle(Message msg) {

        Header header = msg.getHeader();
        OfferRes res = msg.getOfferRes();
        // res check isEmpty


        // get CallInfo -> lock -> Check RmqState
        String callId = res.getCallId();
        CallInfo callInfo = CallManager.getInstance().getCallInfo(callId);

        if (callInfo == null) {
            // 중간에 세션 정리된 상태
            log.warn("() ({}) () OfferRes Fail Find Session", callId);
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
        if (RmqMsgType.isRmqFail(header.getReasonCode())) {
            // todo OfferRes Fail Error Code
            log.warn("{}OfferRes Fail - {} ({})", callInfo.getLogHeader(), header.getReason(), header.getReasonCode());
            ReleaseSession releaseSession = new ReleaseSession();
            releaseSession.release(callInfo, 415);
            return;
        }

        // set SDP -> nego_req & InviteOk
        callInfo.setSdp(res.getSdp());

        SipOutgoingModule.getInstance().outRinging(callInfo);
        RmqMsgSender.getInstance().sendNego(callInfo);
    }
}
