package com.uangel.ais.signal.util;

import com.uangel.ais.session.model.CallInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ObjectInUseException;

/**
 * Sip 에 관련된 Transaction Terminate
 * Transaction State 가 Terminate 가 되지 않은 Transaction 에 대해서 Delete
 *
 * @file TerminateTransaction.java
 * @author youjin Choi
 */
public class TerminateTransaction {
    static final Logger log = LoggerFactory.getLogger(TerminateTransaction.class);

    private TerminateTransaction() {
        // nothing
    }

    public static void sipTransactionTerminate(CallInfo callInfo) {
        log.debug("{}TerminateTransaction.sipTransactionTerminate", callInfo.getLogHeader());
        try {
            if (callInfo.getInviteSt() != null) {
                log.debug("{}sipTransactionTerminate Server Transaction state is [{}]", callInfo.getLogHeader(), callInfo.getInviteSt().getState());
                callInfo.getInviteSt().terminate();
            }

            if (callInfo.getByeCt() != null) {
                log.debug("{}sipTransactionTerminate Client Transaction state is [{}]", callInfo.getLogHeader(), callInfo.getByeCt().getState());
                callInfo.getByeCt().terminate();
            }

            if (callInfo.getDialog() != null) {
                log.debug("{}sipTransactionTerminate Dialog state is [{}]", callInfo.getLogHeader(), callInfo.getDialog().getState());
                callInfo.getDialog().delete();
            }
        } catch (ObjectInUseException e) {
            log.error("TerminateTransaction.ObjectInUseException", e);
        }
    }

}
