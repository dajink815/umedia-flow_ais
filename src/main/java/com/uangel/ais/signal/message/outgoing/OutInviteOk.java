package com.uangel.ais.signal.message.outgoing;

import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.signal.process.outgoing.SipCreateHeader;
import com.uangel.ais.signal.process.outgoing.SipCreateMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.header.ContactHeader;
import stack.java.uangel.sip.message.Response;

import java.text.ParseException;

/**
 * @author dajin kim
 */
public class OutInviteOk {
    static final Logger log = LoggerFactory.getLogger(OutInviteOk.class);

    public OutInviteOk() {
        // nothing
    }

    public Response send(CallInfo callInfo) {
        Response response = null;

        if (callInfo == null) {
            log.warn("() () () OutInviteOk.send - CallInfo is Null");
            return null;
        }

        ServerTransaction st = callInfo.getInviteSt();
        if (st == null) {
            log.warn("{}Server Transaction is null. Can't send InviteOk response.", callInfo.getLogHeader());
            return null;
        }

        callInfo.setCallState(CallState.CONNECT);

        try {
            SipCreateMsg sipCreateMsg = new SipCreateMsg();
            response = sipCreateMsg.createResponse(st.getRequest(), Response.OK, callInfo);

            // Optional Header, Session-Expire

            // SDP, Content-Type
            SipCreateHeader sipCreateHeader = new SipCreateHeader();
            sipCreateHeader.createSdpContentType(callInfo.getSdp(), response);

            // ??
            // Contact, Max-Forwards, Allow
            response.addHeader(sipCreateHeader.createContactHeader());
            response.addHeader(sipCreateHeader.createMaxForwardsHeader());
            response.addHeader(sipCreateHeader.createAllowHeader());

            st.sendResponse(response);
            st.terminate();
        } catch (Exception e) {
            log.error("{}OutInviteOk.send.Exception ", callInfo.getLogHeader(), e);
        }

        return response;
    }
}
