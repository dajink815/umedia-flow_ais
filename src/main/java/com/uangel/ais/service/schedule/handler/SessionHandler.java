package com.uangel.ais.service.schedule.handler;

import com.uangel.ais.service.schedule.base.IntervalTaskUnit;
import com.uangel.ais.session.state.CallState;
import com.uangel.ais.util.DateFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author dajin kim
 */
public class SessionHandler extends IntervalTaskUnit {
    static final Logger log = LoggerFactory.getLogger(SessionHandler.class);

    public SessionHandler(int interval) {
        super(interval);
    }

    @Override
    public void run() {
        checkSession();
    }

    private void checkSession() {
        try {
            callManager.getCallIds().stream()
                    .map(callManager::getCallInfo).filter(Objects::nonNull)
                    .forEach(callInfo -> {

                        try {
                            callInfo.lock();

                            // Delete IDLE Session
                            if (CallState.IDLE.equals(callInfo.getCallState())) {
                                String creteTime = DateFormatUtil.formatYmdHmsS(callInfo.getCreateTime());
                                log.info("{}Removed Call Session : CreateTime [{}]", callInfo.getLogHeader(), creteTime);
                                callManager.deleteCallInfo(callInfo.getCallId());
                            }

                        } finally {
                            callInfo.unlock();
                        }

                    });
        } catch (Exception e) {
            log.error("SessionHandler.checkSession.Exception ", e);
        }
    }
}
