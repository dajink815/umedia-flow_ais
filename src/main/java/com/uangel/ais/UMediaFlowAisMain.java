package com.uangel.ais;

import com.uangel.ais.service.AppInstance;
import com.uangel.ais.service.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class UMediaFlowAisMain {
    static final Logger log = LoggerFactory.getLogger(UMediaFlowAisMain.class);

    public static void main(String[] args) {
        log.info("Process Start [{}]", args[0]);
        AppInstance instance = AppInstance.getInstance();
        instance.setConfigPath(args[0]);

        ServiceManager serviceManager = ServiceManager.getInstance();
        serviceManager.loop();
    }
}
