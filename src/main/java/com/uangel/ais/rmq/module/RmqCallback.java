/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

package com.uangel.ais.rmq.module;

import java.util.Date;

/**
 * Rabbit MQ Callback
 *
 * @file RmqCallback.java
 * @author Tony Lim
 */
@FunctionalInterface
public interface RmqCallback {
    void onReceived(byte[] msg, Date ts);
}
