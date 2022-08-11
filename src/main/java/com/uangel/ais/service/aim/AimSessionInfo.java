package com.uangel.ais.service.aim;

/**
 * @author dajin kim
 */
public class AimSessionInfo {

    private final int aimId;
    private long lastHbTime;
    private boolean loginFlag;
    private boolean timeoutFlag;

    public AimSessionInfo(int aimId) {
        this.aimId = aimId;
    }

    public int getAimId() {
        return aimId;
    }

    public long getLastHbTime() {
        return lastHbTime;
    }
    public void setLastHbTime(long lastHbTime) {
        this.lastHbTime = lastHbTime;
    }
    public void updateLastHbTime() {
        this.lastHbTime = System.currentTimeMillis();
    }

    public boolean isLoginFlag() {
        return loginFlag;
    }
    public void setLoginFlag(boolean loginFlag) {
        this.loginFlag = loginFlag;
    }

    public boolean isTimeoutFlag() {
        return timeoutFlag;
    }
    public void setTimeoutFlag(boolean timeoutFlag) {
        this.timeoutFlag = timeoutFlag;
    }
}
