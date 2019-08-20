package com.lessons.services;

import com.lessons.models.IndicatorDTO;
import com.lessons.models.ShortReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorService {

    private static final Logger logger = LoggerFactory.getLogger(IndicatorService.class);

    @Resource
    private DataSource dataSource;

    public Integer countIndicators() {
        logger.debug("countIndicators() started.");

        String sql = "select count(*) as count from indicators";
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
//        Integer res = jt.queryForObject(sql, Integer.class);
//        return res;

        Integer counts = 0;
        SqlRowSet rs = jt.queryForRowSet(sql);
        if(rs.next())
        {
            counts = rs.getInt("count");
        }
        return counts;
    }

    public List<IndicatorDTO> getAllIndicatorsService() {
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(IndicatorDTO.class);
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        String sql = "select id, value, created_date from indicators";
        List<IndicatorDTO> allIndicators = jt.query(sql, rowMapper);
        return allIndicators;
    }
}
