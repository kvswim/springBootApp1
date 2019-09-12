package com.lessons.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller("com.lessons.controllers.EnvironmentController")
public class EnvironmentController {
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentController.class);

    @Value("${network.name}")
    private String networkName;

    @Value("${hdfs.enable}")
    private Boolean hdfsEnable;

    @Value("${show.classified.banner}")
    private Boolean showClassifiedBanner;

    @RequestMapping(value="/api/env", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<?> getEnvironmentVariables() {
        logger.debug("getEnvironmentVariables() started.");

        Map<String, Object> environmentMap = new HashMap<>();
        environmentMap.put("network.name", networkName);
        environmentMap.put("hdfs.enable", hdfsEnable);
        environmentMap.put("show.classified.banner", showClassifiedBanner);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environmentMap);
    }
}