package com.uangel.ais.rmq.type;

/**
 * @author dajin kim
 */
public class RmqMsgType {

    private RmqMsgType() {
        // nothing
    }

    // AIS - AIM
    public static final String M_LOGIN_REQ = "M_LOGIN_REQ";
    public static final String M_LOGIN_RES = "M_LOGIN_RES";
    public static final String M_HB_REQ = "M_HB_REQ";
    public static final String M_HB_RES = "M_HB_RES";
    public static final String OFFER_REQ = "OFFER_REQ";
    public static final String OFFER_RES = "OFFER_RES";
    public static final String NEGO_REQ = "NEGO_REQ";
    public static final String NEGO_RES = "NEGO_RES";
    public static final String HANGUP_REQ = "HANGUP_REQ";
    public static final String HANGUP_RES = "HANGUP_RES";

    // AIS - AIWF
    public static final String W_HB_REQ = "W_HB_REQ";
    public static final String W_HB_RES = "W_HB_RES";
    public static final String CALL_INCOMING_REQ = "CALL_INCOMING_REQ";
    public static final String CALL_INCOMING_RES = "CALL_INCOMING_RES";
    public static final String CALL_START_REQ = "CALL_START_REQ";
    public static final String CALL_START_RES = "CALL_START_RES";
    public static final String CALL_CLOSE_REQ = "CALL_CLOSE_REQ";
    public static final String CALL_CLOSE_RES = "CALL_CLOSE_RES";
    public static final String CALL_STOP_REQ = "CALL_STOP_REQ";
    public static final String CALL_STOP_RES = "CALL_STOP_RES";

    // AIWF - AIIF
    public static final String I_HB_REQ = "I_HB_REQ";
    public static final String I_HB_RES = "I_HB_RES";
    public static final String CREATE_SESSION_REQ = "CREATE_SESSION_REQ";
    public static final String CREATE_SESSION_RES = "CREATE_SESSION_RES";
    public static final String TTS_START_REQ = "TTS_START_REQ";
    public static final String TTS_START_RES = "TTS_START_RES";
    public static final String TTS_RESULT_REQ = "TTS_RESULT_REQ";
    public static final String TTS_RESULT_RES = "TTS_RESULT_RES";
    public static final String STT_START_REQ = "STT_START_REQ";
    public static final String STT_START_RES = "STT_START_RES";
    public static final String STT_RESULT_REQ = "STT_RESULT_REQ";
    public static final String STT_RESULT_RES = "STT_RESULT_RES";
    public static final String DEL_SESSION_REQ = "DEL_SESSION_REQ";
    public static final String DEL_SESSION_RES = "DEL_SESSION_RES";

    // AIM - AIIF
    public static final String MEDIA_START_REQ = "MEDIA_START_REQ";
    public static final String MEDIA_START_RES = "MEDIA_START_RES";
    public static final String MEDIA_PLAY_REQ = "MEDIA_PLAY_REQ";
    public static final String MEDIA_PLAY_RES = "MEDIA_PLAY_RES";
    public static final String MEDIA_DONE_REQ = "MEDIA_DONE_REQ";
    public static final String MEDIA_DONE_RES = "MEDIA_DONE_RES";
    public static final String MEDIA_STOP_REQ = "MEDIA_STOP_REQ";
    public static final String MEDIA_STOP_RES = "MEDIA_STOP_RES";

    public static final int REASON_CODE_SUCCESS = 0;
    public static final int REASON_CODE_SUCCESS_200 = 200;
    public static final int REASON_CODE_NO_SESSION = 4000;
    public static final int REASON_CODE_SESSION_EXIST = 4001;


    public static final String REASON_SUCCESS = "SUCCESS";
    public static final String REASON_NO_SESSION = "NO_SESSION";
    public static final String REASON_SESSION_EXIST = "SESSION_EXIST";

    /**
     * @fn isRmqFail
     * @brief : RabbitMQ Message 응답 결과 실패 여부 확인
     * @param reasonCode : RMQ Response reasonCode
     * @return true  : 응답 실패
     *         false : 응답 성공
     * */
    public static boolean isRmqFail(int reasonCode) {
        return reasonCode != REASON_CODE_SUCCESS
                && reasonCode != REASON_CODE_SUCCESS_200;
    }

    public static boolean isRmqSuccess(int reasonCode) {
        return reasonCode == REASON_CODE_SUCCESS
                || reasonCode == REASON_CODE_SUCCESS_200;
    }
}
