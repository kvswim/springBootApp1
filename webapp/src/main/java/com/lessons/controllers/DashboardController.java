package com.lessons.controllers;


import com.lessons.models.ReportDTO;
import com.lessons.services.DashboardDao;
import com.lessons.services.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller("com.lessons.controllers.DashboardController")
public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Resource
    private DashboardDao dashboardDao;

    @Resource
    private ReportService reportService;

    public DashboardController()
    {
        logger.debug("DashboardController() constructor called.");
    }

    @PostConstruct
    public void dashboardControllerPostConstruct()
    {
        logger.debug("dashboardControllerPostConstruct() called.");
    }

    /*************************************************************************
     * getDateTime()
     * @return JSON string that holds the date/time
     *************************************************************************/
    @RequestMapping(value = "/api/dashboard/time", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDateTime() {
        logger.debug("getDashboardDetails() started.");

        // Get the date/time from the database
        String sDateTime = dashboardDao.getDatabaseTime();

        // Return the date/time string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(sDateTime);
    }

    @RequestMapping(value = "/api/nextseq", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getNextNumberInSequence() {
        logger.debug("getDashboardDetails() started.");

        // Get the date/time from the database
        int nextInSeq = dashboardDao.getNextValue();
        String result = "" + nextInSeq;

        // Return the date/time string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(result);
    }

    @RequestMapping(value = "/api/dashboard/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> addNewRecord(@RequestParam(name="description") String description) {
        logger.debug("getDashboardDetails() started.");


        // Get the date/time from the database
        dashboardDao.addNewRecord(description);

        // Return a status code 200
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("");
    }

    @RequestMapping(value="/api/reports" ,method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getOneReport(@RequestParam(name="id") Integer reportId)
    {
        logger.debug("getOneReport() started, reportId={}",reportId);

        Map<String, Object> resultingReport = reportService.getReport(reportId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resultingReport);
    }

    @RequestMapping(value="/api/reports/{id}" ,method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getOneReportPrettyURL(@PathVariable(name="id") Integer reportId)
    {
        logger.debug("getOneReport() started, reportId={}",reportId);

        Map<String, Object> resultingReport = reportService.getReport(reportId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resultingReport);
    }

    @RequestMapping(value="/api/reports/all", method=RequestMethod.GET, produces="application/json")
    public ResponseEntity<?> getAllReports()
    {
        logger.debug("getAllReports() started.");

        List<Map<String, Object>> resultingReports = reportService.getAllReports();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resultingReports);
    }

    @RequestMapping(value="api/reports/params", method=RequestMethod.GET, produces="application/json")
    public ResponseEntity<?> allParams(@RequestParam Map<String, Object> inputParamMap)
    {
        logger.debug("allParams() started.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inputParamMap);
    }

    @RequestMapping(value="/api/reports/add", method=RequestMethod.POST, produces="application/json")
    public ResponseEntity<?> add(@RequestBody ReportDTO reportDTO)
    {
        logger.debug("add() started.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reportDTO);
    }

}