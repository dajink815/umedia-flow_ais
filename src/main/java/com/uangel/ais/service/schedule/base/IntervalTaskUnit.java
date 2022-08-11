package com.uangel.ais.service.schedule.base;


import com.uangel.ais.config.AisConfig;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.session.CallManager;

/**
 * @author kangmoo Heo
 */
public abstract class IntervalTaskUnit implements Runnable {
    protected int interval;
    protected IntervalTaskUnit(int interval) {
        this.interval = interval;
    }

    protected final AppInstance appInstance = AppInstance.getInstance();
    protected final CallManager callManager = CallManager.getInstance();
    protected final AisConfig config = appInstance.getConfig();

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
