package com.uangel.ais.rmq.handler.aiwf.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aiwf.RmqAiwfOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.rmq.message.CallCloseRes;
import com.uangel.rmq.message.RmqHeader;
import com.uangel.rmq.message.RmqMessage;

/**
 * @author dajin kim
 */
public class RmqCallCloseRes extends RmqAiwfOutgoing {

    public RmqCallCloseRes() {
        // nothing
    }

    public boolean send(String tId, CallInfo callInfo, String msgType) {
        RmqHeader.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        headerBuilder.setTId(tId);

        RmqMessage msg = RmqMessage.newBuilder()
                .setHeader(headerBuilder.build())
                .setCallCloseRes(CallCloseRes.newBuilder()
                        .setCallId(callInfo.getCallId()))
                .build();

        return sendTo(msg);
    }
}
