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
import java.util.List;

@Controller("com.lessons.controllers.IndicatorController")
public class IndicatorController {
    private static final Logger logger = LoggerFactory.getLogger(IndicatorController.class);

    @Resource
    private IndicatorService indicatorService;

    //Endpoint is non-operational, see DTO and uncomment "count"
    @RequestMapping(value="/api/indicator/count", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getIndicatorCount()
    {
        logger.debug("getIndicatorCount() started.");

        IndicatorDTO res = new IndicatorDTO();
        //res.setCount(indicatorService.countIndicators());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @RequestMapping(value="/api/indicator/all", method=RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllIndicatorsController() {
        logger.debug("getAllIndicatorsController() Started");
        List<IndicatorDTO> listOfAllIndicators = indicatorService.getAllIndicatorsService();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listOfAllIndicators);
    }
}
