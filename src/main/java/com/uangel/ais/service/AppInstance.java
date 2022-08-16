package com.uangel.ais.service;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.signal.module.SipSignal;
import com.uangel.protobuf.Message;
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
    private BlockingQueue<Message> rmqMsgQueue;
    // Rmq connect, blocking
    private boolean localRmqConnect;
    private boolean aiwfRmqConnect;
    private boolean localRmqBlocked;
    private boolean aiwfRmqBlocked;

    private AppInstance() {
        // nothing
    }

    public static AppInstance getInstance() {
        if (instance == null) {
            instance = new AppInstance();
        }
        return instance;
    }

    // SIP
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

    // Config
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

    // RMQ
    public BlockingQueue<Message> getRmqMsgQueue() {
        return rmqMsgQueue;
    }
    public void setRmqMsgQueue(BlockingQueue<Message> rmqMsgQueue) {
        this.rmqMsgQueue = rmqMsgQueue;
    }

    public boolean isLocalRmqConnect() {
        return localRmqConnect;
    }
    public void setLocalRmqConnect(boolean localRmqConnect) {
        this.localRmqConnect = localRmqConnect;
    }

    public boolean isAiwfRmqConnect() {
        return aiwfRmqConnect;
    }
    public void setAiwfRmqConnect(boolean aiwfRmqConnect) {
        this.aiwfRmqConnect = aiwfRmqConnect;
    }

    public boolean isLocalRmqBlocked() {
        return localRmqBlocked;
    }
    public void setLocalRmqBlocked(boolean localRmqBlocked) {
        this.localRmqBlocked = localRmqBlocked;
    }

    public boolean isAiwfRmqBlocked() {
        return aiwfRmqBlocked;
    }
    public void setAiwfRmqBlocked(boolean aiwfRmqBlocked) {
        this.aiwfRmqBlocked = aiwfRmqBlocked;
    }
}
