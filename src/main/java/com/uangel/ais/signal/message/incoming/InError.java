package com.uangel.ais.signal.message.incoming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ResponseEvent;

/**
 * @author dajin kim
 */
public class InError {
    static final Logger log = LoggerFactory.getLogger(InError.class);

    public InError() {
        // nothing
    }

    public void receive(ResponseEvent responseEvent) {

    }
}
