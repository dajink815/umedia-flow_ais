package com.uangel.ais.signal.message.incoming;

import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.signal.process.outgoing.SipOutgoingModule;
import lib.java.handler.sip.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.header.FromHeader;
import stack.java.uangel.sip.header.ToHeader;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.module.SipMessageParser;

import static lib.java.handler.sip.header.SIPHeaderNames.FROM;
import static lib.java.handler.sip.header.SIPHeaderNames.TO;

/**
 * @author dajin kim
 */
public class InInvite extends SipMessageParser {
    static final Logger log = LoggerFactory.getLogger(InInvite.class);
    private static final CallManager callManager = CallManager.getInstance();

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
        log.debug("() ({}) () InInvite To [User: {}, Address: {}, Ip: {}, Port: {}]",
                callId, toUser, toAddress, toIp, toPort);

        String fromUser = getFromHeaderUser(request);
        String fromAddress = ((FromHeader)request.getHeader(FROM)).getAddress().toString();
        String fromIp = getFromHeaderIp(request);
        int fromPort = getFromHeaderPort(request);
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

        // 오류 처리 먼저?


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


        // 내부 오류 체크 - Error Response

        // 1. RMQ Down

        // 2. AIM 연동 실패

        // 3. AIWF 연동실패/status Fail



        // Parse SDP


        // Dialog Transaction


        // Send CallIncomingReq
        RmqMsgSender sender = RmqMsgSender.getInstance();
        sender.sendCallIncoming(callInfo);
    }
}
