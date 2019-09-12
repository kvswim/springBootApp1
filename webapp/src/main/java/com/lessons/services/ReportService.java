package com.lessons.services;

import com.lessons.filter.FilterParams;
import com.lessons.filter.FilterService;
import com.lessons.models.ReportsStatsDTO;
import com.lessons.models.ShortReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Resource
    private DataSource dataSource;

    @Resource
    private DashboardDao dashboardDao;

    @Resource
    private FilterService filterService;

    @Value("${development.mode}")
    private Boolean developmentMode;

    @Value("${network.name}")
    private String networkName;

    public ReportService()
    {
        logger.debug("Starting constructor. The value of developmentMode is: {}", developmentMode);
        logger.debug("Starting constructor. The value of network is: {}", networkName);
    }

    @PostConstruct
    public void reportServicePostConstruct()
    {
        logger.debug("Inside postconstructor, the value of developmentMode is: {}", developmentMode);
        logger.debug("Inside postconstructor, the value of network is: {}", networkName);

        if(!(networkName.equalsIgnoreCase("NIPR") || networkName.equalsIgnoreCase("SIPR")))
        {
            throw new RuntimeException("The networkName was invalid, was: " + networkName);
        }
    }


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

    public void approveReport(final Integer id, final Boolean approved)
    {
        TransactionTemplate tt = new TransactionTemplate();
        tt.setTransactionManager(new DataSourceTransactionManager(this.dataSource));

        // This transaction will throw a TransactionTimedOutException after 60 seconds (causing the transaction to rollback)
        tt.setTimeout(60);

        tt.execute(new TransactionCallbackWithoutResult()
        {
            protected void doInTransactionWithoutResult(TransactionStatus aStatus)
            {
                Map<String, Object> inputParams = new HashMap<>();
                inputParams.put("id", id);
                inputParams.put("reviewed", approved);

                // update record
                String sql = "update reports set reviewed = :reviewed where id = :id returning *";
                NamedParameterJdbcTemplate np = new NamedParameterJdbcTemplate(dataSource);
                Map<String, Object> updatedRecord = np.queryForMap(sql, inputParams);

                //set audit
                int transactionID = dashboardDao.getNextValue();
                sql = "insert into reports_aud (rev, rev_type, id, version, description, display_name, reviewed)" +
                        "values (:rev, :rev_type, :id, :version, :description, :display_name, :reviewed)";
                inputParams.clear();
                inputParams.put("rev", transactionID);
                inputParams.put("rev_type", 1);
                inputParams.put("id", updatedRecord.get("id"));
                inputParams.put("version", updatedRecord.get("version"));
                inputParams.put("description", updatedRecord.get("description"));
                inputParams.put("display_name", updatedRecord.get("display_name"));
                inputParams.put("reviewed", updatedRecord.get("reviewed"));
                np.update(sql, inputParams);
            }
        });
        logger.debug("Transaction finished.");
    }

    public boolean verifyReportExists(Integer id)
    {
       String sql = "select * from reports where id = ?";
       JdbcTemplate jt = new JdbcTemplate(this.dataSource);
       SqlRowSet rs = jt.queryForRowSet(sql, id);
       return rs.next();
    }

    public void deleteReportService(final Integer reportId)
    {
        TransactionTemplate tt = new TransactionTemplate();
        tt.setTransactionManager(new DataSourceTransactionManager(this.dataSource));
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                //delete from reports
                JdbcTemplate jt = new JdbcTemplate(dataSource);
                String sql = "delete from reports where id = ?";
                jt.update(sql, reportId);

                //add audit record to reports_aud
                sql = "insert into reports_aud (rev, rev_type, id)"
                        + "values (:rev, :rev_type, :id)";
                Map<String, Object> inputParams = new HashMap<>();
                inputParams.put("rev", dashboardDao.getNextValue()); //get next value in seq as txn id
                inputParams.put("rev_type", 2); // 2=delete
                inputParams.put("id", reportId);
                NamedParameterJdbcTemplate np = new NamedParameterJdbcTemplate(dataSource);
                np.update(sql, inputParams);
            }
        });
    }

    public List<ShortReportDTO> getAllShortReports() {
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        List<ShortReportDTO> resultingListofShortReports = new ArrayList<>();

        String sql = "select * from reports";
        SqlRowSet rs = jt.queryForRowSet(sql);

        while(rs.next())
        {
            ShortReportDTO thisDTO = new ShortReportDTO();
            thisDTO.setDescription(rs.getString("description"));
            thisDTO.setDisplayName(rs.getString("display_name"));
            thisDTO.setId(rs.getInt("id"));
            resultingListofShortReports.add(thisDTO);
        }
        return resultingListofShortReports;
    }

    public List<ShortReportDTO> getAllShortReports2() {
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(ShortReportDTO.class);
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        String sql = "select id, display_name, description from reports";
        List<ShortReportDTO> resultingListofShortReports = jt.query(sql, rowMapper);
        return resultingListofShortReports;
    }

    //case 1: no whereClause
    //case 2: whereClause present.
    public List<ShortReportDTO> getFilteredReports(List<String> filters) {
        List<ShortReportDTO> resultingListofShortReports = new ArrayList<>();
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(ShortReportDTO.class);
        FilterParams fp = filterService.getFilterParamsForFilters(filters);
        String whereClause = fp.getSqlWhereClause();

        String sql = "select id, display_name, description from reports";
        if ( !whereClause.isEmpty() ) {
            NamedParameterJdbcTemplate np = new NamedParameterJdbcTemplate(this.dataSource);
            sql = sql + " where " + whereClause;
            resultingListofShortReports  = np.query(sql, fp.getSqlParams(), rowMapper);
        } else {
            JdbcTemplate jt = new JdbcTemplate(this.dataSource);
            resultingListofShortReports = jt.query(sql, rowMapper);
        }

        return resultingListofShortReports;
    }

    public List<ReportsStatsDTO> getReportsStats() {
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        List<ReportsStatsDTO> reportStats = new ArrayList<>();
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(ReportsStatsDTO.class);
        String sql = "select lri.report, r.display_name, count(lri.indicator) as indicator_count \n" +
                "from link_reports_indicators lri\n" +
                "    join reports r on (r.id = lri.report)\n" +
                "group by lri.report, r.display_name\n" +
                "order by 3 desc\n" +
                "limit 5";
        reportStats = jt.query(sql,rowMapper);
        return reportStats;
    }
}


