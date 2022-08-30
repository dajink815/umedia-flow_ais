package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.rmq.handler.RmqIncomingMessage;
import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.service.aiwf.AiwfManager;
import com.uangel.ais.service.aiwf.AiwfSessionInfo;
import com.uangel.protobuf.Message;
import com.uangel.protobuf.WHbReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.uangel.ais.service.ServiceDefine.ABNORMAL;
import static com.uangel.ais.service.ServiceDefine.NORMAL;

/**
 * @author dajin kim
 */
public class RmqWHbReq extends RmqIncomingMessage<WHbReq> {
    static final Logger log = LoggerFactory.getLogger(RmqWHbReq.class);
    private static final AiwfManager aiwfManager = AiwfManager.getInstance();

    public RmqWHbReq(Message message) {
        super(message);
    }

    @Override
    public void handle() {

        int status = body.getStatus();
        AiwfSessionInfo sessionInfo = aiwfManager.getAiwfSession(0);

        if (sessionInfo == null) {
            sessionInfo = aiwfManager.createAwifSession(0);
        }

        if (status == ABNORMAL.getNum()) {
            log.info("AIWF HB Status False");
            sessionInfo.setStatus(false);
        } else if (status == NORMAL.getNum()) {
            sessionInfo.setStatus(true);
        }

        sessionInfo.updateLastHbTime();

        if (sessionInfo.isTimeoutFlag()) {
            sessionInfo.setTimeoutFlag(false);
            log.error("[TIMEOUT] AIWF Heartbeat Timeout ({})", sessionInfo.isTimeoutFlag());
        }

        RmqMsgSender.getInstance().sendWHbRes(getTId());
    }
}
