package com.uangel.ais.rmq.handler.aim.incoming;

import com.uangel.ais.rmq.handler.RmqIncomingMessage;
import com.uangel.ais.rmq.handler.RmqMsgSender;
import com.uangel.ais.service.aim.AimManager;
import com.uangel.ais.service.aim.AimSessionInfo;
import com.uangel.protobuf.MLoginReq;
import com.uangel.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.uangel.ais.rmq.type.RmqMsgType.*;

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
        AimSessionInfo sessionInfo = aimManager.createAimSession(0);
        sessionInfo.updateLastHbTime();

        if (sessionInfo.isLoginFlag()) {
            // Send LoginRes Fail
            RmqMsgSender.getInstance().sendMLoginRes(getTId(), REASON_CODE_SESSION_EXIST, REASON_SESSION_EXIST);
        } else {
            // Send LoginRes Success
            sessionInfo.setLoginFlag(true);
            RmqMsgSender.getInstance().sendMLoginRes(getTId());
        }

    }
}
