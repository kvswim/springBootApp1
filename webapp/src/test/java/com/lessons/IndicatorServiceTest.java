package com.lessons;

import com.lessons.controllers.IndicatorController;
import com.lessons.models.IndicatorDTO;
import com.lessons.services.IndicatorService;
import com.lessons.utils.GenericExceptionHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)    // Required to work with JUnit 4
@SpringBootTest                 // Start up a Spring Application Context
public class IndicatorServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(IndicatorServiceTest.class);

    @Resource
    private IndicatorService indicatorService;

    @Resource
    private IndicatorController indicatorController;

    @Test
    public void testCase1() {
        logger.debug("Inside testcase1");
        Integer totalIndicatorsInDatabase = indicatorService.countIndicators();
        assertTrue("The number of indicators in db did not meet expectations", (totalIndicatorsInDatabase == 2));
        logger.debug("Done with testcase1");
    }

    //no worky
//    @Test
//    public void testCase2() {
//        logger.debug("Inside testcase2");
//        ResponseEntity<?> localIndicatorsInDatabase = indicatorController.getIndicatorCount();
//        IndicatorDTO dto = (IndicatorDTO) localIndicatorsInDatabase.getBody();
//        String stringResult = dto.getValue();
//        logger.debug("Done with testcase2");
//    }

    public IndicatorServiceTest() {
        logger.debug("in constructor");
    }

    @PostConstruct
    public void indicatorServiceTestPostConstruct() {
        logger.debug("in postconstructor");
    }
}
