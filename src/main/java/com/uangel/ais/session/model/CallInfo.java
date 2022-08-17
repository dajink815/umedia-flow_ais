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

    private ConcurrentMap<String, List<String>> requestHeader;
    private ConcurrentMap<String, List<String>> responseHeader;
    private ServerTransaction inviteSt;
    private ClientTransaction cancelCt;
    private Dialog dialog;
    private List<ViaHeader> viaHeader = new ArrayList<>();
    // sip message
    private String inviteMsg;
    private String startSipMsg;
    private String stopSipMsg;

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

    public long getcSeq() {
        return cSeq;
    }
    public void setcSeq(long cSeq) {
        this.cSeq = cSeq;
    }

    public String getSdp() {
        return sdp;
    }
    public void setSdp(String sdp) {
        this.sdp = sdp;
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

    // todo
    public void setInviteSt(ServerTransaction inviteSt) {
        this.inviteSt = inviteSt;
    }

    public ClientTransaction getCancelCt() {
        return cancelCt;
    }
    public void setCancelCt(ClientTransaction cancelCt) {
        this.cancelCt = cancelCt;
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

    public String getInviteMsg() {
        return inviteMsg;
    }
    public void setInviteMsg(String inviteMsg) {
        this.inviteMsg = inviteMsg;
    }

    public String getStartSipMsg() {
        return startSipMsg;
    }
    public void setStartSipMsg(String startSipMsg) {
        this.startSipMsg = startSipMsg;
    }

    public String getStopSipMsg() {
        return stopSipMsg;
    }
    public void setStopSipMsg(String stopSipMsg) {
        this.stopSipMsg = stopSipMsg;
    }
}
