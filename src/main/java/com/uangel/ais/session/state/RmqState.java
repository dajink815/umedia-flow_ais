package com.uangel.ais.session.state;

/**
 * @author dajin kim
 */
public enum RmqState {
    NEW, IDLE,
    // AIWF
    INCOMING, START, CLOSE, STOP,
    // AIM
    OFFER_REQ, OFFER_RES, NEGO_REQ, NEGO_RES, HANGUP_REQ, HANGUP_RES
}
