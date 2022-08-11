package com.uangel.ais.rmq.module;

import com.uangel.ais.rmq.module.transport.RmqSender;
import com.uangel.ais.util.PasswdDecryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author dajin kim
 */
public class RmqClient {
    static final Logger log = LoggerFactory.getLogger(RmqClient.class);
    private boolean isConnected;
    private RmqSender rmqSender = null;

    private final String host;
    private final String user;
    private final String pass;
    private final String queueName;
    private final int port;

    public RmqClient(String host, String user, String pass, String queueName, int port) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.queueName = queueName;
        this.port = port;
    }

    public boolean start() {
        RmqSender sender = createSender();
        if (sender != null) {
            rmqSender = sender;
            isConnected = true;
        }

        return isConnected;
    }

    private RmqSender createSender() {
        PasswdDecryptor decryptor = new PasswdDecryptor("skt_acs", "PBEWITHMD5ANDDES");
        String decPass = "";
        try {
            decPass = decryptor.decrypt0(this.pass);
        } catch (Exception e) {
            log.error("RMQ Password is not available ", e);
        }
        RmqSender sender = new RmqSender(host, user, decPass, port, queueName);

        log.debug("RmqSender [{}] -> [{}:{}]", queueName, host, user);
        if (!sender.connect()) sender = null;
        return sender;
    }

    public void closeSender() {
        if (rmqSender != null) {
            rmqSender.close();
        }
    }

    public boolean send(String msg) {
        return send(msg.getBytes(UTF_8));
    }

    public boolean send(byte[] msg) {
        RmqSender sender = getRmqSender();
        if (sender == null) {
            sender = createSender();
            if (sender == null) {
                return false;
            }

            setRmqSender(sender);
            setConnected(true);
        }

        if (!sender.isOpened() && !sender.connect())
            return false;

        return sender.send(msg);
    }

    public String getQueueName() {
        return this.queueName;
    }

    public void setConnected(boolean conn) {
        this.isConnected = conn;
    }

    public RmqSender getRmqSender() {
        return rmqSender;
    }

    public void setRmqSender(RmqSender rmqSender) {
        this.rmqSender = rmqSender;
    }
}
