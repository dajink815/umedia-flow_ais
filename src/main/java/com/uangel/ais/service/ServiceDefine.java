package com.uangel.ais.service;

/**
 * @author dajin kim
 */
public enum ServiceDefine {

    TRANSPORT("udp"), MAX_FORWARDS(70);

    private String str;
    private int num;

    ServiceDefine(String str) {
        this.str = str;
    }

    ServiceDefine(int num) {
        this.num = num;
    }

    public String getStr() {
        return str;
    }

    public int getNum() {
        return num;
    }
}
