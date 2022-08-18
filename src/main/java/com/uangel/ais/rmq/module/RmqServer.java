/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

package com.uangel.ais.rmq.module;

import com.uangel.ais.rmq.module.transport.RmqReceiver;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.util.DateFormatUtil;
import com.uangel.ais.util.PasswdDecryptor;
import com.uangel.ais.util.StringUtil;
import com.uangel.ais.util.Suppress;
import com.google.protobuf.util.JsonFormat;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * RabbitMQ Server
 *
 * @file RmqServer.java
 * @author Youjin Choi
 */
public class RmqServer {
    static final Logger log = LoggerFactory.getLogger(RmqServer.class);
    private static final Suppress suppr = new Suppress(1000L * 30);

    private RmqReceiver rmqReceiver = null;
    private final BlockingQueue<Message> queue;

    private final String host;
    private final String user;
    private final String pass;
    private final String queueName;
    private final int port;

    public RmqServer(String host, String user, String pass, String queueName, int port) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.queueName = queueName;
        this.port = port;
        this.queue = AppInstance.getInstance().getRmqMsgQueue();
    }

    public boolean start() {
        PasswdDecryptor decryptor = new PasswdDecryptor("skt_acs", "PBEWITHMD5ANDDES");
        String decPass = "";

        try {
            decPass = decryptor.decrypt0(this.pass);
        } catch (Exception e) {
            log.error("RMQ Password is not available ", e);
        }

        RmqReceiver receiver = new RmqReceiver(this.host, this.user, decPass, this.port, this.queueName);
        receiver.setCallback(new MessageCallback());
        log.debug("RmqReceiver [{}] <- [{}:{}]", queueName, host, user);

        boolean result = false;
        if (receiver.connect()) {
            setRmqReceiver(receiver);
            result = receiver.start();
        }

        return result;
    }

    public void stop() {
        if (rmqReceiver != null)
            rmqReceiver.close();
    }

    public void setRmqReceiver(RmqReceiver rmqReceiver) {
        this.rmqReceiver = rmqReceiver;
    }

    private class MessageCallback implements RmqCallback {
        @Override
        public void onReceived(byte[] msg, Date ts) {
            String prettyMsg = null;
            Message rmqMsg = null;
            try {
                rmqMsg = Message.parseFrom(msg);
                prettyMsg = JsonFormat.printer().includingDefaultValueFields().print(rmqMsg);
            } catch (Exception e) {
                log.error("RmqServer.parseMessage", e);
            }

            if (rmqMsg == null) {
                String strMsg = new String(msg, StandardCharsets.UTF_8);
                log.warn("RmqServer.MessageCallback.onReceived - Message is Null \r\n[{}]", strMsg);
                return;
            }

            Header header = rmqMsg.getHeader();
            int reasonCode = header.getReasonCode();
            String msgType = header.getType();
            String msgFrom = header.getMsgFrom();

            // HB
            if (rmqMsg.getBodyCase().getNumber() == Message.MHBREQ_FIELD_NUMBER
                    || rmqMsg.getBodyCase().getNumber() == Message.WHBREQ_FIELD_NUMBER) {
                //if (suppr.touch(msgType + msgFrom)) {
                    log.info("[RMQ MESSAGE] onReceived [{}] [{}] <-- [{}]", msgType, reasonCode, msgFrom);
                    printMsg(prettyMsg, ts);
                //}
            } else {
                log.info("[RMQ MESSAGE] onReceived [{}] [{}] <-- [{}]", msgType, reasonCode, msgFrom);
                printMsg(prettyMsg, ts);
            }

            // Check Body Type
            String bodyCase = rmqMsg.getBodyCase().toString();
            String typeCheck = StringUtil.removeUnderBar(msgType);
            if (!bodyCase.equalsIgnoreCase(typeCheck)) {
                log.warn("MessageCallback.onReceived Check Body type [{}]", bodyCase);
            } else {
                log.debug("MessageCallback.onReceived Body type [{}]", bodyCase);
            }

            // Put Queue
            try {
                queue.put(rmqMsg);
            } catch (InterruptedException e) {
                log.error("MessageCallback.onReceived", e);
                Thread.currentThread().interrupt();
            }
        }

        private void printMsg(String prettyMsg, Date ts) {
            if (ts != null) {
                String time = DateFormatUtil.fastFormatYmdHmsS(ts);
                log.debug("[RMQ MESSAGE] onReceived : {} {}", prettyMsg, time);
            } else {
                log.debug("[RMQ MESSAGE] onReceived : {}", prettyMsg);
            }
        }
    }

}
