package com.uangel.ais.service.schedule.handler;

import com.uangel.ais.service.schedule.base.IntervalTaskUnit;
import com.uangel.ais.session.ReleaseSession;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.session.state.RmqState;
import com.uangel.ais.util.DateFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author dajin kim
 */
public class RmqTimeoutHandler extends IntervalTaskUnit {
    private static final Logger log = LoggerFactory.getLogger(RmqTimeoutHandler.class);

    public RmqTimeoutHandler(int interval) {
        super(interval);
    }

    protected static final Set<RmqState> timeoutStates = EnumSet.of(
            RmqState.INCOMING,
            RmqState.OFFER_REQ,
            RmqState.NEGO_REQ,
            RmqState.STOP
    );

    @Override
    public void run() {
        checkRmqTimeout();
    }

    private void checkRmqTimeout() {
        int rmqTimer = config.getRmqTimeout();
        ReleaseSession releaseSession = new ReleaseSession();

        try {
            callManager.getCallIds().stream()
                    .map(callManager::getCallInfo).filter(Objects::nonNull)
                    .forEach(callInfo -> {
                        try {
                            callInfo.lock();

                            RmqState rmqState = callInfo.getRmqState();
                            if (!timeoutStates.contains(rmqState) || !callManager.checkRmqTimeout(callInfo, rmqTimer)) return;

                            String lastRmqTime = DateFormatUtil.formatYmdHmsS(callInfo.getLastRmqTime());
                            log.warn("{}{} TIMEOUT, [T:{}] [S:{}] [State:{}]", callInfo.getLogHeader(),
                                    rmqState, rmqTimer, lastRmqTime, callInfo.getCallState());

                            if (RmqState.STOP.equals(rmqState)) {
                                callInfo.setCallState(CallState.IDLE);
                                callInfo.setRmqState(RmqState.IDLE);
                            } else {
                                // ????????? ??? ?????? ?????? ???????
                                releaseSession.release(callInfo, config.getTimeoutErr());
                            }

                        } finally {
                            callInfo.unlock();
                        }

                    });
        } catch (Exception e) {
            log.error("RmqTimeoutHandler.checkRmqTimeout.Exception ", e);
        }
    }
}
