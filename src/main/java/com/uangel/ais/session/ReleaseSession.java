package com.uangel.ais.session;

import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.session.type.CallType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class ReleaseSession {
    static final Logger log = LoggerFactory.getLogger(ReleaseSession.class);
    private static ReleaseSession releaseSession = null;

    private ReleaseSession() {
        // nothing
    }

    public static ReleaseSession getInstance() {
        if (releaseSession == null)
            releaseSession = new ReleaseSession();
        return releaseSession;
    }

    public void release(CallInfo callInfo, int releaseCode) {
        if (callInfo == null) {
            log.warn("ReleaseSession CallInfo is Null");
            // CallInfo Null 때 세션 정리?
            return;
        }

        try {
            callInfo.lock();

            CallType callType = callInfo.getCallType();
            CallState callState = callInfo.getCallState();
/*            log.info("{}ReleaseSession : {} [{}], [{}] (State:{} {})", callInfo.getLogHeader(),
                    callType, stopCode, reason, callState, callInfo.getSubState());*/

            // IDLE state return
            if (callState.equals(CallState.IDLE)) {
                return;
            }

            if (callState.equals(CallState.CONNECT)) {
                // BYE

            } else {


            }





        } catch (Exception e) {
            log.error("ReleaseSession.release.Exception ", e);
        } finally {
            callInfo.unlock();
        }

    }


}
