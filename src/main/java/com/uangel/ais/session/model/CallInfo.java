package com.uangel.ais.session.model;

import com.uangel.ais.session.state.CallState;
import com.uangel.ais.session.state.RmqState;
import com.uangel.ais.session.type.CallType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stack.java.uangel.sip.ClientTransaction;
import stack.java.uangel.sip.Dialog;
import stack.java.uangel.sip.ServerTransaction;
import stack.java.uangel.sip.header.ViaHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dajin kim
 */
public class CallInfo {
    private static final Logger log = LoggerFactory.getLogger(CallInfo.class);
    private final ReentrantLock lock = new ReentrantLock();

    private final String callId;
    private final long createTime;
    private long lastRmqTime;

    // State
    private CallState callState;
    private RmqState rmqState;

    // Type
    private CallType callType;

    // SIP 정보
    private String toMdn;
    private String fromMdn;
    private String toIp;
    private String fromIp;
    private int toPort;
    private int fromPort;
    private String toTag;
    private String fromTag;
    private String toAddress;
    private String fromAddress;
    private long cSeq;
    private String sdp;
    private String contact;

    private ConcurrentMap<String, List<String>> requestHeader;
    private ConcurrentMap<String, List<String>> responseHeader;
    private ServerTransaction inviteSt; // InInvite
    private ClientTransaction byeCt;    // OutBye
    private Dialog dialog;
    private List<ViaHeader> viaHeader = new ArrayList<>();

    private boolean recvCancel;
    private boolean isSendStop;

    private String logHeader = "";

    public CallInfo(String callId) {
        this.callId = callId;
        this.createTime = System.currentTimeMillis();
        this.setLogHeader();
        // status
        this.callState = CallState.NEW;
        this.rmqState = RmqState.NEW;
    }

    // lock methods
    public void lock(){
        this.lock.lock();
    }
    public void unlock(){
        this.lock.unlock();
    }
    public void handleLock(Runnable r){
        try{
            this.lock.lock();
            r.run();
        } finally {
            this.lock.unlock();
        }
    }

    public String getCallId() {
        return callId;
    }
    public long getCreateTime() {
        return createTime;
    }

    public String getLogHeader() {
        return logHeader;
    }
    private void setLogHeader() {
        this.logHeader = "() ("+ this.callId + ") () ";
    }

    public long getLastRmqTime() {
        return lastRmqTime;
    }
    public void setLastRmqTime(long lastRmqTime) {
        this.lastRmqTime = lastRmqTime;
    }
    public void updateLastRmqTime() {
        this.lastRmqTime = System.currentTimeMillis();
    }

    // State
    public CallState getCallState() {
        return callState;
    }
    public void setCallState(CallState callState) {
        if (this.callState == null || !this.callState.equals(callState)) {
            log.info("{}CALL Status Changed [{}] --> [{}]", logHeader, this.callState, callState);
            this.callState = callState;
        }
    }

    public RmqState getRmqState() {
        return rmqState;
    }
    public void setRmqState(RmqState rmqState) {
        if (this.rmqState == null || !this.rmqState.equals(rmqState)) {
            log.info("{}RMQ Status Changed [{}] --> [{}]", logHeader, this.rmqState, rmqState);
            this.rmqState = rmqState;
        }
    }

    // Type
    public CallType getCallType() {
        return callType;
    }
    public void setCallType(CallType callType) {
        if (this.callType == null) {
            this.callType = callType;
        } else {
            log.warn("{}Cannot Change CallType", logHeader);
        }
    }

    // SIP 정보
    public String getToMdn() {
        return toMdn;
    }
    public void setToMdn(String toMdn) {
        this.toMdn = toMdn;
    }

    public String getFromMdn() {
        return fromMdn;
    }
    public void setFromMdn(String fromMdn) {
        this.fromMdn = fromMdn;
    }

    public String getToIp() {
        return toIp;
    }
    public void setToIp(String toIp) {
        this.toIp = toIp;
    }

    public String getFromIp() {
        return fromIp;
    }
    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public int getToPort() {
        return toPort;
    }
    public void setToPort(int toPort) {
        this.toPort = toPort;
    }

    public int getFromPort() {
        return fromPort;
    }
    public void setFromPort(int fromPort) {
        this.fromPort = fromPort;
    }

    public String getToTag() {
        return toTag;
    }
    public void setToTag(String toTag) {
        this.toTag = toTag;
    }

    public String getFromTag() {
        return fromTag;
    }
    public void setFromTag(String fromTag) {
        this.fromTag = fromTag;
    }

    public String getToAddress() {
        return toAddress;
    }
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public long getCSeq() {
        return cSeq;
    }
    public void setCSeq(long cSeq) {
        this.cSeq = cSeq;
    }

    public String getSdp() {
        return sdp;
    }
    public void setSdp(String sdp) {
        this.sdp = sdp;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public ConcurrentMap<String, List<String>> getRequestHeader() {
        return requestHeader;
    }
    public void setRequestHeader(ConcurrentMap<String, List<String>> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public ConcurrentMap<String, List<String>> getResponseHeader() {
        return responseHeader;
    }
    public void setResponseHeader(ConcurrentMap<String, List<String>> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public ServerTransaction getInviteSt() {
        return inviteSt;
    }
    public void setInviteSt(ServerTransaction inviteSt) {
        if (this.inviteSt != null && this.inviteSt != inviteSt) {
            try {
                log.info("{}Terminate INVITE server transaction", this.logHeader);
                this.inviteSt.terminate();
            } catch (Exception e) {
                log.error("CallInfo.setInviteSt.Exception", e);
            }
        }
        this.inviteSt = inviteSt;
    }

    public ClientTransaction getByeCt() {
        return byeCt;
    }
    public void setByeCt(ClientTransaction byeCt) {
        if (this.byeCt != null && this.byeCt != byeCt) {
            try {
                log.info("{}Terminate BYE client transaction", this.logHeader);
                this.byeCt.terminate();
            } catch (Exception e) {
                log.error("CallInfo.setByeCt.Exception", e);
            }
        }
        this.byeCt = byeCt;
    }

    public Dialog getDialog() {
        return dialog;
    }
    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public List<ViaHeader> getViaHeader() {
        return viaHeader;
    }
    public void setViaHeader(List<ViaHeader> viaHeader) {
        this.viaHeader = viaHeader;
    }

    public boolean isRecvCancel() {
        return recvCancel;
    }
    public void setRecvCancel(boolean recvCancel) {
        this.recvCancel = recvCancel;
    }

    public boolean isSendStop() {
        return isSendStop;
    }
    public void setSendStop(boolean sendStop) {
        isSendStop = sendStop;
    }
}
