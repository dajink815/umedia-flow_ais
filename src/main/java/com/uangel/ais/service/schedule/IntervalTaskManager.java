package com.uangel.ais.service.schedule;

import com.uangel.ais.service.schedule.base.IntervalTaskUnit;
import com.uangel.ais.service.schedule.handler.SessionMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Session Manager
 * 주기적으로 세션을 필터링하고 처리 (Default 1초)
 * @author Kangmoo Heo
 */
public class IntervalTaskManager {
    private static final Logger log = LoggerFactory.getLogger(IntervalTaskManager.class);
    private static final IntervalTaskManager instance = new IntervalTaskManager();
    private final Map<String, IntervalTaskUnit> jobs = new HashMap<>();
    private static final int TASK_INTERVAL = 1000;
    private ScheduledExecutorService executorService;
    private boolean isStarted = false;

    private IntervalTaskManager() {
        // Nothing
    }

    public void init(){
        addJob(SessionMonitor.class.getSimpleName(), new SessionMonitor(TASK_INTERVAL));
    }

    public static IntervalTaskManager getInstance() {
        return instance;
    }

    public void start() {
        if (isStarted) {
            log.info("() () () Already Started IntervalTaskManager");
            return;
        }
        isStarted = true;
        executorService = Executors.newScheduledThreadPool(jobs.size());
        for (IntervalTaskUnit runner : jobs.values()) {
            executorService.scheduleAtFixedRate(() -> {
                        Thread.currentThread().setName("IntervalTask_" + runner.getClass().getSimpleName());
                        runner.run();
                    },
                    runner.getInterval() - System.currentTimeMillis() % runner.getInterval(),
                    runner.getInterval(),
                    TimeUnit.MILLISECONDS);
        }
        log.info("() () () IntervalTaskManager Start");
    }

    public void stop() {
        if (!isStarted) {
            log.info("() () () Already Stopped IntervalTaskManager");
            return;
        }
        isStarted = false;
        executorService.shutdown();
        log.info("() () () IntervalTaskManager Stop");
    }

    public void addJob(String name, IntervalTaskUnit runner){
        if(jobs.get(name) != null){
            log.warn("() () () Hashmap Key duplication");
            return;
        }
        log.debug("() () () Add Runner [{}]",name);
        jobs.put(name, runner);
    }
}
