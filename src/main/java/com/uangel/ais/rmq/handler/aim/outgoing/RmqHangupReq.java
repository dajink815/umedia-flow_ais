package com.uangel.ais.rmq.handler.aim.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aim.RmqAimOutgoing;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.rmq.message.HangupReq;
import com.uangel.rmq.message.RmqHeader;
import com.uangel.rmq.message.RmqMessage;

/**
 * @author dajin kim
 */
public class RmqHangupReq extends RmqAimOutgoing {

    public RmqHangupReq() {
        // nothing
    }

    public boolean send(CallInfo callInfo, String msgType) {
        RmqHeader.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        RmqMessage msg = RmqMessage.newBuilder()
                .setHeader(headerBuilder.build())
                .setHangupReq(HangupReq.newBuilder()
                        .setCallId(callInfo.getCallId()))
                .build();

        return sendTo(msg);
    }
}
