/*
 * Copyright (C) 2019. Uangel Corp. All rights reserved.
 *
 */

package com.uangel.ais.config;

import org.apache.commons.configuration2.Configuration;

import java.util.Iterator;
import java.util.Objects;

public class DiffConfig {

    private DiffConfig() {
        // nothing
    }

    public static void diff(Configuration config1, Configuration config2, ConfigChangedListner l) {
        for (Iterator<String> keys = config1.getKeys(); keys.hasNext(); ) {
            String key = keys.next();
            Object v1 = config1.getProperty(key);
            Object v2 = config2.getProperty(key);
            if (!Objects.equals(v1, v2)) {
                l.configChanged(key, v1, v2);
            }
        }

        for (Iterator<String> keys = config2.getKeys(); keys.hasNext(); ) {
            String key = keys.next();
            Object v2 = config2.getProperty(key);
            if (!config1.containsKey(key)) {
                l.configChanged(key, null, v2);
            }
        }
    }
}
