package com.uangel.ais.rmq.handler.aiwf.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aiwf.RmqAiwfOutgoing;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import com.uangel.protobuf.WHbRes;

/**
 * @author dajin kim
 */
public class RmqWHbRes extends RmqAiwfOutgoing {

    public RmqWHbRes() {
        // nothing
    }

    public boolean send(String tId, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        headerBuilder.setTId(tId);

        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .setWHbRes(WHbRes.newBuilder())
                .build();

        return sendTo(msg);
    }
}
