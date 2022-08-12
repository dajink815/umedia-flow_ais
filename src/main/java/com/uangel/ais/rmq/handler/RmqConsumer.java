package com.uangel.ais.rmq.handler;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.rmq.handler.aim.RmqAimConsumer;
import com.uangel.ais.rmq.handler.aiwf.RmqAiwfConsumer;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.util.SleepUtil;
import com.uangel.rmq.message.RmqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author dajin kim
 */
public class RmqConsumer implements Runnable {
    static final Logger log = LoggerFactory.getLogger(RmqConsumer.class);
    private final AisConfig config = AppInstance.getInstance().getConfig();
    private final BlockingQueue<RmqMessage> rmqMsgQueue;
    private boolean isQuit = false;

    public RmqConsumer(BlockingQueue<RmqMessage> queue) {
        this.rmqMsgQueue = queue;
    }

    @Override
    public void run() {
        queueProcessing();
    }

    private void queueProcessing() {
        while (!isQuit) {
            try {
                RmqMessage rmqMsg = rmqMsgQueue.poll(10, TimeUnit.MILLISECONDS);

                if (rmqMsg == null) {
                    SleepUtil.trySleep(10);
                    continue;
                }
                messageProcessing(rmqMsg);

            } catch (InterruptedException e) {
                log.error("RmqConsumer.queueProcessing", e);
                isQuit = true;
                Thread.currentThread().interrupt();
            }
        }
    }

    private void messageProcessing(RmqMessage msg) {
        String msgFrom = msg.getHeader().getMsgFrom();
        if (config.getAiwf().contains(msgFrom)) {
            RmqAiwfConsumer consumer = new RmqAiwfConsumer();
            consumer.aiwfMessageProcessing(msg);
        } else if (config.getAim().contains(msgFrom)) {
            RmqAimConsumer consumer = new RmqAimConsumer();
            consumer.aimMessageProcessing(msg);
        } else {
            log.warn("RmqConsumer.messageProcessing - Check MsgFrom [{}:{} ({})]", msg.getHeader().getType(), msgFrom, msg.getHeader().getTId());
        }
    }
}

