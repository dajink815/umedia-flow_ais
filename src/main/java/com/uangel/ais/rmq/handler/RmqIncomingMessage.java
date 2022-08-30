package com.uangel.ais.rmq.handler;

import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author dajin kim
 */
public abstract class RmqIncomingMessage<T> implements RmqIncomingInterface {
    static final Logger log = LoggerFactory.getLogger(RmqIncomingMessage.class);
    protected final Header header;
    protected T body;

    protected RmqIncomingMessage(Message message) {
        header = message.getHeader();
        try {
            body = (T) message.getAllFields().entrySet().stream()
                    .filter(entry -> !entry.getKey().getName().equals("header"))
                    .map(Map.Entry::getValue).findAny().orElse(null);
        } catch (Exception e) {
            log.error("RmqIncomingMessage.Constructor.Exception ", e);
        }
        if (body == null) {
            log.warn("RmqIncomingMessage - check body type [{}]", header.getType());
        }
    }

    protected int getReasonCode() {
        return header.getReasonCode();
    }

    protected String getReason() {
        return header.getReason();
    }

    protected String getTId() {
        return header.getTId();
    }

}
