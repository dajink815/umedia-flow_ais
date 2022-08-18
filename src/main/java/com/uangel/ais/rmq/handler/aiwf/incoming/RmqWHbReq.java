package com.uangel.ais.rmq.handler.aiwf.incoming;

import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.service.aim.AimSessionInfo;
import com.uangel.ais.service.aiwf.AiwfManager;
import com.uangel.ais.service.aiwf.AiwfSessionInfo;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqWHbReq {
    static final Logger log = LoggerFactory.getLogger(RmqWHbReq.class);
    private static final AiwfManager aiwfManager = AiwfManager.getInstance();

    public RmqWHbReq() {
        // nothing
    }

    public void handle(Message msg) {

        AiwfSessionInfo sessionInfo = aiwfManager.getAiwfSession(0);

        if (sessionInfo == null) {
            sessionInfo = aiwfManager.createAwifSession(0);
        }

        sessionInfo.updateLastHbTime();

        if (sessionInfo.isTimeoutFlag()) {
            sessionInfo.setTimeoutFlag(false);
            log.error("[TIMEOUT] AIWF Heartbeat Timeout ({})", sessionInfo.isTimeoutFlag());
        }

        RmqMsgSender.getInstance().sendWHbRes(msg.getHeader().getTId());
    }
}
