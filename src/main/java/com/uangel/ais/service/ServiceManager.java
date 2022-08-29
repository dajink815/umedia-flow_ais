package com.uangel.ais.service;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.service.aim.AimManager;
import com.uangel.ais.service.aiwf.AiwfManager;
import com.uangel.ais.service.schedule.IntervalTaskManager;
import com.uangel.ais.signal.SipManager;
import com.uangel.ais.util.SleepUtil;
import com.uangel.ais.rmq.RmqManager;
import com.uangel.protobuf.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class ServiceManager {
    static final Logger log = LoggerFactory.getLogger(ServiceManager.class);
    private static ServiceManager serviceManager = null;
    private static final AppInstance instance = AppInstance.getInstance();
    private boolean isQuit = false;
    private SipManager sipManager = null;
    private RmqManager rmqManager = null;
    private IntervalTaskManager intervalTaskManager = null;

    private ServiceManager() {
        instance.setConfig(new AisConfig(instance.getConfigPath()));
    }

    public static ServiceManager getInstance() {
        if (serviceManager == null) {
            serviceManager = new ServiceManager();
        }

        return serviceManager;
    }

    public void loop() {
        this.startService();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Process is about to quit (Ctrl+C)");
            this.isQuit = true;
            this.stopService();
        }));

        while (!isQuit) {
            try {
                SleepUtil.trySleep(1000);
            } catch (Exception e) {
                log.error("ServiceManager.loop.Exception ", e);
            }
        }

        log.info("Process End");
    }

    private void startService() {
        log.info("Start Service...");

        // Default msgFrom
        MessageBuilder.setDefaultMsgFrom(instance.getConfig().getAis());

        // SIP , RMQ
        sipManager = SipManager.getInstance();

        rmqManager = RmqManager.getInstance();
        rmqManager.start();

        // AIM , AIWF
        AimManager.getInstance().createAimSession(0);
        AiwfManager.getInstance().createAwifSession(0);

        this.intervalTaskManager = IntervalTaskManager.getInstance();
        try {
            this.intervalTaskManager.init();
            this.intervalTaskManager.start();
        } catch (Exception e) {
            log.error("IntervalTaskManager.start.Exception", e);
        }
    }

    private void stopService() {
        log.info("Stop Service...");

        if (sipManager != null)
            sipManager.stop();

        if (rmqManager != null)
            rmqManager.stop();

        if (intervalTaskManager != null)
            intervalTaskManager.stop();
    }


}
