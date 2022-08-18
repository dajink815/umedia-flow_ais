package com.uangel.ais.signal.message.incoming;

import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.signal.process.outgoing.SipOutgoingModule;
import com.uangel.ais.signal.util.SipHeaderParser;
import lib.java.handler.sip.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.*;
import stack.java.uangel.sip.header.CSeqHeader;
import stack.java.uangel.sip.header.FromHeader;
import stack.java.uangel.sip.header.ToHeader;
import stack.java.uangel.sip.header.ViaHeader;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.module.SipMessageParser;

import static lib.java.handler.sip.header.SIPHeaderNames.*;

/**
 * @author dajin kim
 */
public class InInvite extends SipMessageParser {
    static final Logger log = LoggerFactory.getLogger(InInvite.class);
    private static final CallManager callManager = CallManager.getInstance();
    private static final AppInstance instance = AppInstance.getInstance();

    public InInvite() {
        // nothing
    }

    public void receive(RequestEvent requestEvent, ServerTransaction st) {
        Request request = requestEvent.getRequest();
        String callId = getCallId(request);

        // RequestLine User/IP/Port?
        String toUser = getToHeaderUser(request);
        String toAddress = ((ToHeader)request.getHeader(TO)).getAddress().toString();
        String toIp = getToHeaderIp(request);
        int toPort = getToHeaderPort(request);
        // For Linphone Test
        if (toPort <= 0) toPort = getRequestLinePort(request);
        log.debug("() ({}) () InInvite To [User: {}, Address: {}, Ip: {}, Port: {}]",
                callId, toUser, toAddress, toIp, toPort);

        String fromUser = getFromHeaderUser(request);
        String fromAddress = ((FromHeader)request.getHeader(FROM)).getAddress().toString();
        String fromIp = getFromHeaderIp(request);
        int fromPort = getFromHeaderPort(request);
        // For Linphone Test
        if (fromPort <= 0) fromPort = ((ViaHeader)request.getHeader(VIA)).getPort();
        log.debug("() ({}) () InInvite From [User: {}, Address: {}, Ip: {}, Port: {}]",
                callId, fromUser, fromAddress, fromIp, fromPort);

        // Send Trying
        SipOutgoingModule sipOutgoing = SipOutgoingModule.getInstance();
        sipOutgoing.outTrying(requestEvent, st);

        // Create CallInfo
        CallInfo callInfo = callManager.createCallInfo(callId);
        if (callInfo == null) {
            // Error Response
            return;
        }

        callInfo.setCallState(CallState.INVITE);
        callInfo.setInviteSt(st);

        // 내부 오류 체크
        // 1. RMQ Down
        if (!instance.isLocalRmqConnect() || !instance.isAiwfRmqConnect()) {

            // Error Response
            // 세션 정리
            return;
        }

        // 2. AIM 연동 실패


        // 3. AIWF 연동실패/status Fail



        String fromTag = getFromTag(request);
        Utils utils = new Utils();
        String toTag = utils.generateTag();

        // Set CallInfo
        callInfo.setToMdn(toUser);
        callInfo.setToAddress(toAddress);
        callInfo.setToIp(toIp);
        callInfo.setToPort(toPort);

        callInfo.setFromMdn(fromUser);
        callInfo.setFromAddress(fromAddress);
        callInfo.setFromIp(fromIp);
        callInfo.setFromPort(fromPort);

        callInfo.setFromTag(fromTag);
        callInfo.setToTag(toTag);
        callInfo.setCallState(CallState.TRYING);

        callInfo.setCSeq(((CSeqHeader) request.getHeader(CSeqHeader.NAME)).getSeqNumber());

        // Request HashMap

        // Parse SDP
        String sdp = null;
        try {
            sdp = getSdp(request);
        } catch (Exception e) {
            log.warn("InInvite.receive.getSdp.Exception (callId: {})", callId);
        }
        callInfo.setSdp(sdp);

        // INVITE Contact = target URI
        String contactStr = SipHeaderParser.getTargetUri(request);
        callInfo.setContact(contactStr);

        // Dialog Transaction
        Dialog dialog;
        try {
            dialog = ((SipProvider) requestEvent.getSource()).getNewDialog(st);
        } catch (SipException e) {
            log.error("InInvite.receive.getNewDialog.Exception (callId: {})", callId, e);
            return;
        }
        callInfo.setDialog(dialog);

        // Send CallIncomingReq
        RmqMsgSender.getInstance().sendCallIncoming(callInfo);
    }

    private void terminateCall(CallInfo callInfo, int errCode) {

    }
}
