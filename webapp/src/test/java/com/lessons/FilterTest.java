package com.lessons;

import com.lessons.filter.FilterParams;
import com.lessons.filter.FilterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)    // Required to work with JUnit 4
@SpringBootTest                 // Start up a Spring Application Context
public class FilterTest {
    private static final Logger logger = LoggerFactory.getLogger(FilterTest.class);

    @Resource
    private FilterService filterService;

    @Test
    public void filterParamTestEquals() {
        logger.debug("Starting filterParamTestEquals.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("ID~EQUALS~5");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);

        assertTrue("The output filterparam map did not have the correct size.",
                (filterParam.getSqlParams().size() == 1));

        assertTrue("The expected SQL clause was incorrect.",
                (filterParam.getSqlWhereClause().trim().equalsIgnoreCase("ID = :bindVariable1::Integer")));
    }

    @Test
    public void filterParamTestGreater() {
        logger.debug("Starting filterParamTestGreater.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("VALUE~GREATER~5");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);

        assertTrue("The output filterparam map did not have the correct size.",
                (filterParam.getSqlParams().size() == 1));

        assertTrue("The expected SQL clause was incorrect.",
                (filterParam.getSqlWhereClause().trim().equalsIgnoreCase("VALUE > :bindVariable1::Integer")));
    }

    @Test
    public void filterParamTestGreaterEqual() {
        logger.debug("Starting filterParamTestGreaterEqual.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("VALUE~GREATER_EQUAL~5");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);

        assertTrue("The output filterparam map did not have the correct size.",
                (filterParam.getSqlParams().size() == 1));

        assertTrue("The expected SQL clause was incorrect.",
                (filterParam.getSqlWhereClause().trim().equalsIgnoreCase("VALUE >= :bindVariable1::Integer")));
    }

    @Test
    public void filterParamTestLess() {
        logger.debug("Starting filterParamTestLess.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("VALUE~LESS~5");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);

        assertTrue("The output filterparam map did not have the correct size.",
                (filterParam.getSqlParams().size() == 1));

        assertTrue("The expected SQL clause was incorrect.",
                (filterParam.getSqlWhereClause().trim().equalsIgnoreCase("VALUE < :bindVariable1::Integer")));
    }

    @Test
    public void filterParamTestLessEqual() {
        logger.debug("Starting filterParamTestLessEqual.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("VALUE~LESS_EQUAL~5");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);

        assertTrue("The output filterparam map did not have the correct size.",
                (filterParam.getSqlParams().size() == 1));

        assertTrue("The expected SQL clause was incorrect.",
                (filterParam.getSqlWhereClause().trim().equalsIgnoreCase("VALUE <= :bindVariable1::Integer")));

    }

    @Test
    public void filterParamTestBetween() {
        logger.debug("Starting filterParamTestBetween.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("DATE~BETWEEN~2019-09-06-T23:00:00~2019-09-06-T23:59:59");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);

        assertTrue("The output filterparam map did not have the correct size.",
                (filterParam.getSqlParams().size() == 2));

        assertTrue("The expected SQL clause was incorrect.",
                (filterParam.getSqlWhereClause().trim().equalsIgnoreCase("DATE BETWEEN :bindVariable1::Timestamp AND :bindVariable2::Timestamp")));

    }

    //TODO add additional test case(s) testing >1 variable for IN
    @Test
    public void filterParamTestIn() {
        logger.debug("Starting filterParamTestIn.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("NAME~IN~ASDF");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);

        assertTrue("The output filterparam map did not have the correct size.",
                (filterParam.getSqlParams().size() == 1));

        assertTrue("The expected SQL clause was incorrect.",
                (filterParam.getSqlWhereClause().trim().equalsIgnoreCase("NAME IN (:bindVariable1)")));

    }

    //TODO add additional test case(s) testing >1 variable for NOTIN
    @Test
    public void filterParamTestNotIn() {
        logger.debug("Starting filterParamTestNotIn.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("NAME~NOTIN~ASDF");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);

        assertTrue("The output filterparam map did not have the correct size.",
                (filterParam.getSqlParams().size() == 1));

        assertTrue("The expected SQL clause was incorrect.",
                (filterParam.getSqlWhereClause().trim().equalsIgnoreCase("NAME NOTIN (:bindVariable1)")));
    }

    @Test
    public void filterParamTestContains() {
        logger.debug("Starting filterParamTestContains.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("NAME~CONTAINS~ASDF");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);

        assertTrue("The output filterparam map did not have the correct size.",
                (filterParam.getSqlParams().size() == 1));

        assertTrue("The expected SQL clause was incorrect.",
                (filterParam.getSqlWhereClause().trim().equalsIgnoreCase("NAME CONTAINS %:bindVariable1%")));
    }

    @Test
    public void filterParamTestIContains() {
        logger.debug("Starting filterParamTestIContains.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("NAME~ICONTAINS");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);
    }

    @Test
    public void filterParamTestIsNull() {
        logger.debug("Starting filterParamTestIsNotNull.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);
    }

    @Test
    public void filterParamTestIsNotNull() {
        logger.debug("Starting filterParamTestIsNotNull.");
        List<String> testFilterList = new ArrayList<>();
        testFilterList.add("");
        FilterParams filterParam = filterService.getFilterParamsForFilters(testFilterList);
    }
}
