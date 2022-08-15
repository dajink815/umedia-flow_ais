package com.uangel.ais.rmq.handler.aiwf.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aiwf.RmqAiwfOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.protobuf.CallStartReq;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;

/**
 * @author dajin kim
 */
public class RmqCallStartReq extends RmqAiwfOutgoing {

    public RmqCallStartReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .setCallStartReq(CallStartReq.newBuilder()
                        .setCallId(callInfo.getCallId()))
                .build();

        return sendTo(msg);
    }
}
