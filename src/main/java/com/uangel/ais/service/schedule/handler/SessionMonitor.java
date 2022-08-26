package com.uangel.ais.service.schedule.handler;

import com.uangel.ais.service.schedule.base.IntervalTaskUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class SessionMonitor extends IntervalTaskUnit {
    private static final Logger log = LoggerFactory.getLogger(SessionMonitor.class);

    public SessionMonitor(int interval) {
        super(interval);
    }

    @Override
    public void run() {
        printSessionCount();
    }

    private void printSessionCount() {
        log.debug("CallSession count : {}", callManager.getCallInfoSize());
/*        log.debug("Rmq Status LOCAL [{} (Block:{})], AIWF [{} (Block:{})]",
                appInstance.isLocalRmqConnect(), appInstance.isLocalRmqBlocked(),
                appInstance.isAiwfRmqConnect(), appInstance.isAiwfRmqBlocked());*/
    }
}
