package com.uangel.ais.rmq.handler.aiwf;

import com.uangel.ais.rmq.handler.RmqOutgoingMessage;
import com.uangel.ais.service.AppInstance;

/**
 * @author dajin kim
 */
public class RmqAiwfOutgoing extends RmqOutgoingMessage {

    public RmqAiwfOutgoing() {
        super(AppInstance.getInstance().getConfig().getAiwf());
    }
}
