package com.uangel.ais.rmq.handler.incoming;


import com.uangel.rmq.message.RmqHeader;
import com.uangel.rmq.message.RmqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqAimConsumer {
    static final Logger log = LoggerFactory.getLogger(RmqAimConsumer.class);

    public void aimMessageProcessing(RmqMessage msg) {
        switch(msg.getBodyCase().getNumber()){

            case RmqMessage.MEDIASTARTREQ_FIELD_NUMBER:

                break;
            case RmqMessage.MEDIAPLAYRES_FIELD_NUMBER:

                break;
            case RmqMessage.MEDIADONEREQ_FIELD_NUMBER:

                break;
            case RmqMessage.MEDIASTOPRES_FIELD_NUMBER:

                break;
            default:
                RmqHeader header = msg.getHeader();
                log.warn("Not Defined Type [{}:{}]", header.getTId(), header.getType());
                break;
        }
    }
}
