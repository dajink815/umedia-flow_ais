package com.uangel.ais.rmq.handler.aim.outgoing;

import com.uangel.ais.rmq.handler.aim.RmqAimOutgoing;
import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import com.uangel.protobuf.OfferReq;

/**
 * @author dajin kim
 */
public class RmqOfferReq extends RmqAimOutgoing {

    public RmqOfferReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        Message msg = Message.newBuilder()
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
