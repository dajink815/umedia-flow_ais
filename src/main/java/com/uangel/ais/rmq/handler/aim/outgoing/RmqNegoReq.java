package com.uangel.ais.rmq.handler.aim.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aim.RmqAimOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import com.uangel.protobuf.NegoReq;

/**
 * @author dajin kim
 */
public class RmqNegoReq extends RmqAimOutgoing {

    public RmqNegoReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .setNegoReq(NegoReq.newBuilder()
                        .setCallId(callInfo.getCallId())
                        .setSdp(callInfo.getSdp()))
                .build();

        return sendTo(msg);
    }
}
