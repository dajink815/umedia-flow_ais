package com.uangel.ais.rmq.handler.aim.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aim.RmqAimOutgoing;
import com.uangel.rmq.message.RmqHeader;
import com.uangel.rmq.message.RmqMessage;

/**
 * @author dajin kim
 */
public class RmqMLoginRes extends RmqAimOutgoing {

    public RmqMLoginRes() {
        // nothing
    }

    public boolean send(String tId, String msgType) {
        RmqHeader.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        headerBuilder.setTId(tId);

        RmqMessage msg = RmqMessage.newBuilder()
                .setHeader(headerBuilder.build())
                .build();

        return sendTo(msg);
    }
}
