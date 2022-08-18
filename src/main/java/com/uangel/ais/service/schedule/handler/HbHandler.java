package com.uangel.ais.service.schedule.handler;

import com.uangel.ais.service.aim.AimManager;
import com.uangel.ais.service.aiwf.AiwfManager;
import com.uangel.ais.service.schedule.base.IntervalTaskUnit;

/**
 * @author dajin kim
 */
public class HbHandler extends IntervalTaskUnit {
    private final AimManager aimManager = AimManager.getInstance();
    private final AiwfManager aiwfManager = AiwfManager.getInstance();

    public HbHandler(int interval) {
        super(interval);
    }

    @Override
    public void run() {
        aimManager.check();
        aiwfManager.check();
    }
}
