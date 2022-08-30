package com.uangel.ais.signal.process.outgoing;

import com.uangel.ais.service.AppInstance;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.signal.module.SipSignal;
import com.uangel.ais.util.StringUtil;
import lib.java.handler.sip.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.InvalidArgumentException;
import stack.java.uangel.sip.address.URI;
import stack.java.uangel.sip.header.*;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.message.Response;
import stack.java.uangel.sip.module.SipMessageParser;

import java.text.ParseException;

/**
 * @author dajin kim
 */
public class SipCreateMsg extends SipCreateHeader {
    static final Logger log = LoggerFactory.getLogger(SipCreateMsg.class);
    private static final CallManager callManager = CallManager.getInstance();
    private final SipSignal sipSignal = AppInstance.getInstance().getSipSignal();

    public SipCreateMsg() {
        // nothing
    }

    // Create Response with Request
    public Response createResponse(String callId, Request request, int statusCode) {
        CallInfo callInfo = callManager.getCallInfo(callId);
        if (callInfo == null) {
            log.warn("() ({}) () OutResponse.sendResponse (byRequest, Code:{}) CallInfo is Null", callId, statusCode);
        }
        return createResponse(request, statusCode, callInfo);
    }

    // Create Response with Request
    public Response createResponse(Request request, int statusCode, CallInfo callInfo) {
        Response response;
        try {
            response = this.sipSignal.getMessageFactory().createResponse(statusCode, request);

            // To Tag
            if (statusCode != 100 && SipMessageParser.getToTag(request) == null) {
                String toTag = getToTag(callInfo);
                ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
                toHeader.setTag(toTag);
                String callId = SipMessageParser.getCallId(request);
                log.debug("() ({}) () OutResponse To Tag: {}", callId, toTag);

                if (callInfo != null && callInfo.getToTag() == null)
                    callInfo.setToTag(toTag);
            }

            // State
            if (callInfo != null && statusCode >= Response.BAD_REQUEST
                    && (!callInfo.isRecvCancel() || statusCode != Response.REQUEST_TERMINATED)) {
                callInfo.setCallState(CallState.ERROR);
            }

        } catch (Exception e) {
            log.error("OutResponse.createResponse.byRequest ", e);
            response = null;
        }
        return response;
    }

    // Create Response with CallInfo
    public Response createResponse(CallInfo callInfo, int code, String method) {
        Response response;
        try {
            // todo Error State
            CallIdHeader callIdHeader = createCallIdHeader(callInfo.getCallId());
            CSeqHeader cSeqHeader = createCSeqHeader(method, callInfo.getCSeq());
            FromHeader fromHeader = createFromHeader(callInfo.getFromAddress(), callInfo.getFromTag());
            ToHeader toHeader = createToHeader(callInfo.getToAddress(), callInfo.getToTag());
            MaxForwardsHeader mx = createMaxForwardsHeader();
            response = sipSignal.getMessageFactory().createResponse(code, callIdHeader, cSeqHeader, fromHeader, toHeader, callInfo.getViaHeader(), mx);
        } catch (Exception e) {
            log.error("SipCreateReqRes.createResponse", e);
            response = null;
        }

        return response;
    }

    // Create Request with CallInfo
    public Request createRequest(CallInfo callInfo, String method) throws ParseException, InvalidArgumentException {
        Request request;
        URI reqURI;
        ToHeader toHeader;
        FromHeader fromHeader;

        // Inbound 만 고려
        if (StringUtil.isNull(callInfo.getContact())) {
            reqURI = createURIbyAddress(callInfo.getFromAddress());
        } else {
            reqURI = createSipURI(callInfo.getContact());
        }

        fromHeader = createFromHeader(callInfo.getToAddress(), callInfo.getToTag());
        toHeader = createToHeader(callInfo.getFromAddress(), callInfo.getFromTag());

        CallIdHeader callIdHeader = createCallIdHeader(callInfo.getCallId());
        long cSeq = callInfo.getCSeq() + 1;
        CSeqHeader cSeqHeader = createCSeqHeader(method, cSeq);
        callInfo.setCSeq(cSeq);

        // Create Request
        request = sipSignal.getMessageFactory().createRequest(reqURI, method, callIdHeader, cSeqHeader, fromHeader, toHeader, createVia(), createMaxForwardsHeader());

        // Route Headers?

        // Contact
        request.addHeader(createContactHeader());
        return request;
    }

    private String getToTag(CallInfo callInfo) {
        String toTag;
        if (callInfo == null || callInfo.getToTag() == null) {
            Utils utils = new Utils();
            toTag = utils.generateTag();
        } else {
            toTag = callInfo.getToTag();
        }
        return toTag;
    }

}
