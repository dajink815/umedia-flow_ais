package com.uangel.ais.signal.process.incoming;

import com.uangel.ais.signal.message.incoming.InByeRes;
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
        log.info("Incoming {}Ok Response [\r\n{}]", method, response);

        if (Request.BYE.equals(method)) {
            InByeRes inByeRes = new InByeRes();
            inByeRes.receiveOk(responseEvent);
        } else if(Request.OPTIONS.equals(method)) {
            // Outgoing Options?
        } else {
            log.warn("SipResponseModule.inOk - Receive Other Message [{} {}]", method, response.getStatusCode());
        }
    }

    protected void inError(ResponseEvent responseEvent, String method) {
        Response response = responseEvent.getResponse();
        log.info("Incoming Error Response [\r\n{}]", response);

        if (Request.BYE.equals(method)) {
            InByeRes inByeRes = new InByeRes();
            inByeRes.receiveErr(responseEvent);
        } else {
            log.warn("SipResponseModule.inError - Receive Other Message [{} {}]", method, response.getStatusCode());
        }
    }
}
