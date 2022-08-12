package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.rmq.message.RmqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqCallCloseReq {
    static final Logger log = LoggerFactory.getLogger(RmqCallCloseReq.class);

    public RmqCallCloseReq() {
        // nothing
    }

    public void handle(RmqMessage msg) {
        // 세션 정리

        // hangup, CallStop
    }
}
