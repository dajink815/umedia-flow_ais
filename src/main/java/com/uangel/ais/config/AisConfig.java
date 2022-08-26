package com.uangel.ais.config;

import com.uangel.ais.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class AisConfig extends DefaultConfig {
    static final Logger log = LoggerFactory.getLogger(AisConfig.class);

    // SECTION
    private static final String SECTION_COMMON = "COMMON";
    private static final String SECTION_SIP = "SIP";
    private static final String SECTION_RMQ = "RMQ";

    // FIELD(COMMON)
    private static final String FIELD_LONG_CALL = "LONG_CALL";
    private static final String FIELD_THREAD_SIZE = "THREAD_SIZE";
    private static final String FIELD_QUEUE_SIZE = "QUEUE_SIZE";

    // FIELD(SIP)
    private static final String FIELD_SERVER_IP = "SERVER_IP";
    private static final String FIELD_SERVER_PORT = "SERVER_PORT";
    private static final String FIELD_STACK_NAME = "STACK_NAME";

    // FIELD(RMQ)
    private static final String FIELD_AIS = "AIS";
    private static final String FIELD_AIM = "AIM";
    private static final String FIELD_AIWF = "AIWF";
    private static final String FIELD_AIIF = "AIIF";
    private static final String FIELD_HOST = "HOST";
    private static final String FIELD_USER = "USER";
    private static final String FIELD_PORT = "PORT";
    private static final String FIELD_PASS = "PASS";
    private static final String FIELD_AIWF_HOST = "AIWF_HOST";
    private static final String FIELD_AIWF_USER = "AIWF_USER";
    private static final String FIELD_AIWF_PORT = "AIWF_PORT";
    private static final String FIELD_AIWF_PASS = "AIWF_PASS";
    private static final String FIELD_HB_TIMEOUT = "HB_TIMEOUT";
    private static final String FIELD_RMQ_TIMEOUT = "RMQ_TIMEOUT";


    // VALUE(COMMON)
    private int longCall;
    private int threadSize;
    private int queueSize;

    // VALUE(RMQ)
    private String ais;
    private String aim;
    private String aiwf;
    private String aiif;
    private String host;
    private String user;
    private int port;
    private String pass;
    private String aiwfHost;
    private String aiwfUser;
    private int aiwfPort;
    private String aiwfPass;
    private int rmqThreadSize;
    private int rmqQueueSize;
    private int hbTimeout;
    private int rmqTimeout;

    // VALUE(SIP)
    private String serverIp;
    private int serverPort;
    private String stackName;

    private String automaticDialogSupport;
    private String logMessageContent;
    private String cacheServerConnections;
    private String cacheClientConnections;
    private Integer receiveUdpBufferSize;
    private Integer sendUdpBufferSize;
    private String reentrantListener;
    private String debugLog;
    private String serverLog;
    private Integer threadPoolSize;
    private Integer maxMessageSize;
    private Integer maxServerTransactions;
    private String deliverNotify;
    private String automaticDialogError;
    private String deliverTerminateEventForAck;
    private String aggressiveCleanup;
    private String traceLevel;

    public AisConfig(String configPath) {
        super(configPath);
        boolean result = load(configPath);
        log.info("Load config ... [{}]",  StringUtil.getOkFail(result));
        if (result) {
            loadConfig();
        }
    }

    @Override
    public String getStrValue(String session, String key, String defaultValue) {
        String value = super.getStrValue(session, key, defaultValue);
        log.info("() () () Config Session [{}] key [{}] value [{}]", session, key, value);
        return value;
    }

    @Override
    public int getIntValue(String section, String key, int defaultValue) {
        int value = super.getIntValue(section, key, defaultValue);
        log.info("() () () Config Session [{}] key [{}] value [{}]", section, key, value);
        return value;
    }

    private void loadConfig() {
        loadCommonConfig();
        loadSipConfig();
        loadRmqConfig();
    }

    private void loadCommonConfig() {
        this.longCall = getIntValue(SECTION_COMMON, FIELD_LONG_CALL, 410000);
        this.threadSize = getIntValue(SECTION_COMMON, FIELD_THREAD_SIZE, 5);
        this.queueSize = getIntValue(SECTION_COMMON, FIELD_QUEUE_SIZE, 5);
    }

    private void loadRmqConfig() {
        this.ais = getStrValue(SECTION_RMQ, FIELD_AIS, "");
        this.aim = getStrValue(SECTION_RMQ, FIELD_AIM, "");
        this.aiwf = getStrValue(SECTION_RMQ, FIELD_AIWF, "");
        this.aiif = getStrValue(SECTION_RMQ, FIELD_AIIF, "");
        this.host = getStrValue(SECTION_RMQ, FIELD_HOST, "");
        this.user = getStrValue(SECTION_RMQ, FIELD_USER, "");
        this.port = getIntValue(SECTION_RMQ, FIELD_PORT, 5);
        this.pass = getStrValue(SECTION_RMQ, FIELD_PASS, "");
        this.aiwfHost = getStrValue(SECTION_RMQ, FIELD_AIWF_HOST, "");
        this.aiwfUser = getStrValue(SECTION_RMQ, FIELD_AIWF_USER, "");
        this.aiwfPort = getIntValue(SECTION_RMQ, FIELD_AIWF_PORT, 5);
        this.aiwfPass = getStrValue(SECTION_RMQ, FIELD_AIWF_PASS, "");
        this.rmqThreadSize = getIntValue(SECTION_RMQ, FIELD_THREAD_SIZE, 5);
        this.rmqQueueSize = getIntValue(SECTION_RMQ, FIELD_QUEUE_SIZE, 5);
        this.hbTimeout = getIntValue(SECTION_RMQ, FIELD_HB_TIMEOUT, 5000);
        this.rmqTimeout = getIntValue(SECTION_RMQ, FIELD_RMQ_TIMEOUT, 5000);
    }

    private void loadSipConfig() {
        this.serverIp = getStrValue(SECTION_SIP, FIELD_SERVER_IP, "127.0.0.1");
        this.serverPort = getIntValue(SECTION_SIP, FIELD_SERVER_PORT, 5060);
        this.stackName = getStrValue(SECTION_SIP, FIELD_STACK_NAME, "AICALL");

        this.automaticDialogSupport = getStrValue(SECTION_SIP, "AUTOMATIC_DIALOG_SUPPORT", "off");
        this.logMessageContent = getStrValue(SECTION_SIP, "LOG_MESSAGE_CONTENT", "true");
        this.cacheServerConnections = getStrValue(SECTION_SIP, "CACHE_SERVER_CONNECTIONS", "true");
        this.cacheClientConnections = getStrValue(SECTION_SIP, "CACHE_CLIENT_CONNECTIONS", "true");
        this.receiveUdpBufferSize = getIntValue(SECTION_SIP, "RECEIVE_UDP_BUFFER_SIZE", 65536);
        this.sendUdpBufferSize = getIntValue(SECTION_SIP, "SEND_UDP_BUFFER_SIZE", 65536);
        this.reentrantListener = getStrValue(SECTION_SIP, "REENTRANT_LISTENER", "true");
        this.debugLog = getStrValue(SECTION_SIP, "DEBUG_LOG", "debug.log");
        this.serverLog = getStrValue(SECTION_SIP, "SERVER_LOG", "server.log");
        this.threadPoolSize = getIntValue(SECTION_SIP, "THREAD_POOL_SIZE", 512);
        this.maxMessageSize = getIntValue(SECTION_SIP, "MAX_MESSAGE_SIZE", 65536);
        this.maxServerTransactions = getIntValue(SECTION_SIP, "MAX_SERVER_TRANSACTIONS", 65536);
        this.deliverNotify = getStrValue(SECTION_SIP, "DELIVER_UNSOLICITED_NOTIFY", "true");
        this.automaticDialogError = getStrValue(SECTION_SIP, "AUTOMATIC_DIALOG_ERROR_HANDLING", "false");
        this.deliverTerminateEventForAck = getStrValue(SECTION_SIP, "DELIVER_TERMINATED_EVENT_FOR_ACK", "true");
        this.aggressiveCleanup = getStrValue(SECTION_SIP, "AGGRESSIVE_CLEANUP", "true");
        this.traceLevel = getStrValue(SECTION_SIP, "TRACE_LEVEL", "ais.log");
    }

    // COMMON
    public int getLongCall() {
        return longCall;
    }
    public int getThreadSize() {
        return threadSize;
    }
    public int getQueueSize() {
        return queueSize;
    }

    // RMQ
    public String getAis() {
        return ais;
    }
    public String getAim() {
        return aim;
    }
    public String getAiwf() {
        return aiwf;
    }
    public String getAiif() {
        return aiif;
    }
    public String getHost() {
        return host;
    }
    public String getUser() {
        return user;
    }
    public int getPort() {
        return port;
    }
    public String getPass() {
        return pass;
    }
    public String getAiwfHost() {
        return aiwfHost;
    }
    public String getAiwfUser() {
        return aiwfUser;
    }
    public int getAiwfPort() {
        return aiwfPort;
    }
    public String getAiwfPass() {
        return aiwfPass;
    }
    public int getRmqThreadSize() {
        return rmqThreadSize;
    }
    public int getRmqQueueSize() {
        return rmqQueueSize;
    }
    public int getHbTimeout() {
        return hbTimeout;
    }
    public int getRmqTimeout() {
        return rmqTimeout;
    }

    // SIP
    public String getServerIp() {
        return serverIp;
    }
    public int getServerPort() {
        return serverPort;
    }
    public String getStackName() {
        return stackName;
    }


    public String getAutomaticDialogSupport() {
        return automaticDialogSupport;
    }
    public String getLogMessageContent() {
        return logMessageContent;
    }
    public String getCacheServerConnections() {
        return cacheServerConnections;
    }
    public String getCacheClientConnections() {
        return cacheClientConnections;
    }
    public Integer getReceiveUdpBufferSize() {
        return receiveUdpBufferSize;
    }
    public Integer getSendUdpBufferSize() {
        return sendUdpBufferSize;
    }
    public String getReentrantListener() {
        return reentrantListener;
    }
    public String getDebugLog() {
        return debugLog;
    }
    public String getServerLog() {
        return serverLog;
    }
    public Integer getThreadPoolSize() {
        return threadPoolSize;
    }
    public Integer getMaxMessageSize() {
        return maxMessageSize;
    }
    public Integer getMaxServerTransactions() {
        return maxServerTransactions;
    }
    public String getDeliverNotify() {
        return deliverNotify;
    }
    public String getAutomaticDialogError() {
        return automaticDialogError;
    }
    public String getDeliverTerminateEventForAck() {
        return deliverTerminateEventForAck;
    }
    public String getAggressiveCleanup() {
        return aggressiveCleanup;
    }
    public String getTraceLevel() {
        return traceLevel;
    }
}
