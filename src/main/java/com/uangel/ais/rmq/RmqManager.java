package com.uangel.ais.rmq;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.rmq.handler.RmqConsumer;
import com.uangel.ais.util.StringUtil;
import com.uangel.protobuf.Message;
import com.uangel.ais.rmq.module.RmqClient;
import com.uangel.ais.rmq.module.RmqServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author dajin kim
 */
public class RmqManager {
    static final Logger log = LoggerFactory.getLogger(RmqManager.class);
    private static RmqManager rmqManager = null;
    private final AppInstance instance = AppInstance.getInstance();
    private final AisConfig config = instance.getConfig();
    private ExecutorService executorRmqService;

    private RmqServer rmqServer;
    private final ConcurrentHashMap<String, RmqClient> rmqClientMap = new ConcurrentHashMap<>();

    private RmqManager() {
        // nothing
    }

    public static RmqManager getInstance() {
        if (rmqManager == null) {
            rmqManager = new RmqManager();
        }
        return rmqManager;
    }

    public void start() {
        startRmqConsumer();
        startRmqClient();
        startRmqServer();
    }

    public void stop() {
        if (rmqServer != null) {
            rmqServer.stop();
        }
        if (!rmqClientMap.isEmpty()) {
            rmqClientMap.forEach((key, client) -> client.closeSender());
        }
        if (executorRmqService != null) {
            executorRmqService.shutdown();
        }
    }

    private void startRmqConsumer() {
        if (executorRmqService != null) return;

        executorRmqService = Executors.newFixedThreadPool(config.getRmqThreadSize());
        BlockingQueue<Message> rmqMsgQueue = new ArrayBlockingQueue<>(config.getRmqQueueSize());
        instance.setRmqMsgQueue(rmqMsgQueue);

        for (int i = 0; i < config.getRmqThreadSize(); i++) {
            executorRmqService.execute(new RmqConsumer(rmqMsgQueue));
        }
    }

    // Server
    private void startRmqServer() {
        // AIS Server
        if (rmqServer == null) {
            String target = config.getAis();
            String host = config.getHost();
            String user = config.getUser();
            String pass = config.getPass();
            int port = config.getPort();

            RmqServer rmqAisServer = new RmqServer(host, user, pass, target, port);
            boolean localResult = rmqAisServer.start();
            if (localResult) rmqServer = rmqAisServer;
            instance.setLocalRmqConnect(localResult);
            log.info("RabbitMQ Server Start {}. [{}], [{}], [{}]", StringUtil.getSucFail(localResult), target, host, user);
        }
    }

    // Client
    private void startRmqClient() {
        // AIWF
        boolean aiwfResult = addClient(config.getAiwf(), config.getAiwfHost(), config.getAiwfUser(), config.getAiwfPass(), config.getAiwfPort());
        instance.setAiwfRmqConnect(aiwfResult);
        // AIM
        boolean localResult = addClient(config.getAim(), config.getHost(), config.getUser(), config.getPass(), config.getPort());
        instance.setLocalRmqConnect(localResult);

    }
    private boolean addClient(String target, String host, String user, String pass, int port) {
        boolean result = false;
        if (rmqClientMap.get(target) == null) {
            RmqClient client = new RmqClient(host, user, pass, target, port);
            result = client.start();
            if (result) rmqClientMap.put(target, client);
            log.info("RabbitMQ Client Start {}. [{}], [{}], [{}]", StringUtil.getSucFail(result), target, host, user);
        }
        return result;
    }

    public RmqClient getRmqClient(String queueName) {
        return rmqClientMap.get(queueName);
    }

    public RmqServer getRmqServer() {
        return rmqServer;
    }

}
