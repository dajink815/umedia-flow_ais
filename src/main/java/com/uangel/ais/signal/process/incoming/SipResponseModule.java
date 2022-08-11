package com.uangel.ais.signal.process.incoming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ResponseEvent;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.message.Response;

/**
 * @author dajin kim
 */
public class SipResponseModule extends IncomingCheck {
    static final Logger log = LoggerFactory.getLogger(SipResponseModule.class);

    protected void inOk(ResponseEvent responseEvent, String method) {
        Response response = responseEvent.getResponse();
        log.info("Incoming Ok Response [\r\n{}]", response);

        if (Request.BYE.equals(method)) {
            //
        } else {
            log.warn("SipResponseModule.inOk - Receive Other Message [{}]", method);
        }
    }

    protected void inError(ResponseEvent responseEvent, String method) {
        Response response = responseEvent.getResponse();
        log.info("Incoming Error Response [\r\n{}]", response);

        if (Request.BYE.equals(method)) {
            //
        } else {
            log.warn("SipResponseModule.inError - Receive Other Message [{}]", method);
        }
    }
}
