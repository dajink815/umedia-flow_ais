package com.uangel.ais.rmq.handler.aiwf.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aiwf.RmqAiwfOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.rmq.message.CallStopReq;
import com.uangel.rmq.message.RmqHeader;
import com.uangel.rmq.message.RmqMessage;

/**
 * @author dajin kim
 */
public class RmqCallStopReq extends RmqAiwfOutgoing {

    public RmqCallStopReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        RmqHeader.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        RmqMessage msg = RmqMessage.newBuilder()
                .setHeader(headerBuilder.build())
                .setCallStopReq(CallStopReq.newBuilder()
                        .setCallId(callInfo.getCallId()))
                .build();

        return sendTo(msg);
    }

    public boolean send(CallInfo callInfo, String msgType, int reasonCode, String reason) {
        RmqHeader.Builder headerBuilder = RmqBuilder.getFailHeader(msgType, reason, reasonCode);
        RmqMessage msg = RmqMessage.newBuilder()
                .setHeader(headerBuilder.build())
                .setCallStopReq(CallStopReq.newBuilder()
                        .setCallId(callInfo.getCallId()))
                .build();

        return sendTo(msg);

    }
}
