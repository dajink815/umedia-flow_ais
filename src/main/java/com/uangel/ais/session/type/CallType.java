package com.uangel.ais.session.type;

/**
 * @author dajin kim
 */
public enum CallType {

    INBOUND(0),
    OUTBOUND(1);

    private final int value;

    CallType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CallType getTypeEnum(String type) {
        switch (type.toUpperCase()) {
            case "OUTBOUND": return OUTBOUND;
            case "INBOUND":
            default:
                return INBOUND;
        }
    }
}
