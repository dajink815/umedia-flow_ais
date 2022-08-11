package com.uangel.ais.service;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.signal.module.SipSignal;
import com.uangel.rmq.message.RmqMessage;
import stack.java.uangel.sip.RequestEvent;
import stack.java.uangel.sip.ResponseEvent;

import java.util.concurrent.BlockingQueue;

/**
 * @author dajin kim
 */
public class AppInstance {
    private static AppInstance instance = null;

    // Sip Event Queue
    private BlockingQueue<RequestEvent> sipReqQue;
    private BlockingQueue<ResponseEvent> sipResQue;
    private SipSignal sipSignal = null;

    private String configPath = null;
    private AisConfig aisConfig = null;

    // RMQ
    private BlockingQueue<RmqMessage> rmqMsgQueue;

    private AppInstance() {
        // nothing
    }

    public static AppInstance getInstance() {
        if (instance == null) {
            instance = new AppInstance();
        }
        return instance;
    }

    public BlockingQueue<RequestEvent> getSipReqQue() {
        return sipReqQue;
    }
    public void setSipReqQue(BlockingQueue<RequestEvent> sipReqQue) {
        this.sipReqQue = sipReqQue;
    }

    public BlockingQueue<ResponseEvent> getSipResQue() {
        return sipResQue;
    }
    public void setSipResQue(BlockingQueue<ResponseEvent> sipResQue) {
        this.sipResQue = sipResQue;
    }

    public SipSignal getSipSignal() {
        return sipSignal;
    }
    public void setSipSignal(SipSignal sipSignal) {
        this.sipSignal = sipSignal;
    }

    public String getConfigPath() {
        return configPath;
    }
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public AisConfig getConfig() {
        return aisConfig;
    }
    public void setConfig(AisConfig config) {
        this.aisConfig = config;
    }

    public BlockingQueue<RmqMessage> getRmqMsgQueue() {
        return rmqMsgQueue;
    }
    public void setRmqMsgQueue(BlockingQueue<RmqMessage> rmqMsgQueue) {
        this.rmqMsgQueue = rmqMsgQueue;
    }
}
