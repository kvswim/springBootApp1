package com.lessons.controllers;

import com.lessons.models.IndicatorDTO;
import com.lessons.services.IndicatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller("com.lessons.controllers.IndicatorController")
public class IndicatorController {
    private static final Logger logger = LoggerFactory.getLogger(IndicatorController.class);

    @Resource
    private IndicatorService indicatorService;

    @RequestMapping(value="/api/indicator/count", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getIndicatorCount()
    {
        logger.debug("getIndicatorCount() started.");

        IndicatorDTO res = new IndicatorDTO();
        res.setCount(indicatorService.countIndicators());

        return ResponseEntity
                .status(HttpStatus.I_AM_A_TEAPOT)
                .body(res);
    }
}
