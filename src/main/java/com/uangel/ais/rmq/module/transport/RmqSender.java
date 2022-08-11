package com.uangel.ais.rmq.module.transport;

/**
 * @author dajin kim
 */

/**
 * Rabbit MQ Sender
 *
 * @file RmqSender.java
 * @author Tony Lim
 */
public class RmqSender extends RmqTransport {
    /**
     * RabbitMQ Message Sender
     *
     * @param host      RabbitMQ host
     * @param userName  RabbitMQ user
     * @param password  RabbitMQ password(암호화)
     * @param port      RabbitMQ post
     * @param queueName RabbitMQ queueName
     * @see RmqTransport
     */
    public RmqSender(String host, String userName, String password, int port, String queueName) {
        super(host, userName, password, port, queueName);
    }

    public boolean send(byte[] msg) {
        if (!isOpened()) {
            log.error("RMQ channel is NOT opened");
            return false;
        }

        boolean result = false;

        try {
            getChannel().basicPublish("", getQueueName(), null, msg);
            result = true;
        } catch (Exception e) {
            log.error("RmqSender.send", e);
        }

        return result;
    }

    public boolean isOpened() {
        return getChannel().isOpen();
    }
}
