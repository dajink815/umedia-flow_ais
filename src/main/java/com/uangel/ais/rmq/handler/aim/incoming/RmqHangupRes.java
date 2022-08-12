package com.uangel.ais.rmq.handler.aim.incoming;

import com.uangel.rmq.message.RmqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqHangupRes {
    static final Logger log = LoggerFactory.getLogger(RmqHangupRes.class);

    public RmqHangupRes() {
        // nothing
    }

    public void handle(RmqMessage msg) {
    }
}
