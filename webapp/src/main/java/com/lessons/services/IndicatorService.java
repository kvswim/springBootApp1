package com.lessons.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.annotation.Resource;

@Service
public class IndicatorService {

    private static final Logger logger = LoggerFactory.getLogger(IndicatorService.class);
    @Resource
    private DataSource dataSource;

    public Integer countIndicators() {
        logger.debug("countIndicators() started.");

        String sql = "select count(*) from indicators";
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        Integer res = jt.queryForObject(sql, Integer.class);

        return res;
    }
}
