package com.uangel.ais.signal.message.incoming;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.service.aim.AimManager;
import com.uangel.ais.service.aim.AimSessionInfo;
import com.uangel.ais.service.aiwf.AiwfManager;
import com.uangel.ais.service.aiwf.AiwfSessionInfo;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.signal.process.outgoing.OutResponse;
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

import java.util.List;

import static lib.java.handler.sip.header.SIPHeaderNames.*;

/**
 * @author dajin kim
 */
public class InInvite extends SipMessageParser {
    static final Logger log = LoggerFactory.getLogger(InInvite.class);
    private static final CallManager callManager = CallManager.getInstance();
    private static final AppInstance instance = AppInstance.getInstance();
    private static final AisConfig config = instance.getConfig();

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
        // todo For Linphone Test
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

        // 내부 오류 체크
        String serviceError = checkServiceStatus(callInfo);
        if (serviceError != null) {
            // log
            log.warn("() ({}) () InInvite Service Error [{}]", callId, serviceError);
            // Error Response
            OutResponse outResponse = new OutResponse();
            outResponse.sendResponse(request, config.getServiceErr(), callInfo, st);
            // Terminate ST
            terminateST(st, callId);
            return;
        }

        callInfo.setCallState(CallState.INVITE);
        callInfo.setInviteSt(st);

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
        callInfo.setRequestHeader(getHeaderMap(request));

        List<ViaHeader> viaHeaderList = SipHeaderParser.createSipHeaderListType(request, ViaHeader.NAME, ViaHeader.class);
        callInfo.setViaHeader(viaHeaderList);

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

    private String checkServiceStatus(CallInfo callInfo) {
        if (callInfo == null)
            return "Fail to Create Session";
        // 1. RMQ Down
        if (!instance.isLocalRmqConnect() || !instance.isAiwfRmqConnect())
            return instance.getRmqStopReason();
        // 2. AIM 연동 실패 (하나의 AIM 만 고려, ID 0)
        AimSessionInfo aimSessionInfo = AimManager.getInstance().getAimSession(0);
        if (aimSessionInfo.isTimeoutFlag())
            return "AIM SERVER DOWN";
        // 3. AIWF 연동실패/status Fail
        AiwfSessionInfo aiwfSessionInfo = AiwfManager.getInstance().getAiwfSession(0);
        if (aiwfSessionInfo.isTimeoutFlag() || !aiwfSessionInfo.isStatus())
            return "AIWF SERVER ERROR";

        return null;
    }

    private void terminateST(ServerTransaction st, String callId) {
        try {
            st.terminate();
        } catch (ObjectInUseException e) {
            log.error("InInvite.terminateST Error", e);
        }
        callManager.deleteCallInfo(callId);
    }
}
