package com.uangel.ais.rmq.handler.aiwf.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aiwf.RmqAiwfOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.protobuf.CallStopReq;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;

/**
 * @author dajin kim
 */
public class RmqCallStopReq extends RmqAiwfOutgoing {

    public RmqCallStopReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .setCallStopReq(CallStopReq.newBuilder()
                        .setCallId(callInfo.getCallId()))
                .build();

        return sendTo(msg);
    }

    public boolean send(CallInfo callInfo, String msgType, int reasonCode, String reason) {
        Header.Builder headerBuilder = RmqBuilder.getFailHeader(msgType, reason, reasonCode);
        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .setCallStopReq(CallStopReq.newBuilder()
                        .setCallId(callInfo.getCallId()))
                .build();

        return sendTo(msg);

    }
}
