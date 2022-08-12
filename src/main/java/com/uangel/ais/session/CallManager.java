package com.uangel.ais.session;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.session.model.CallInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author dajin kim
 */
public class CallManager {
    static final Logger log = LoggerFactory.getLogger(CallManager.class);
    private static final ConcurrentHashMap<String, CallInfo> callInfoMap = new ConcurrentHashMap<>();
    private static CallManager callManager = null;
    private final AisConfig config = AppInstance.getInstance().getConfig();

    private CallManager() {
        // nothing
    }

    public static CallManager getInstance() {
        if (callManager == null) {
            callManager = new CallManager();
        }
        return callManager;
    }

    public CallInfo createCallInfo(String callId) {
        if(callId == null) return null;
        if (callInfoMap.containsKey(callId)) {
            log.error("CallInfo [{}] Exist", callId);
            return null;
        }

        CallInfo callInfo = new CallInfo(callId);
        // Set Field

        callInfoMap.put(callId, callInfo);
        log.warn("CallInfo [{}] Created", callId);
        return callInfo;

    }

    public CallInfo getCallInfo(String callId) {
        if(callId == null) return null;
        CallInfo callInfo = callInfoMap.get(callId);
        if (callInfo == null) {
            log.warn("CallInfo [{}] Null", callId);
        }
        return callInfo;
    }

    // Transaction 정리
    public void deleteCallInfo(String callId) {
        if (callId == null) return;
        if (callInfoMap.remove(callId) != null) {
            log.warn("CallInfo [{}] Removed", callId);
        }
    }

    public ConcurrentMap<String, CallInfo> getCallInfoMap() {
        return callInfoMap;
    }

    public int getCallInfoSize() {
        return callInfoMap.size();
    }
}
