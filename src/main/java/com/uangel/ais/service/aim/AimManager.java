package com.uangel.ais.service.aim;

import com.uangel.ais.service.AppInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dajin kim
 */
public class AimManager {
    static final Logger log = LoggerFactory.getLogger(AimManager.class);
    private static AimManager aimManager = null;
    private static final Map<Integer, AimSessionInfo> aimSessionInfos = new HashMap<>();
    private final int hbTimeout;

    private AimManager() {
        log.info("AIM Manager started");
        hbTimeout = AppInstance.getInstance().getConfig().getHbTimeout();
    }

    public static AimManager getInstance() {
        if (aimManager == null) {
            aimManager = new AimManager();
        }
        return aimManager;
    }

    // Multiple AIM?
    // createAllAimSession
    // findAimSession - AimSessionInfo 로드 밸런싱 (HashMap 에 있고, HB Timeout 되지 않은)

    public AimSessionInfo createAimSession(int aimId) {
        if (aimId < 0) return null;

        // AIM session 중복 체크
        if (aimSessionInfos.containsKey(aimId)) {
            log.warn("AimSessionInfo [{}] Exist", aimId);
            return aimSessionInfos.get(aimId);
        }

        AimSessionInfo sessionInfo = new AimSessionInfo(aimId);
        sessionInfo.updateLastHbTime();

        synchronized (aimSessionInfos) {
            aimSessionInfos.put(aimId, sessionInfo);
        }

        log.warn("New AimSessionInfo [{}] Created. Total [{}]", aimId, aimSessionInfos.size());
        return sessionInfo;
    }

    public AimSessionInfo getAimSession(int aimId) {
        if (aimId < 0) return null;
        AimSessionInfo sessionInfo = null;
        synchronized (aimSessionInfos) {
            if (aimSessionInfos.containsKey(aimId)) {
                sessionInfo = aimSessionInfos.get(aimId);
            }
        }

        if (sessionInfo == null) {
            log.warn("AimSessionInfo [{}] Null", aimId);
        }
        return sessionInfo;
    }

    public void check() {
        AimSessionInfo sessionInfo = getAimSession(0);
        if (sessionInfo == null) {
            // 생성?
            return;
        }

        // Standby -> Active 절체시 필드값 리셋? (lastHbTime 현재 시간으로)

        // Login Flag?

        if (checkHbTimeout(sessionInfo) && !sessionInfo.isTimeoutFlag()) {
            sessionInfo.setTimeoutFlag(true);
            log.error("[TIMEOUT] AIM Heartbeat Timeout ({})", sessionInfo.isTimeoutFlag());
        }
    }

    private boolean checkHbTimeout(AimSessionInfo sessionInfo) {
        long lastHbTime = sessionInfo.getLastHbTime();
        return lastHbTime > 0 && lastHbTime + hbTimeout < System.currentTimeMillis();
    }
}
