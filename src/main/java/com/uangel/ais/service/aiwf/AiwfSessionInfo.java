package com.uangel.ais.service.aiwf;

/**
 * @author dajin kim
 */
public class AiwfSessionInfo {

    private long lastHbTime;
    private boolean timeoutFlag;
    private boolean status;

    public AiwfSessionInfo() {
        // nothing
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

    public boolean isTimeoutFlag() {
        return timeoutFlag;
    }
    public void setTimeoutFlag(boolean timeoutFlag) {
        this.timeoutFlag = timeoutFlag;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
