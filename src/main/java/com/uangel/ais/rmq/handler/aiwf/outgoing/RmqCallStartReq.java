package com.uangel.ais.rmq.handler.aiwf.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aiwf.RmqAiwfOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.rmq.message.CallStartReq;
import com.uangel.rmq.message.RmqHeader;
import com.uangel.rmq.message.RmqMessage;

/**
 * @author dajin kim
 */
public class RmqCallStartReq extends RmqAiwfOutgoing {

    public RmqCallStartReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        RmqHeader.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        RmqMessage msg = RmqMessage.newBuilder()
                .setHeader(headerBuilder.build())
                .setCallStartReq(CallStartReq.newBuilder()
                        .setCallId(callInfo.getCallId()))
                .build();

        return sendTo(msg);
    }
}
