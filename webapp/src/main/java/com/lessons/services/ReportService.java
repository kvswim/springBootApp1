package com.lessons.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Resource
    private DataSource dataSource;

    public Map<String, Object> getReport(int reportId)
    {
        String sql = "select * from reports where id = ?";
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        Map<String, Object> result = jt.queryForMap(sql, reportId);
        return result;
    }

    public List<Map<String, Object>> getAllReports() {
        String sql = "select id, description, created_date from reports";
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        List<Map<String, Object>> result = jt.queryForList(sql);
        return result;
    }
}


