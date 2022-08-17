package com.uangel.ais.signal.process.incoming;

import com.uangel.ais.signal.message.incoming.InAck;
import com.uangel.ais.signal.message.incoming.InBye;
import com.uangel.ais.signal.message.incoming.InCancel;
import com.uangel.ais.signal.message.incoming.InInvite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.message.Request;

/**
 * @author dajin kim
 */
public class SipRequestModule extends IncomingCheck {
    static final Logger log = LoggerFactory.getLogger(SipRequestModule.class);
    private static final String TRANSACTION_NULL = "create ServerTransaction Exception. serverTransaction is null";

    protected SipRequestModule() {
        // nothing
    }

    protected void inInvite(RequestEvent requestEvent) {
        ServerTransaction st = setServerTransaction(requestEvent);
        if (st == null) {
            log.error(TRANSACTION_NULL);
            return;
        }

        Request request = requestEvent.getRequest();
        log.info("Incoming Invite Request [\r\n{}]", request);

        InInvite inInvite = new InInvite();
        inInvite.receive(requestEvent, st);
    }

    protected void inAck(RequestEvent requestEvent) {
        ServerTransaction st = setServerTransaction(requestEvent);
        if (st == null) {
            log.error(TRANSACTION_NULL);
            return;
        }

        Request request = requestEvent.getRequest();
        log.info("Incoming Ack Request [\r\n{}]", request);

        InAck inAck = new InAck();
        inAck.receive(requestEvent);
    }

    protected void inCancel(RequestEvent requestEvent) {
        ServerTransaction st = setServerTransaction(requestEvent);
        if (st == null) {
            log.error(TRANSACTION_NULL);
            return;
        }

        Request request = requestEvent.getRequest();
        log.info("Incoming Cancel Request [\r\n{}]", request);

        InCancel inCancel = new InCancel();
        inCancel.receive(requestEvent, st);
    }

    protected void inBye(RequestEvent requestEvent) {
        ServerTransaction st = setServerTransaction(requestEvent);
        if (st == null) {
            log.error(TRANSACTION_NULL);
            return;
        }

        Request request = requestEvent.getRequest();
        log.info("Incoming Bye Request [\r\n{}]", request);

        InBye inBye = new InBye();
        inBye.receive(requestEvent, st);
    }

    protected void inOptions(RequestEvent requestEvent) {
        ServerTransaction st = setServerTransaction(requestEvent);
        if (st == null) {
            log.error(TRANSACTION_NULL);
            return;
        }

        Request request = requestEvent.getRequest();
        //log.info("Incoming Options Request [\r\n{}]", request);


    }


    protected void inUpdate(RequestEvent requestEvent) {
        ServerTransaction st = setServerTransaction(requestEvent);
        if (st == null) {
            log.error(TRANSACTION_NULL);
            return;
        }

        Request request = requestEvent.getRequest();
        log.info("Incoming Update Request [\r\n{}]", request);


    }

}
