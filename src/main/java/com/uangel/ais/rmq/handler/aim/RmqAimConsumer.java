package com.uangel.ais.rmq.handler.aim;


import com.uangel.ais.rmq.handler.aim.incoming.*;
import com.uangel.protobuf.Header;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqAimConsumer {
    static final Logger log = LoggerFactory.getLogger(RmqAimConsumer.class);

    public void aimMessageProcessing(Message msg) {

        switch(msg.getBodyCase().getNumber()){
            case Message.MLOGINREQ_FIELD_NUMBER:
                RmqMLoginReq mLoginReq = new RmqMLoginReq(msg);
                mLoginReq.handle();
                break;
            case Message.MHBREQ_FIELD_NUMBER:
                RmqMHbReq mHbReq = new RmqMHbReq(msg);
                mHbReq.handle();
                break;
            case Message.OFFERRES_FIELD_NUMBER:
                RmqOfferRes offerRes = new RmqOfferRes(msg);
                offerRes.handle();
                break;
            case Message.NEGORES_FIELD_NUMBER:
                RmqNegoRes negoRes = new RmqNegoRes(msg);
                negoRes.handle();
                break;
            case Message.HANGUPRES_FIELD_NUMBER:
                RmqHangupRes hangupRes = new RmqHangupRes(msg);
                hangupRes.handle();
                break;
            default:
                Header header = msg.getHeader();
                log.warn("Not Defined Type [{}:{}]", header.getTId(), header.getType());
                break;
        }
    }
}
