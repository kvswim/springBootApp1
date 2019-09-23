package com.lessons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)    // Required to work with JUnit 4
@SpringBootTest                 // Start up a Spring Application Context
public class AddTestRecords {
    private static final Logger logger = LoggerFactory.getLogger(AddTestRecords.class);

    @Resource
    private DataSource dataSource;

    @Test
    public void insertTestRecords() {
        //Generate 50,000 reports with 1-500 indicators each, containing random data
        int totalReportsToCreate = 50000;
        int maxNumberOfIndicatorsPerReport = 500;

        //Generate multiline string for reports
        long start = System.currentTimeMillis();
        logger.debug("Adding " + totalReportsToCreate + " records...");
        createReports(totalReportsToCreate);
        long end = System.currentTimeMillis();
        long timeToCreateReports = end - start;
        logger.debug("Done adding reports. Took: {} ms", timeToCreateReports);

        start = System.currentTimeMillis();
        logger.debug("Starting to add indicators to reports...");
        for (int i = 1; i <= totalReportsToCreate; i++) {
            createIndicators(i, getRandomNumber(1, maxNumberOfIndicatorsPerReport));
            if ((i % 100) == 0) {
                logger.debug("Adding indicators to report {} of {}", i, totalReportsToCreate);
            }
        }
        end = System.currentTimeMillis();
        long timeToCreateIndicators = end - start;
        logger.debug("Done adding indicators to reports. Took: {} ms", timeToCreateIndicators);

        logger.debug("Done generating data. Reports took: {} ms. Indicators took: {} ms.", timeToCreateReports, timeToCreateIndicators);
    }

/*
    private void createReport(int reportId) {
        JdbcTemplate jt = new JdbcTemplate(dataSource);

        int passedInReportId = reportId;
        String displayName = "Report" + passedInReportId + ".txt";
        boolean isCustomReport = getRandomBoolean();

        //Stage 1: Insert report
        String sql = "insert into reports(id, display_name, is_custom_report) values (?, ?, ?)";
        jt.update(sql, passedInReportId, displayName, isCustomReport);

        //Stage 2: Insert Reports Audit
//        sql = "insert into reports_aud"
    }
 */

    private void  createReports(int totalReportsToCreate)
    {
        JdbcTemplate jt = new JdbcTemplate(dataSource);
        String multiLineReportString = "";
        for(int i=1; i<= totalReportsToCreate; i++)
        {
            //id, display_name, is_custom_report
            String displayName = "Report" + i + ".txt";
            boolean isCustomReport = getRandomBoolean();
            multiLineReportString = multiLineReportString.concat("("+ i + ",'" + displayName + "'," + isCustomReport + "),");
        }
        multiLineReportString = multiLineReportString.substring(0, multiLineReportString.length() - 1);
        String sql = "insert into reports(id, display_name, is_custom_report) values " + multiLineReportString;
        jt.update(sql);


    }

    private void createIndicators(int reportId, int totalNumberOfIndicators) {
        JdbcTemplate jt = new JdbcTemplate(dataSource);

        //Stage 1a: Generate indicator data as long multiline insert string
        //Stage 1b: Simultaneously generate audit data as long multiline insert string
        //Stage 1c: Simultaneously generate link_reports_indicators data
        //Stage 2: Insert muliline string into indicators
        //Stage 3: Insert multiline string into link_reports_indicators
        //Stage 4: Insert audit string into indicators_aud

        Map<String, String> multilineStringMap = getMultilineStrings(reportId, totalNumberOfIndicators);

        String sql = "insert into indicators(id, value, ind_type) values " + multilineStringMap.get("multilineIndicatorString");
        jt.update(sql);

        sql = "insert into link_reports_indicators(id, report, indicator) values " + multilineStringMap.get("multilineLriString");
        jt.update(sql);

        sql = "insert into indicators_aud(id, value, ind_type, rev, rev_type, timestamp, username) values " + multilineStringMap.get("multilineAuditString");
        jt.update(sql);

        //Single line insert implementation. This is f@#$ing slow!
        /*for (int i=1; i<=totalNumberOfIndicators; i++)
        {
            //Stage 1: Generate indicator data
            Integer indicatorId = getUniqueID();
            String value = getRandomIP();
            Integer indicatorType = getRandomNumber(1, 5);

            //Stage 2: Insert Indicator
            String sql = "insert into indicators(id, value, ind_type) values (?, ?, ?)";
            jt.update(sql, indicatorId, value, indicatorType);

            //Stage 3: Insert Link_Reports_Indicators
            sql = "insert into link_reports_indicators(id, report, indicator) values (?, ?, ?)";
            jt.update(sql, getUniqueID(), reportId, indicatorId);

            //Stage 4: Insert Audit Record
            sql = "insert into indicators_aud(id, value, ind_type, rev, rev_type, timestamp, username) values (?, ?,?,?,?,now(), 'Me')";
            jt.update(sql, indicatorId, value, indicatorType, getUniqueID(), 0);
        }*/
    }

    private Integer getUniqueID() {
        JdbcTemplate jt = new JdbcTemplate(dataSource);
        String sql = "select nextval('seq_table_ids')";
        Integer returnValue = jt.queryForObject(sql, Integer.class);
        return returnValue;
    }

