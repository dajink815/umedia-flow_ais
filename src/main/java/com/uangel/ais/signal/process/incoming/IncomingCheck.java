package com.uangel.ais.signal.process.incoming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.*;
import stack.java.uangel.sip.header.CSeqHeader;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.message.Response;

/**
 * @author dajin kim
 */
public class IncomingCheck {
    static final Logger log = LoggerFactory.getLogger(IncomingCheck.class);

    protected IncomingCheck() {
        // nothing
    }

    protected ServerTransaction setServerTransaction(RequestEvent requestEvent) {
        SipProvider sipProvider;
        sipProvider = (SipProvider) requestEvent.getSource();
        Request request = requestEvent.getRequest();

        ServerTransaction st = requestEvent.getServerTransaction();
        if (st == null) try {
            st = sipProvider.getNewServerTransaction(request);
            log.debug("createServerTransaction [{}]", st);
        } catch (TransactionAlreadyExistsException | TransactionUnavailableException e) {
            log.error("IncomingCheck.setServerTransaction", e);
        }

        return st;
    }

    protected boolean checkTidProcess(ResponseEvent responseEvent) {
        ClientTransaction tid = responseEvent.getClientTransaction();

        if (tid == null) {
            Response response = responseEvent.getResponse();
            log.warn("SipResponseModule.checkTidProcess : ClientTransaction is null. Response [{}]", response);
            Dialog dialog = responseEvent.getDialog();

            if (dialog != null && response.getStatusCode() == Response.OK) {
                CSeqHeader cSeqHeader = ((CSeqHeader) response.getHeader(CSeqHeader.NAME));
                try {
                    if (cSeqHeader != null && Request.INVITE.equals(cSeqHeader.getMethod())) {
                        // INVITE 200OK 재전송 방지를 위해 ACK 전송 후 dialog 삭제
                        //Request ackRequest = Outgoing.getInstance().outAck(responseEvent, null);
                        //log.info("SipResponseModule.checkTidProcess sendAck for INVITE dialog [{}]", ackRequest);
                    }
                } catch (Exception e) {
                    log.error("SipResponseModule.checkTidProcess", e);
                }
            }
            log.debug("checkTidProcess return true");
            return true;
        }
        return false;
    }
}
