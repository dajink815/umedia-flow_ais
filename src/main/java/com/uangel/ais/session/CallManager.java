package com.uangel.ais.session;

import com.uangel.ais.session.model.CallInfo;
import com.uangel.ais.session.type.CallType;
import com.uangel.ais.signal.util.TerminateTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author dajin kim
 */
public class CallManager {
    static final Logger log = LoggerFactory.getLogger(CallManager.class);
    private static final ConcurrentHashMap<String, CallInfo> callInfoMap = new ConcurrentHashMap<>();
    private static CallManager callManager = null;

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
        // INBOUND 호만 고려
        callInfo.setCallType(CallType.INBOUND);

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
        CallInfo deleteCall = callInfoMap.remove(callId);
        if (deleteCall != null) {
            TerminateTransaction.sipTransactionTerminate(deleteCall);
            log.warn("CallInfo [{}] Removed", callId);
        }
    }

    public List<String> getCallIds() {
        synchronized (callInfoMap){
            return new ArrayList<>(callInfoMap.keySet());
        }
    }

    public ConcurrentMap<String, CallInfo> getCallInfoMap() {
        return callInfoMap;
    }

    public int getCallInfoSize() {
        return callInfoMap.size();
    }

    public boolean checkLongCall(CallInfo callInfo, int timer) {
        if (callInfo == null || timer <= 0) return false;
        long createTime = callInfo.getCreateTime();
        return createTime > 0 && createTime + timer < System.currentTimeMillis();
    }

    public boolean checkRmqTimeout(CallInfo callInfo, int timer) {
        if (callInfo == null || timer <= 0) return false;
        long rmqSendTime = callInfo.getLastRmqTime();
        return rmqSendTime > 0 && rmqSendTime + timer < System.currentTimeMillis();
    }
}
