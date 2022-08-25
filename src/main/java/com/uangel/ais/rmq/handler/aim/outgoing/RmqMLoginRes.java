package com.uangel.ais.rmq.handler.aim.outgoing;

import com.uangel.ais.rmq.common.RmqBuilder;
import com.uangel.ais.rmq.handler.aim.RmqAimOutgoing;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.MLoginRes;
import com.uangel.protobuf.Message;

/**
 * @author dajin kim
 */
public class RmqMLoginRes extends RmqAimOutgoing {

    public RmqMLoginRes() {
        // nothing
    }

    public boolean send(String tId, String msgType) {
        Header.Builder headerBuilder = RmqBuilder.getDefaultHeader(msgType);
        headerBuilder.setTId(tId);

        Message msg = Message.newBuilder()
                .setHeader(headerBuilder.build())
                .setMLoginRes(MLoginRes.newBuilder())
                .build();

        return sendTo(msg);
    }
}
