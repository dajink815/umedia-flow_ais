package com.uangel.ais.signal;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.signal.consumer.SipRequestConsumer;
import com.uangel.ais.signal.consumer.SipResponseConsumer;
import com.uangel.ais.signal.module.SipSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.ResponseEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SIP Manager
 * Start Request, Response Consumer Thread
 *
 * @file SipManager.java
 * @author dajin kim
 */
public class SipManager {
    static final Logger log = LoggerFactory.getLogger(SipManager.class);
    private static SipManager sipManager = null;
    private final AppInstance instance = AppInstance.getInstance();
    private ExecutorService executorRequestService;
    private ExecutorService executorResponseService;

    private SipManager() {
        // nothing
    }

    public static SipManager getInstance() {
        if (sipManager == null) {
            sipManager = new SipManager();
            sipManager.start();
        }

        return sipManager;
    }

    public void start() {
        AisConfig config = instance.getConfig();
        log.info("SipManager Start [{}:{}]", config.getServerIp(), config.getServerPort());

        // SipSignal
        SipSignal sipSignal = new SipSignal();
        instance.setSipSignal(sipSignal);

        // Request Queue
        BlockingQueue<RequestEvent> reqQue = new ArrayBlockingQueue<>(config.getQueueSize());
        instance.setSipReqQue(reqQue);
        // Response Queue
        BlockingQueue<ResponseEvent> resQue = new ArrayBlockingQueue<>(config.getQueueSize());
        instance.setSipResQue(resQue);

        // Request, Response Consumer Thread (config Thread 사이즈 만큼 생성)
        executorRequestService = Executors.newFixedThreadPool(config.getThreadSize());
        executorResponseService = Executors.newFixedThreadPool(config.getThreadSize());
        for (int i = 0; i < config.getThreadSize(); i++) {
            executorRequestService.execute(() -> new Thread(new SipRequestConsumer(reqQue)).start());
            executorResponseService.execute(() -> new Thread(new SipResponseConsumer(resQue)).start());
        }

        // Active 에서만 실행?
        sipSignal.init();
    }

    public void stop() {
        log.info("SipManager Stop");
        instance.getSipSignal().deleteListeningPoint();
        instance.getSipSignal().stopSipStack();

        if (executorRequestService != null) executorRequestService.shutdown();
        if (executorResponseService != null) executorResponseService.shutdown();
    }
}
