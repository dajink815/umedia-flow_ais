package com.uangel.ais.signal.message.incoming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ResponseEvent;

/**
 * @author dajin kim
 */
public class InByeRes {
    static final Logger log = LoggerFactory.getLogger(InByeRes.class);

    public InByeRes() {
        // nothing
    }

    public void receiveOk(ResponseEvent responseEvent) {

    }

    public void receiveErr(ResponseEvent responseEvent) {

    }
}
