package com.uangel.ais.service.aiwf;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.service.AppInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dajin kim
 */
public class AiwfManager {
    static final Logger log = LoggerFactory.getLogger(AiwfManager.class);
    private static AiwfManager aiwfManager = null;
    private static final Map<Integer, AiwfSessionInfo> aiwfSessionInfos = new HashMap<>();
    private final int hbTimeout;

    private AiwfManager() {
        log.info("AIWF Manager started");
        hbTimeout = AppInstance.getInstance().getConfig().getHbTimeout();
    }

    public static AiwfManager getInstance() {
        if (aiwfManager == null) {
            aiwfManager = new AiwfManager();
        }
        return aiwfManager;
    }

    public AiwfSessionInfo createAwifSession(int aiwfId) {
        if (aiwfId < 0) return null;

        // AIWF session 중복 체크
        if (aiwfSessionInfos.containsKey(aiwfId)) {
            log.warn("AiwfSessionInfo [{}] Exist", aiwfId);
            return aiwfSessionInfos.get(aiwfId);
        }

        AiwfSessionInfo sessionInfo = new AiwfSessionInfo();
        sessionInfo.updateLastHbTime();

        synchronized (aiwfSessionInfos) {
            aiwfSessionInfos.put(aiwfId, sessionInfo);
        }

        log.warn("New AiwfSessionInfo [{}] Created. Total [{}]", aiwfId, aiwfSessionInfos.size());
        return sessionInfo;
    }

    public AiwfSessionInfo getAiwfSession(int aiwfId) {
        if (aiwfId < 0) return null;
        AiwfSessionInfo sessionInfo = null;
        synchronized (aiwfSessionInfos) {
            if (aiwfSessionInfos.containsKey(aiwfId)) {
                sessionInfo = aiwfSessionInfos.get(aiwfId);
            }
        }

        if (sessionInfo == null) {
            log.warn("AiwfSessionInfo [{}] Null", aiwfId);
        }
        return sessionInfo;
    }

    public void check() {
        AiwfSessionInfo sessionInfo = getAiwfSession(0);
        if (sessionInfo == null) {
            // 생성?
            return;
        }

        // Standby -> Active 절체시 필드값 리셋? (lastHbTime 현재 시간으로)

        if (checkHbTimeout(sessionInfo) && !sessionInfo.isTimeoutFlag()) {
            log.error("[TIMEOUT] AIWF Heartbeat Timeout");
            sessionInfo.setTimeoutFlag(true);
        }
    }

    private boolean checkHbTimeout(AiwfSessionInfo sessionInfo) {
        long lastHbTime = sessionInfo.getLastHbTime();
        return lastHbTime > 0 && lastHbTime + hbTimeout < System.currentTimeMillis();
    }
}
