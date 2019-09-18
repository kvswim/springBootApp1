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
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)    // Required to work with JUnit 4
@SpringBootTest                 // Start up a Spring Application Context
public class AddTestRecords {
    private static final Logger logger = LoggerFactory.getLogger(AddTestRecords.class);

    @Resource
    private DataSource dataSource;

    @Test
    public void insertTestRecords() {
        int totalReportsToCreate = 50000;
        for(int i = 1; i <= totalReportsToCreate; i++) {
            createReport(i);
            createIndicators(i, getRandomNumber(1, 100));
            if((i % 100) == 0) {
                logger.debug("Processing report {} of {}", i, totalReportsToCreate);
            }
        }
    }

    private void createReport(int reportId)
    {
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

    private void createIndicators(int reportId, int totalNumberOfIndicators)
    {
        JdbcTemplate jt = new JdbcTemplate(dataSource);

        for (int i=1; i<=totalNumberOfIndicators; i++)
        {
            //TODO: MultiLineInsert
            // insert into indicators/lri/ind_aud values() (id, value, ind_type), (id, value, ind_type)...

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
        }
    }

    private Integer getUniqueID()
    {
        JdbcTemplate jt = new JdbcTemplate(dataSource);
        String sql = "select nextval('seq_table_ids')";
        Integer returnValue = jt.queryForObject(sql, Integer.class);
        return returnValue;
    }

    private int getRandomNumber(int min, int max)
    {
        int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNumber;
    }

    private String getRandomIP()
    {
        String resultingIP;
        int firstOctet = getRandomNumber(1, 255);
        int secondOctet = getRandomNumber(0, 255);
        int thirdOctet = getRandomNumber(0, 255);
        int fourthOctet = getRandomNumber(1, 255);
        resultingIP = firstOctet + "." + secondOctet + "." + thirdOctet + "." + fourthOctet;

        return resultingIP;
    }

    private boolean getRandomBoolean()
    {
        int randomNumber = getRandomNumber(1, 2);
        if (randomNumber == 1)
        {
            return true;
        } else {
            return false;
        }
    }
}
