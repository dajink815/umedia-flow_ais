package com.uangel.ais.rmq.handler.aim.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aim.RmqAimOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.rmq.message.NegoReq;
import com.uangel.rmq.message.RmqHeader;
import com.uangel.rmq.message.RmqMessage;

/**
 * @author dajin kim
 */
public class RmqNegoReq extends RmqAimOutgoing {

    public RmqNegoReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        RmqHeader.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        RmqMessage msg = RmqMessage.newBuilder()
                .setHeader(headerBuilder.build())
                .setNegoReq(NegoReq.newBuilder()
                        .setCallId(callInfo.getCallId())
                        .setSdp(callInfo.getSdp()))
                .build();

        return sendTo(msg);
    }
}
