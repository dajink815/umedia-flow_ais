package com.uangel.ais.rmq.handler.aim.incoming;

import com.uangel.ais.rmq.handler.RmqIncomingMessage;
import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.service.aim.AimManager;
import com.uangel.ais.service.aim.AimSessionInfo;
import com.uangel.protobuf.MLoginReq;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqMLoginReq extends RmqIncomingMessage<MLoginReq> {
    static final Logger log = LoggerFactory.getLogger(RmqMLoginReq.class);
    private static final AimManager aimManager = AimManager.getInstance();

    public RmqMLoginReq(Message msg) {
        super(msg);
    }

    @Override
    public void handle() {
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

        RmqMsgSender.getInstance().sendMLoginRes(getTId());
    }
}
