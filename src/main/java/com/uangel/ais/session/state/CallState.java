package com.uangel.ais.session.state;

/**
 * @author dajin kim
 */
public enum CallState {
    NEW, IDLE,
    INVITE, TRYING, RINGING,
    INVITE_OK, CONNECT, BYE, BYE_OK,
    ERROR, CANCEL, CANCEL_OK
}
