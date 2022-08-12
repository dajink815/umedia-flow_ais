package com.uangel.ais.rmq.handler.aiwf;


import com.uangel.ais.rmq.handler.aiwf.incoming.*;
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
            case RmqMessage.WHBREQ_FIELD_NUMBER:
                RmqWHbReq wHbReq = new RmqWHbReq();
                wHbReq.handle(msg);
                break;
            case RmqMessage.CALLINCOMINGRES_FIELD_NUMBER:
                RmqCallIncomingRes callIncomingRes = new RmqCallIncomingRes();
                callIncomingRes.handle(msg);
                break;
            case RmqMessage.CALLSTARTRES_FIELD_NUMBER:
                RmqCallStartRes callStartRes = new RmqCallStartRes();
                callStartRes.handle(msg);
                break;
            case RmqMessage.CALLCLOSEREQ_FIELD_NUMBER:
                RmqCallCloseReq callCloseReq = new RmqCallCloseReq();
                callCloseReq.handle(msg);
                break;
            case RmqMessage.CALLSTOPRES_FIELD_NUMBER:
                RmqCallStopRes callStopRes = new RmqCallStopRes();
                callStopRes.handle(msg);
                break;
            default:
                RmqHeader header = msg.getHeader();
                log.warn("Not Defined Type [{}:{}]", header.getTId(), header.getType());
                break;
        }
    }
}
