package com.uangel.ais.rmq.handler.aim.incoming;

import com.uangel.rmq.message.RmqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqOfferRes {
    static final Logger log = LoggerFactory.getLogger(RmqOfferRes.class);

    public RmqOfferRes() {
        // nothing
    }

    public void handle(RmqMessage msg) {
        // offerRes Fail -> Error Response -> hangup, CallStop

        // 180 Ringing

        // negoReq
    }
}
