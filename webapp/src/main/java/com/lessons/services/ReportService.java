package com.lessons.services;

import com.lessons.controllers.DashboardController;
import com.lessons.models.ReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
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
}


