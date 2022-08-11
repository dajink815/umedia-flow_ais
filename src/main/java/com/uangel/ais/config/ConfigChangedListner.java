/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

package com.uangel.ais.config;

public interface ConfigChangedListner {
    void configChanged(String k, Object v1, Object v2);
}
