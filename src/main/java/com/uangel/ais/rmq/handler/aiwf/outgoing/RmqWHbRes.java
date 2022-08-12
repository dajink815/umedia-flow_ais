package com.uangel.ais.rmq.handler.aiwf.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aiwf.RmqAiwfOutgoing;
import com.uangel.rmq.message.RmqHeader;
import com.uangel.rmq.message.RmqMessage;

/**
 * @author dajin kim
 */
public class RmqWHbRes extends RmqAiwfOutgoing {

    public RmqWHbRes() {
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
