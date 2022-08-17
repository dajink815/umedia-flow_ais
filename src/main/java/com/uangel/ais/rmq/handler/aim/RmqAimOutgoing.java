package com.uangel.ais.rmq.handler.aim;

import com.uangel.ais.rmq.handler.RmqOutgoingMessage;
import com.uangel.ais.service.AppInstance;

/**
 * @author dajin kim
 */
public class RmqAimOutgoing extends RmqOutgoingMessage {

    public RmqAimOutgoing() {
        super(AppInstance.getInstance().getConfig().getAiwf());
    }
}
