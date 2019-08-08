package com.lessons.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

@Service
public class DashboardDao
{
    private static final Logger logger = LoggerFactory.getLogger(DashboardDao.class);

    @Resource
    private DataSource dataSource;

    public DashboardDao()
    {
        logger.debug("DashboardDao() Constructor called");
    }

    @PostConstruct
    public void dashboardDaoPostConstructor()
    {

        logger.debug("dashboardDaoPostConstructor() called");
    }


    public String getDatabaseTime()
    {
        logger.debug("getDatabaseTime() started.");

        // Run a SQL query to get the current date time
        String sql = "Select NOW()";
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        String sDateTime = jt.queryForObject(sql, String.class);
        logger.debug("Database Time is {}", sDateTime);

        return sDateTime;
    }

    public int getNextValue()
    {
        logger.debug("getNextValue() started.");

        String sql = "select nextval('seq_table_ids')";
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        int result = jt.queryForObject(sql, Integer.class);

        logger.debug("getNextValue() returns {}", result);

        return result;
    }

    public void addNewRecord(String desc)
    {
        logger.debug("addNewRecord started.");

        Integer priority = 9001;

        String sql="insert into reports(id, description, priority)\n" +
                "values(nextval('seq_table_ids'), ? , ?)";
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        jt.update(sql, desc, priority); //execute sql but don't give anything back

        logger.debug("addNewRecord finished.");
    }
}