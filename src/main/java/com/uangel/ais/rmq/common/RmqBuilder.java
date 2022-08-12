package com.uangel.ais.rmq.common;

import com.uangel.ais.service.AppInstance;
import com.uangel.ais.util.DateFormatUtil;
import com.uangel.rmq.message.RmqHeader;
import com.uangel.ais.rmq.common.RmqMsgType;

import java.util.UUID;

/**
 * @author dajin kim
 */
public class RmqBuilder {

    private RmqBuilder() {
        // nothing
    }

    public static RmqHeader.Builder getDefaultHeader(String type) {
        return RmqHeader.newBuilder()
                .setType(type)
                .setTId(UUID.randomUUID().toString())
                .setMsgFrom(AppInstance.getInstance().getConfig().getAiif())
                .setReason(RmqMsgType.REASON_SUCCESS)
                .setReasonCode(RmqMsgType.REASON_CODE_SUCCESS)
                .setTimestamp(DateFormatUtil.currentTimeStamp());
    }

    public static RmqHeader.Builder getFailHeader(String type, String reason, int reasonCode) {
        return RmqHeader.newBuilder()
                .setType(type)
                .setTId(UUID.randomUUID().toString())
                .setMsgFrom(AppInstance.getInstance().getConfig().getAiif())
                .setReason(reason)
                .setReasonCode(reasonCode)
                .setTimestamp(DateFormatUtil.currentTimeStamp());
    }

}
