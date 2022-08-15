package com.uangel.ais.rmq.handler.aim.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aim.RmqAimOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.protobuf.HangupReq;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;

/**
 * @author dajin kim
 */
public class RmqHangupReq extends RmqAimOutgoing {

    public RmqHangupReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .setHangupReq(HangupReq.newBuilder()
                        .setCallId(callInfo.getCallId()))
                .build();

        return sendTo(msg);
    }
}
