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
                RmqWHbReq wHbReq = new RmqWHbReq(msg);
                wHbReq.handle();
                break;
            case Message.CALLINCOMINGRES_FIELD_NUMBER:
                RmqCallIncomingRes callIncomingRes = new RmqCallIncomingRes(msg);
                callIncomingRes.handle();
                break;
            case Message.CALLSTARTRES_FIELD_NUMBER:
                RmqCallStartRes callStartRes = new RmqCallStartRes(msg);
                callStartRes.handle();
                break;
            case Message.CALLCLOSEREQ_FIELD_NUMBER:
                RmqCallCloseReq callCloseReq = new RmqCallCloseReq(msg);
                callCloseReq.handle();
                break;
            case Message.CALLSTOPRES_FIELD_NUMBER:
                RmqCallStopRes callStopRes = new RmqCallStopRes(msg);
                callStopRes.handle();
                break;
            default:
                Header header = msg.getHeader();
                log.warn("Not Defined Type [{}:{}]", header.getTId(), header.getType());
                break;
        }
    }
}
