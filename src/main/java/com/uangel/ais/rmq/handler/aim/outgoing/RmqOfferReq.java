package com.uangel.ais.rmq.handler.aim.outgoing;

import com.uangel.ais.rmq.handler.aim.RmqAimOutgoing;
import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.rmq.message.OfferReq;
import com.uangel.rmq.message.RmqHeader;
import com.uangel.rmq.message.RmqMessage;

/**
 * @author dajin kim
 */
public class RmqOfferReq extends RmqAimOutgoing {

    public RmqOfferReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        RmqHeader.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        RmqMessage msg = RmqMessage.newBuilder()
                .setHeader(headerBuilder.build())
                .setOfferReq(OfferReq.newBuilder()
                        .setCallId(callInfo.getCallId())
                        .setFromNo(callInfo.getFromMdn())
                        .setToNo(callInfo.getToMdn())
                        .setSdp(callInfo.getSdp()))
                .build();

        return sendTo(msg);
    }
}
