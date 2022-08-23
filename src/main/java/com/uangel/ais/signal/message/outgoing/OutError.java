package com.uangel.ais.signal.message.outgoing;

import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.signal.process.outgoing.SipCreateMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.message.Response;

/**
 * @author dajin kim
 */
public class OutError {
    static final Logger log = LoggerFactory.getLogger(OutError.class);

    public OutError() {
        // nothing
    }

    public Response send(CallInfo callInfo, int code, String method) {
        SipCreateMsg sipCreateMsg = new SipCreateMsg();
        Response response;

        try {
            response = sipCreateMsg.createResponse(callInfo, code, method);
            // INVITE 외의 method?
            ServerTransaction callInfoSt = callInfo.getInviteSt();
            if (callInfoSt == null)
                return null;

            if (response != null) {
                callInfoSt.sendResponse(response);
            }
        } catch (Exception e) {
            log.error("OutError.send.Exception ", e);
            response = null;
        }

        return response;
    }
}
