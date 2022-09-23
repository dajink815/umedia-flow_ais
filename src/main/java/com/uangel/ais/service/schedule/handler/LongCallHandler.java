package com.uangel.ais.service.schedule.handler;

import com.uangel.ais.service.schedule.base.IntervalTaskUnit;
import com.uangel.ais.session.CallManager;
import com.uangel.ais.session.ReleaseSession;
import com.uangel.ais.util.DateFormatUtil;
import com.uangel.ais.util.SleepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author dajin kim
 */
public class LongCallHandler extends IntervalTaskUnit {
    private static final Logger log = LoggerFactory.getLogger(LongCallHandler.class);

    public LongCallHandler(int interval) {
        super(interval);
    }

    @Override
    public void run() {
        checkLongCall();
    }

    private void checkLongCall() {
        int longCallTimer = config.getLongCall();
        ReleaseSession releaseSession = new ReleaseSession();

        try {
            callManager.getCallIds().stream()
                    .map(callManager::getCallInfo)
                    .filter(Objects::nonNull)
                    .filter(callInfo -> callManager.checkLongCall(callInfo, longCallTimer))
                    .forEach(callInfo -> {

                        String createTime = DateFormatUtil.formatYmdHmsS(callInfo.getCreateTime());
                        log.warn("{} LONG CALL [T:{}] [C:{}] [State:{} {}]", callInfo.getLogHeader(),
                                longCallTimer, createTime, callInfo.getCallState(), callInfo.getRmqState());

                        releaseSession.release(callInfo, config.getServiceErr());

                    });
        } catch (Exception e) {
            log.error("LongCallHandler.checkLongCall.Exception ", e);
        }
    }
}
