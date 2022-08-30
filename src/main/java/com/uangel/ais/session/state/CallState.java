package com.uangel.ais.session.state;

/**
 * @author dajin kim
 */
public enum CallState {
    NEW, IDLE,
    INVITE, TRYING, RINGING, CONNECT,
    BYE, ERROR, CANCEL
}
