package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqCallStopRes {
    static final Logger log = LoggerFactory.getLogger(RmqCallStopRes.class);

    public RmqCallStopRes() {
        // nothing
    }

    public void handle(Message msg) {
    }
}
