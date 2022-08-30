package com.uangel.ais.rmq.handler.aim.incoming;

import com.uangel.ais.rmq.handler.RmqIncomingMessage;
import com.uangel.protobuf.HangupRes;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqHangupRes extends RmqIncomingMessage<HangupRes> {
    static final Logger log = LoggerFactory.getLogger(RmqHangupRes.class);

    public RmqHangupRes(Message msg) {
        super(msg);
    }

    @Override
    public void handle() {

    }
}
