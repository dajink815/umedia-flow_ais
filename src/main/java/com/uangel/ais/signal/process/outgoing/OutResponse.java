package com.uangel.ais.signal.process.outgoing;

import com.uangel.ais.session.model.CallInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.message.Request;
import stack.java.uangel.sip.message.Response;

/**
 * @author dajin kim
 */
public class OutResponse extends CreateSipMsg {
    static final Logger log = LoggerFactory.getLogger(OutResponse.class);

    public OutResponse() {
        // nothing
    }

    // INVITE ERROR RESPONSE?

    // Send Response with Request
    public Response sendResponse(Request request, int statusCode, CallInfo callInfo, ServerTransaction st) {
        Response response = createResponse(request, statusCode, callInfo);

        try {
            if (response != null) {
                st.sendResponse(response);
            }
        } catch (Exception e) {
            log.error("OutResponse.sendResponse (byRequest)", e);
        }

        return response;
    }


    // Send Response with CallInfo
    public Response sendResponse(CallInfo callInfo, int statusCode, String method) {
        Response response = null;

        try {
/*            CreateSipMsg createSipMsg = new CreateSipMsg();
            response = createSipMsg.createResponse(callInfo, statusCode, method);*/

/*            ServerTransaction callInfoSt = callInfo.getSt();
            if (callInfoSt == null)
                return null;*/

/*            if (response != null) {
                callInfoSt.sendResponse(response);
            }*/
        } catch (Exception e) {
            log.error("OutResponse.sendResponse (byCallInfo) ", e);
        }

        return response;
    }


}
