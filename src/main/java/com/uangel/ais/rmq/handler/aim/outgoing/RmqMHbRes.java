package com.uangel.ais.rmq.handler.aim.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aim.RmqAimOutgoing;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import com.uangel.protobuf.MessageBuilder;

/**
 * @author dajin kim
 */
public class RmqMHbRes extends RmqAimOutgoing {

    public RmqMHbRes() {
        // nothing
    }

    public boolean send(String tId, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        headerBuilder.setTId(tId);

        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .build();

        return sendTo(msg);
    }
}
