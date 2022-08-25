package com.uangel.ais.rmq.handler;

import com.uangel.ais.config.AisConfig;
import com.uangel.ais.util.StringUtil;
import com.google.protobuf.util.JsonFormat;
import com.uangel.ais.rmq.RmqManager;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import com.uangel.ais.rmq.module.RmqClient;
import com.uangel.ais.service.AppInstance;
import com.uangel.ais.util.Suppress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqOutgoingMessage {
    static final Logger log = LoggerFactory.getLogger(RmqOutgoingMessage.class);
    private static final Suppress suppr = new Suppress(1000L * 30);
    private final String target;

    public RmqOutgoingMessage(String target) {
        this.target = target;
    }

    public boolean sendTo(Message rmqMessage) {
        return sendTo(this.target, rmqMessage);
    }

    public boolean sendTo(String target, Message rmqMessage) {
        boolean result = false;

        AisConfig config = AppInstance.getInstance().getConfig();
        if (StringUtil.isNull(target)) {
            target = config.getAiwf();
        }

        try {
            String json = JsonFormat.printer().includingDefaultValueFields().print(rmqMessage);

            Header header = rmqMessage.getHeader();
            String msgType = header.getType();
            int bodyNum = rmqMessage.getBodyCase().getNumber();

            if (bodyNum == Message.MHBRES_FIELD_NUMBER || bodyNum == Message.WHBRES_FIELD_NUMBER
                    // remove
                    || bodyNum == Message.MLOGINRES_FIELD_NUMBER) {
                if (suppr.touch(msgType + header.getMsgFrom())) {
                    printMsg(rmqMessage, json);
                }
            } else {
                printMsg(rmqMessage, json);
            }

            RmqClient client = RmqManager.getInstance().getRmqClient(target);
            if (client != null) {
                result = client.send(rmqMessage.toByteArray());
                // RMQ 메시지 전송 실패
                if (!result) {
                    log.warn("() () () send failed [{}] -> [{}]", msgType, target);
                }
            } else {
                log.warn("() () () RmqClient is Null [{}]", target);
            }
        } catch (Exception e) {
            log.error("RmqOutgoingMessage.sendTo.Exception ", e);
        }

        return result;
    }

    private void printMsg(Message rmqMessage, String json) {
        Header header = rmqMessage.getHeader();
        String msgType = header.getType();

        log.info("[RMQ MESSAGE] send [{}] [{}] --> [{}]", msgType, header.getReasonCode(), target);
        log.debug("[RMQ MESSAGE] Json --> {}", json);

        // Check Body Type
        String bodyCase = rmqMessage.getBodyCase().toString();
        String typeCheck = StringUtil.removeUnderBar(msgType);
        if (!bodyCase.equalsIgnoreCase(typeCheck)) {
            log.warn("RmqOutgoingMessage.sendTo Check Body type [{}]", bodyCase);
        } else {
            log.debug("RmqOutgoingMessage.sendTo Body type [{}]", bodyCase);
        }
    }
}
