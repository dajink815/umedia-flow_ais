package com.uangel.ais.rmq.handler.aiwf.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aiwf.RmqAiwfOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.protobuf.CallIncomingReq;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;

/**
 * @author dajin kim
 */
public class RmqCallIncomingReq extends RmqAiwfOutgoing {

    public RmqCallIncomingReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .setCallIncomingReq(CallIncomingReq.newBuilder()
                        .setCallId(callInfo.getCallId())
                        .setFromNo(callInfo.getFromMdn())
                        .setToNo(callInfo.getToMdn()))
                .build();

        return sendTo(msg);
    }
}
