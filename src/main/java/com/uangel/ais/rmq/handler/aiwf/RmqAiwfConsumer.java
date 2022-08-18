package com.uangel.ais.rmq.handler.aiwf;


import com.uangel.ais.rmq.handler.aiwf.incoming.*;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqAiwfConsumer {
    static final Logger log = LoggerFactory.getLogger(RmqAiwfConsumer.class);

    public void aiwfMessageProcessing(Message msg) {

        switch(msg.getBodyCase().getNumber()){
            case Message.WHBREQ_FIELD_NUMBER:
                RmqWHbReq wHbReq = new RmqWHbReq();
                wHbReq.handle(msg);
                break;
            case Message.CALLINCOMINGRES_FIELD_NUMBER:
                RmqCallIncomingRes callIncomingRes = new RmqCallIncomingRes();
                callIncomingRes.handle(msg);
                break;
            case Message.CALLSTARTRES_FIELD_NUMBER:
                RmqCallStartRes callStartRes = new RmqCallStartRes();
                callStartRes.handle(msg);
                break;
            case Message.CALLCLOSEREQ_FIELD_NUMBER:
                RmqCallCloseReq callCloseReq = new RmqCallCloseReq();
                callCloseReq.handle(msg);
                break;
            case Message.CALLSTOPRES_FIELD_NUMBER:
                RmqCallStopRes callStopRes = new RmqCallStopRes();
                callStopRes.handle(msg);
                break;
            default:
                Header header = msg.getHeader();
                log.warn("Not Defined Type [{}:{}]", header.getTId(), header.getType());
                break;
        }
    }
}
