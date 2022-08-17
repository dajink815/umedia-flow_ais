package com.uangel.ais.rmq.handler.aim.incoming;

import com.uangel.ais.rmq.common.RmqMsgType;
import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.session.CallManager;
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

            // Error Response
            // hangup, CallStop

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

            return;
        }

        // set SDP -> nego_req & InviteOk
        callInfo.setSdp(res.getSdp());

        SipOutgoingModule.getInstance().outRinging(callInfo);
        RmqMsgSender.getInstance().sendNego(callInfo);
    }
}
