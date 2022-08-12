package com.uangel.ais.signal.process.outgoing;

import com.uangel.ais.service.AppInstance;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.signal.module.SipSignal;
import lib.java.handler.sip.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.header.ToHeader;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.message.Response;
import stack.java.uangel.sip.module.SipMessageParser;

/**
 * @author dajin kim
 */
public class OutResponse {
    static final Logger log = LoggerFactory.getLogger(OutResponse.class);
    private static final CallManager callManager = CallManager.getInstance();
    private final SipSignal sipSignal = AppInstance.getInstance().getSipSignal();

    private Request request;

    public OutResponse() {
        // nothing
    }

    public OutResponse(Request request) {
        this.request = request;
    }

    // Create Response with Request (CallInfo Null)
    public Response sendResponse(int statusCode) {
        if (request == null) {
            log.warn("");
            return null;
        }

        Response response = null;
        try {
            response = this.sipSignal.getMessageFactory().createResponse(statusCode, request);

            // To Tag
            if (statusCode != 100 && SipMessageParser.getToTag(request) == null) {
                Utils utils = new Utils();
                String toTag = utils.generateTag();
                ToHeader toHeader = (ToHeader)response.getHeader(ToHeader.NAME);
                toHeader.setTag(toTag);
                String callId = SipMessageParser.getCallId(request);
                log.debug("() ({}) () OutResponse To Tag: {}", callId, toTag);
            }

            //st.sendResponse(response);

        } catch (Exception e) {
            log.error("OutResponse.sendResponse (byRequest & CallInfo Null) ", e);
        }
        return response;
    }

    // Create Response with Request
    public Response sendResponse(String callId, int statusCode) {
        CallInfo callInfo = callManager.getCallInfo(callId);
        if (callInfo == null) {
            log.warn("() ({}) () OutResponse.sendResponse (byRequest, Code:{}) CallInfo is Null", callId, statusCode);
            return sendResponse(statusCode);
        }

/*        if (request == null || st == null) {
            return sendResponse(callInfo, statusCode);
        }*/

        Response response = null;
        try {
            response = this.sipSignal.getMessageFactory().createResponse(statusCode, request);

            // To Tag
            if (statusCode != 100 && SipMessageParser.getToTag(request) == null) {
                String toTag = callInfo.getToTag();
                if (toTag == null) {
                    Utils utils = new Utils();
                    toTag = utils.generateTag();
                }
                ToHeader toHeader = (ToHeader)response.getHeader(ToHeader.NAME);
                toHeader.setTag(toTag);
                log.debug("{}OutResponse To Tag: {}", callInfo.getLogHeader(), toTag);
            }

            // State
            if (statusCode >= Response.BAD_REQUEST) {
                callInfo.setCallState(CallState.ERROR);
            }

            //st.sendResponse(response);
        } catch (Exception e) {
            log.error("OutResponse.sendResponse (byRequest) ", e);
        }


        return response;
    }

    // Create Response with CallInfo
    public Response sendResponse(CallInfo callInfo, int statusCode, String method) {
        Response response = null;

        try {
            CreateSipMsg createSipMsg = new CreateSipMsg();
            response = createSipMsg.createResponse(callInfo, statusCode, method);
            ServerTransaction callInfoSt = callInfo.getSt();
            if (callInfoSt == null)
                return null;

/*            if (response != null) {
                callInfoSt.sendResponse(response);
            }*/
        } catch (Exception e) {
            log.error("OutResponse.sendResponse (byCallInfo) ", e);
        }

        return response;
    }

}
