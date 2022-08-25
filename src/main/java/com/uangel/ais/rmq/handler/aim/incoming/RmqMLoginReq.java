package com.uangel.ais.rmq.handler.aim.incoming;

import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.service.aim.AimManager;
import com.uangel.ais.service.aim.AimSessionInfo;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqMLoginReq {
    static final Logger log = LoggerFactory.getLogger(RmqMLoginReq.class);
    private static final AimManager aimManager = AimManager.getInstance();

    public RmqMLoginReq() {
        // nothing
    }

    public void handle(Message msg) {
        AimSessionInfo sessionInfo = aimManager.getAimSession(0);

        if (sessionInfo == null) {
            sessionInfo = aimManager.createAimSession(0);
        }

        sessionInfo.updateLastHbTime();
        sessionInfo.setLoginFlag(true);

/*        if (sessionInfo.isTimeoutFlag()) {
            sessionInfo.setTimeoutFlag(false);
            log.error("[TIMEOUT] AIM Heartbeat Timeout ({})", sessionInfo.isTimeoutFlag());
        }*/

        RmqMsgSender.getInstance().sendMLoginRes(msg.getHeader().getTId());
    }
}
