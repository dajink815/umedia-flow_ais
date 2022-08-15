package com.uangel.ais.rmq.handler.aim.incoming;

import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqNegoRes {
    static final Logger log = LoggerFactory.getLogger(RmqNegoRes.class);

    public RmqNegoRes() {
        // nothing
    }

    public void handle(Message msg) {
        // negoRes Fail -> Error Response -> hangup, CallStop

        // AIM -> sdp 받아서 200ok

        // Ack 수신 후 CallStart
    }
}
