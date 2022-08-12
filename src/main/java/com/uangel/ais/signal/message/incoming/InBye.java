package com.uangel.ais.signal.message.incoming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.ServerTransaction;

/**
 * @author dajin kim
 */
public class InBye {
    static final Logger log = LoggerFactory.getLogger(InBye.class);

    public InBye() {
        // nothing
    }

    public void receive(RequestEvent requestEvent, ServerTransaction st) {

    }
}
