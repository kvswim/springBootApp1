package com.lessons.controllers;

import com.lessons.filter.FilterService;
import com.lessons.models.ShortReportDTO;
import com.lessons.services.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class ReportsController {

    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);

    @Resource
    private ReportService reportService;

    @Resource
    private FilterService filterService;

    @RequestMapping(value="/api/reports/{id}" ,method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteReport(@PathVariable(name="id") Integer reportId)
    {
        logger.debug("deleteReport() started, reportId={}", reportId);

        if(!reportService.verifyReportExists(reportId)) {
            return ResponseEntity
                    .status(HttpStatus.I_AM_A_TEAPOT)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Can't brew coffee because I'm a teapot!");
        } else {
            reportService.deleteReportService(reportId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(reportId + "deleted successfully!");
        }
    }

    @RequestMapping(value="/api/reports/short", method=RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getShortReports() {
        logger.debug("getShortReports() started.");
        List<ShortReportDTO> listOfShortReportDTOs = reportService.getAllShortReports();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listOfShortReportDTOs);
    }

    // /api/reports/filtered
    //gets list<string> of filters
    //return list of DTOs
    @RequestMapping(value="/api/reports/filtered", method=RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getFiltered(@RequestParam(name="filters", required = false) List<String> filters)
    {
        if( !filterService.areFiltersValid(filters) )
        {
            return ResponseEntity
                    .status(HttpStatus.I_AM_A_TEAPOT) //mere mortals use 400 bad request
                    .body("Passed in filters were invalid.");
        }

        List<ShortReportDTO> listOfShortReportDTOs = reportService.getFilteredReports(filters);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listOfShortReportDTOs);
    }
}
