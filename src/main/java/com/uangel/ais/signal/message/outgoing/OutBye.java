package com.uangel.ais.signal.message.outgoing;

import com.uangel.ais.service.AppInstance;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.signal.module.SipSignal;
import com.uangel.ais.signal.process.outgoing.SipCreateMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ClientTransaction;
import stack.java.uangel.sip.message.Request;

/**
 * @author dajin kim
 */
public class OutBye {
    static final Logger log = LoggerFactory.getLogger(OutBye.class);
    private static final SipSignal sipSignal = AppInstance.getInstance().getSipSignal();

    public OutBye() {
        // nothing
    }

    public Request send(CallInfo callInfo) {
        try {
            callInfo.setCallState(CallState.BYE);

            SipCreateMsg sipCreateMsg = new SipCreateMsg();
            Request request = sipCreateMsg.createRequest(callInfo, Request.BYE);

            ClientTransaction ct = sipSignal.getSipProvider().getNewClientTransaction(request);
            ct.sendRequest();

            callInfo.setByeCt(ct);
            return request;
        } catch (Exception e) {
            log.error("OutBye.send.Exception ", e);
            return null;
        }
    }

}
