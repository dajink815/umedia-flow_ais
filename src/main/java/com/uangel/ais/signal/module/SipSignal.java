/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

package com.uangel.ais.signal.module;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.service.ServiceDefine;
import com.uangel.ais.util.SleepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.*;
import stack.java.uangel.sip.address.AddressFactory;
import stack.java.uangel.sip.header.HeaderFactory;
import stack.java.uangel.sip.message.MessageFactory;

import java.util.Properties;

/**
 * SIP Signal
 *
 * @file SipSignal.java
 * @author Tony Lim
 */
public class SipSignal implements SipListener {
    public static final String LIB_JAVA = "lib.java";
    static final Logger log = LoggerFactory.getLogger(SipSignal.class);
    private final AppInstance instance = AppInstance.getInstance();
    private AddressFactory addressFactory = null;
    private MessageFactory messageFactory;
    private HeaderFactory headerFactory;
    private SipStack sipStack;
    private SipSignal listener;
    private ListeningPoint lp = null;
    private SipProvider sipProvider;

    public SipSignal() {
        // Do Nothing
    }

    public void init() {
        SipFactory sipFactory = SipFactory.getInstance();
        sipFactory.setPathName(LIB_JAVA);

        sipStack = getSipStack(sipFactory);

        createFactory(sipFactory, sipStack);
        log.info("SipSignal Init Finished");
    }

    private SipStack getSipStack(SipFactory sipFactory) {
        try {
            sipStack = sipFactory.createSipStack(initProperties());
        } catch (PeerUnavailableException e) {
            log.error("SipSignal.getSipStack", e);
            throw new RuntimeException();
        }
        return sipStack;
    }

    private void createFactory(SipFactory sipFactory, SipStack sipStack) {
        AisConfig config = AppInstance.getInstance().getConfig();
        try {
            this.headerFactory = sipFactory.createHeaderFactory();
            this.addressFactory = sipFactory.createAddressFactory();
            this.messageFactory = sipFactory.createMessageFactory();
            this.lp = sipStack.createListeningPoint(config.getServerIp(), config.getServerPort(), ServiceDefine.TRANSPORT.getStr());
            this.listener = this;
            this.sipProvider = sipStack.createSipProvider(lp);
            this.sipProvider.addSipListener(listener);
            sipStack.start();

        } catch (Exception ex) {
            log.error("SipSignal.createFactory", ex);
            SleepUtil.trySleep(1000);
        }
    }

    private Properties initProperties() {
        AisConfig config = AppInstance.getInstance().getConfig();

        Properties properties = new Properties();
        properties.setProperty("STACK_NAME", config.getStackName());
        // Set Active Virtual IP
        properties.setProperty("IP_ADDRESS", config.getServerIp());
        properties.setProperty("AUTOMATIC_DIALOG_SUPPORT", config.getAutomaticDialogSupport());
        properties.setProperty("LOG_MESSAGE_CONTENT", config.getLogMessageContent());
        properties.setProperty("CACHE_SERVER_CONNECTIONS", config.getCacheServerConnections());
        properties.setProperty("CACHE_CLIENT_CONNECTIONS", config.getCacheClientConnections());
        properties.setProperty("RECEIVE_UDP_BUFFER_SIZE", config.getReceiveUdpBufferSize().toString());
        properties.setProperty("SEND_UDP_BUFFER_SIZE", config.getSendUdpBufferSize().toString());
        properties.setProperty("REENTRANT_LISTENER", config.getReentrantListener());
        properties.setProperty("DEBUG_LOG", config.getDebugLog());
        properties.setProperty("SERVER_LOG", config.getServerLog());
        properties.setProperty("THREAD_POOL_SIZE", config.getThreadPoolSize().toString());
        properties.setProperty("MAX_MESSAGE_SIZE", config.getMaxMessageSize().toString());
        properties.setProperty("MAX_SERVER_TRANSACTIONS", "655350");
        properties.setProperty("DELIVER_UNSOLICITED_NOTIFY", "true");
        properties.setProperty("AUTOMATIC_DIALOG_ERROR_HANDLING", "false");
        properties.setProperty("DELIVER_TERMINATED_EVENT_FOR_ACK", config.getDeliverTerminateEventForAck());
        properties.setProperty("AGGRESSIVE_CLEANUP", config.getAggressiveCleanup());
        properties.setProperty("TRACE_LEVEL", "LOG4J");

        return properties;
    }

    public void deleteListeningPoint() {
        try {
            if (this.lp != null)
                sipStack.deleteListeningPoint(this.lp);
            this.lp = null;
        } catch (ObjectInUseException e) {
            log.error("SipSignal.deleteListeningPoint", e);
        }
    }

    public void stopSipStack() {
        if (this.listener != null)
            this.sipProvider.removeSipListener(this.listener);
        this.sipStack.stop();
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {

        dialogTerminatedEvent.getDialog().delete();
    }

    @Override
    public void processIOException(IOExceptionEvent arg0) {
        log.error("IOException");
    }

    @Override
    public void processRequest(RequestEvent requestEvent) {
        try {
            instance.getSipReqQue().put(requestEvent);
        } catch (InterruptedException e) {
            log.error("SipSignal.processRequest", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        try {
            instance.getSipResQue().put(responseEvent);
        } catch (InterruptedException e) {
            log.error("SipSignal.processResponse", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        Transaction transaction;
        if (timeoutEvent.isServerTransaction()) {
            transaction = timeoutEvent.getServerTransaction();
        } else {
            transaction = timeoutEvent.getClientTransaction();

        }

        if (transaction.getDialog() != null)
            log.warn("() () () dialogState = {} ", transaction.getDialog().getState());
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        if (transactionTerminatedEvent.isServerTransaction()) {
            try {
                transactionTerminatedEvent.getServerTransaction().terminate();
            } catch (ObjectInUseException e) {
                log.error("SipSignal.processTransactionTerminated", e);
            }
        } else {
            try {
                transactionTerminatedEvent.getClientTransaction().terminate();
            } catch (ObjectInUseException e) {
                log.error("SipSignal.processTransactionTerminated", e);
            }
        }

    }

    public SipProvider getSipProvider() {
        return this.sipProvider;
    }

    public AddressFactory getAddressFactory() {
        return addressFactory;
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public HeaderFactory getHeaderFactory() {
        return headerFactory;
    }
}
