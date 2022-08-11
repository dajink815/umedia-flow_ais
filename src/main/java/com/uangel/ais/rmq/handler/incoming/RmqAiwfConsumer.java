package com.uangel.ais.rmq.handler.incoming;


import com.uangel.rmq.message.RmqHeader;
import com.uangel.rmq.message.RmqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqAiwfConsumer {
    static final Logger log = LoggerFactory.getLogger(RmqAiwfConsumer.class);

    public void aiwfMessageProcessing(RmqMessage msg) {
        log.debug("RmqAiwfConsumer [{}]", msg.getHeader().getType());

        switch(msg.getBodyCase().getNumber()){
            case RmqMessage.IHBRES_FIELD_NUMBER:

                break;
            case RmqMessage.CREATESESSIONREQ_FIELD_NUMBER:
                log.debug("RmqAiwfConsumer CreateSessionReq");
                break;
            case RmqMessage.DELSESSIONREQ_FIELD_NUMBER:

                break;
            case RmqMessage.TTSSTARTREQ_FIELD_NUMBER:

                break;
            case RmqMessage.TTSRESULTRES_FIELD_NUMBER:

                break;
            case RmqMessage.STTSTARTREQ_FIELD_NUMBER:

                break;
            case RmqMessage.STTRESULTRES_FIELD_NUMBER:

                break;
            default:
                RmqHeader header = msg.getHeader();
                log.warn("Not Defined Type [{}:{}]", header.getTId(), header.getType());
                break;
        }
    }
}
