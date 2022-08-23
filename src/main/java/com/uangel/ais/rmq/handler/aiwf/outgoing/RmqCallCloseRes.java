package com.uangel.ais.rmq.handler.aiwf.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aiwf.RmqAiwfOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.protobuf.CallCloseRes;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;

/**
 * @author dajin kim
 */
public class RmqCallCloseRes extends RmqAiwfOutgoing {

    public RmqCallCloseRes() {
        // nothing
    }

    public boolean send(String tId, CallInfo callInfo, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        headerBuilder.setTId(tId);

        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .setCallCloseRes(CallCloseRes.newBuilder()
                        .setCallId(callInfo.getCallId()))
                .build();

        return sendTo(msg);
    }

    public boolean send(String tId, int reasonCode, String reason, String callId, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        headerBuilder.setTId(tId);

        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .setCallCloseRes(CallCloseRes.newBuilder()
                        .setCallId(callId))
                .build();

        return sendTo(msg);
    }
}
