/*
 * Copyright (C) 2019. Uangel Corp. All rights reserved.
 *
 */

package com.uangel.ais.rmq.module;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;
import com.uangel.ais.service.AppInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * RMQ Recovery
 *
 * @file RmqRecoveryListener.java
 * @author youjin Choi
 */
public class RmqRecoveryListener implements RecoveryListener {
    static final Logger log = LoggerFactory.getLogger(RmqRecoveryListener.class);
    private final String queue;
    private final boolean isLocal;

    public RmqRecoveryListener(String queue, boolean isLocal) {
        this.queue = queue;
        this.isLocal = isLocal;
    }

    @Override
    public void handleRecovery(Recoverable recoverable) {
        if (recoverable instanceof Channel) {
            int channelNumber = ((Channel) recoverable).getChannelNumber();
            log.error("Rmq {} Connection to channel # {} was recovered.", queue, channelNumber);

            AppInstance instance = AppInstance.getInstance();
            if (isLocal) {
                instance.setLocalRmqConnect(true);
            } else {
                instance.setAiwfRmqConnect(true);
            }

        }
    }

    @Override
    public void handleRecoveryStarted(Recoverable recoverable) {
        log.error("Rmq {} handleRecoveryStarted", queue);
    }
}
