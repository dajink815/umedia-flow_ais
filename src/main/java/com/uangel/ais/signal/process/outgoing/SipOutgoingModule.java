package com.uangel.ais.signal.process.outgoing;

import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.signal.message.outgoing.OutBye;
import com.uangel.ais.signal.message.outgoing.OutError;
import com.uangel.ais.signal.message.outgoing.OutInviteOk;
import com.uangel.ais.signal.message.outgoing.OutTryingRinging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.message.Response;

/**
 * @author dajin kim
 */
public class SipOutgoingModule {
    static final Logger log = LoggerFactory.getLogger(SipOutgoingModule.class);
    private static SipOutgoingModule outgoing = null;

    private SipOutgoingModule() {
        // nothing
    }

    public static SipOutgoingModule getInstance() {
        if (outgoing == null)
            outgoing = new SipOutgoingModule();
        return outgoing;
    }

    public void outBye(CallInfo callInfo) {
        OutBye outBye = new OutBye();
        Request request = outBye.send(callInfo);
        log.info("Outgoing Bye Request [\r\n{}]", request);
    }

    public void outTrying(RequestEvent requestEvent, ServerTransaction st) {
        OutTryingRinging outTrying = new OutTryingRinging();
        Response response = outTrying.sendTrying(requestEvent, st);
        log.info("Outgoing Trying Response [\r\n{}]", response);
    }

    public void outRinging(CallInfo callInfo) {
        OutTryingRinging outRinging = new OutTryingRinging();
        Response response = outRinging.sendRinging(callInfo);
        log.info("Outgoing Ringing Response [\r\n{}]", response);
    }

    public void outOk(CallInfo callInfo) {
        OutInviteOk outInviteOk = new OutInviteOk();
        Response response = outInviteOk.send(callInfo);
        log.info("Outgoing InviteOk Response [\r\n{}]", response);
    }

    public void outError(CallInfo callInfo, int code, String method) {
        OutError outError = new OutError();
        Response response = outError.send(callInfo, code, method);
        log.info("Outgoing Error Response [\r\n{}]", response);
    }
}