    private int getRandomNumber(int min, int max) {
        int randomNumber = ThreadLocalRandom.current().nextInt(min, max);
        return randomNumber;
    }

    private String getRandomIP() {
        String resultingIP;
        int firstOctet = getRandomNumber(1, 255);
        int secondOctet = getRandomNumber(0, 255);
        int thirdOctet = getRandomNumber(0, 255);
        int fourthOctet = getRandomNumber(1, 255);
        resultingIP = firstOctet + "." + secondOctet + "." + thirdOctet + "." + fourthOctet;

        return resultingIP;
    }

    private boolean getRandomBoolean() {
        int randomNumber = getRandomNumber(1, 2);
        if (randomNumber == 1) {
            return true;
        } else {
            return false;
        }
    }

    private Map<String, Integer[]> getUniqueIds(int totalNumberOfIndicators)
    {
        Set<Integer> setOfUniqueIndicatorIds = new HashSet<>();
        while(setOfUniqueIndicatorIds.size() < totalNumberOfIndicators)
        {
            setOfUniqueIndicatorIds.add(getRandomNumber(0, Integer.MAX_VALUE));
        }
        Set<Integer> setOfUniqueRevIds = new HashSet<>();
        while(setOfUniqueRevIds.size() < totalNumberOfIndicators)
        {
            Integer numberToAdd = getRandomNumber(0, Integer.MAX_VALUE);
            if(!setOfUniqueIndicatorIds.contains(numberToAdd))
            {
                setOfUniqueRevIds.add(numberToAdd);
            }
        }
        Set<Integer> setOfUniqueLriIds = new HashSet<>();
        while(setOfUniqueLriIds.size() < totalNumberOfIndicators)
        {
            Integer numberToAdd = getRandomNumber(0, Integer.MAX_VALUE);
            if(!setOfUniqueIndicatorIds.contains(numberToAdd) && !setOfUniqueRevIds.contains(numberToAdd))
            {
                setOfUniqueLriIds.add(numberToAdd);
            }
        }
        Integer[] arrayOfUniqueIndicatorIds = setOfUniqueIndicatorIds.toArray(new Integer[totalNumberOfIndicators]);
        Integer[] arrayOfUniqueRevIds = setOfUniqueRevIds.toArray(new Integer[totalNumberOfIndicators]);
        Integer[] arrayOfUniqueLriIds = setOfUniqueLriIds.toArray(new Integer[totalNumberOfIndicators]);

        Map<String, Integer[]> mapOfUniqueIds = new HashMap<>();
        mapOfUniqueIds.put("arrayOfUniqueIndicatorIds", arrayOfUniqueIndicatorIds);
        mapOfUniqueIds.put("arrayOfUniqueRevIds", arrayOfUniqueRevIds);
        mapOfUniqueIds.put("arrayOfUniqueLriIds", arrayOfUniqueLriIds);

        return mapOfUniqueIds;
    }

    private Map<String, String> getMultilineStrings(int reportId, int totalNumberOfIndicators)
    {
        String multilineIndicatorString = "";
        String multilineAuditString = "";
        String multilineLriString = "";
        String username = "TEST_USER";

        Map<String, Integer[]> mapOfUniqueIds = getUniqueIds(totalNumberOfIndicators);
        Integer[] arrayOfUniqueIndicatorIds = mapOfUniqueIds.get("arrayOfUniqueIndicatorIds");
        Integer[] arrayOfUniqueRevIds = mapOfUniqueIds.get("arrayOfUniqueRevIds");
        Integer[] arrayOfUniqueLriIds = mapOfUniqueIds.get("arrayOfUniqueLriIds");

        for (int i = 0; i < totalNumberOfIndicators; i++) {
            Integer indicatorId = arrayOfUniqueIndicatorIds[i];
            String value = getRandomIP();
            Integer indicatorType = getRandomNumber(1, 5);
            Integer revId = arrayOfUniqueRevIds[i];
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Integer lriId = arrayOfUniqueLriIds[i];
            //Indicator: indicatorId, value, indicatorType
            //Link_reports_indicators: lriId, reportId, indicatorId
            //Audit: indicatorId, value, indicatorType, revId, 0 (rev_type=create), Timestamp, Username
            multilineIndicatorString = multilineIndicatorString + ("(" + indicatorId + ",'" + value + "'," + indicatorType + "),");
            multilineLriString = multilineLriString + ("(" + lriId + "," + reportId + "," + indicatorId + "),");
            multilineAuditString = multilineAuditString + ("(" + indicatorId + ",'" + value + "'," + indicatorType + "," + revId + ",0,'" + timestamp + "','" + username + "'),");
        }
        //trim last comma
        multilineIndicatorString = multilineIndicatorString.substring(0, multilineIndicatorString.length() - 1);
        multilineLriString = multilineLriString.substring(0, multilineLriString.length() - 1);
        multilineAuditString = multilineAuditString.substring(0, multilineAuditString.length() - 1);

        Map<String, String> mapOfMultiLineStrings = new HashMap<>();
        mapOfMultiLineStrings.put("multilineIndicatorString", multilineIndicatorString);
        mapOfMultiLineStrings.put("multilineLriString", multilineLriString);
        mapOfMultiLineStrings.put("multilineAuditString", multilineAuditString);

        return mapOfMultiLineStrings;
    }
}