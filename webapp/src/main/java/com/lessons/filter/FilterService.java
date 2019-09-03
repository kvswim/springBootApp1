package com.lessons.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * FilterService
 *
 * Used to convert the list of filter strings into a SQL where clause
 *
 * The passed-in list Of String can have any of these formats:
 *    Fieldname <separator> EQUALS          value
 *    Fieldname <separator> GREATER         value
 *    Fieldname <separator> GREATER_EQUAL   value
 *    Fieldname <separator> LESS            value
 *    Fieldname <separator> LESS_EQUAL      value
 *    Fieldname <separator> BETWEEN         value1 <separator> value2                        (applies to dates and numeric fields only)
 *    Fieldname <separator> IN              value1 <separator> value2 <separator> value3....
 *    Fieldname <separator> NOTIN           value1 <separator> value2 <separator> value3....
 *    Fieldname <separator> CONTAINS        value1
 *    Fieldname <separator> ICONTAINS       value1
 *    Fieldname <separator> ISNULL
 *    Fieldname <separator> ISNOTNULL
 *
 */


@Service("com.lessons.filter.FilterService")
public class FilterService {
    private static final Logger logger = LoggerFactory.getLogger(FilterService.class);

    public static final String FILTER_SEPARATOR = "~";

    @PostConstruct
    public void init() {
        logger.debug("init() started.");

    }

    /**
     * Returns false if the passed-in list of filters is invalid in anyway
     */
    public boolean areFiltersValid(List<String> aFilters) {
        logger.debug("areFiltersValid() started.");
        if(aFilters == null) {
            //throw new RuntimeException("aFilters was null!");
            logger.debug("aFilters was null, continuing...");
            return true;
        }
        // Are the filter operation strings valid -- e.g., are they either BETWEEN, IN, CONTIANS, ICONTAINS, ISNULL, or ISNOTNULL
        // Are filters in the correct format -- e.g., BETWEEN requires 2 values
        // Do the field names correspond to database columns?
        for (String filter : aFilters)
        {
            String[] parts = StringUtils.split(filter, "~");
            String operation = parts[1];
            FilterOperation br = FilterOperation.getOperation(operation);
            if (br == null) {
                logger.warn("The passed in operator was not valid.");
                return false;
            }

            int expectedTokenCount = br.getTokenCount();
            int actualTokenCount = parts.length;
            String tokenComparison = br.getCompareOperation(); //will only be == or >=

            if (tokenComparison.equalsIgnoreCase("==")) {
                if(expectedTokenCount != actualTokenCount) {
                    logger.warn("Expected an exact match of tokens, was not.");
                    return false;
                }
            }
            if(tokenComparison.equalsIgnoreCase(">=")) {
                if( !(actualTokenCount >= expectedTokenCount) )
                {
                    logger.warn("Expected tokens to be greater than or equal to, was not.");
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Return a SqlDetails that holds the SQL and the bind variables
     *
     * Approach:
     *  1) Loop through each fliter string
     *     a) Split-up the string by the separator character
     *     b) Get the operation string
     *     c) Pull the fields out
     *     d) Add the field values to the map of bind variables
     *     e) Append to the SQL string
     *  2) Create a FilterParams object and store the map of bind variables and SQL string in it
     *  3) Return the FilterParams object
     *
     * @param aFilters holds a list of Strings that hold filter operations
     * @return FilterParams object that holds the SQL partial where clause and a map of parameters
     */
    public FilterParams getFilterParamsForFilters(List<String> aFilters) {
        logger.debug("getFilterParamsForFilters() started.");
        String sqlWhereClause = "";
        int varNumber = 1;
        Map<String, Object> mapOfBindVariables = new HashMap<>();
        // Loop through the list of Strings
        for(String filter : aFilters) {
            List<String> parts = Arrays.asList(StringUtils.split(filter, "~"));

            //String[] parts = StringUtils.split(filter, "~");
            String operation = parts.get(1);
            String columnName = parts.get(0);
            FilterOperation br = FilterOperation.getOperation(operation);
            String brFilterOperation = br.getFilterOperation();
            String brSqlFormat = br.getSqlFormat();


            String bindVariableName = "bindVariable" + varNumber;
            String sqlWhereFragment = String.format(brSqlFormat, columnName, bindVariableName);
            //sqlWhereClause = sqlWhereClause + columnName + "=" + ":" + bindVariableName + " AND ";
            sqlWhereClause = sqlWhereClause + sqlWhereFragment + " AND ";
            String value = parts.get(2);
            mapOfBindVariables.put(bindVariableName, value);

//            if (brFilterOperation.equalsIgnoreCase("equals")) //EQUALS
//            {
//                String bindVariableName = "bindVariable" + varNumber;
//                sqlWhereClause = sqlWhereClause + columnName + "=" + ":" + bindVariableName + " AND ";
//                String value = parts.get(2);
//                //since a bind variable is used we need to specify the type of object in map
//                //equals will always be (integer)
//                mapOfBindVariables.put(bindVariableName, Integer.valueOf(value));
//            }
//            else if (brFilterOperation.equalsIgnoreCase("greater")) //GREATER
//            {
//                String bindVariableName = "bindVariable" + varNumber;
//                sqlWhereClause = sqlWhereClause + columnName + ">" + ":" + bindVariableName + " AND ";
//                String value = parts.get(2);
//                mapOfBindVariables.put(bindVariableName, Integer.valueOf(value));
//            }
//            else if (brFilterOperation.equalsIgnoreCase("greater_equal")) //GREATER_EQUAL
//            {
//                String bindVariableName = "bindVariable" + varNumber;
//                sqlWhereClause = sqlWhereClause + columnName + ">=" + ":" + bindVariableName + " AND ";
//                String value = parts.get(2);
//                mapOfBindVariables.put(bindVariableName, Integer.valueOf(value));
//            }
//            else if (brFilterOperation.equalsIgnoreCase("less")) //LESS
//            {
//                String bindVariableName = "bindVariable" + varNumber;
//                sqlWhereClause = sqlWhereClause + columnName + "<" + ":" + bindVariableName + " AND ";
//                String value = parts.get(2);
//                mapOfBindVariables.put(bindVariableName, Integer.valueOf(value));
//            }
//            else if (brFilterOperation.equalsIgnoreCase("less_equal")) //LESS_EQUAL
//            {
//                String bindVariableName = "bindVariable" + varNumber;
//                sqlWhereClause = sqlWhereClause + columnName + ">=" + ":" + bindVariableName + " AND ";
//                String value = parts.get(2);
//                mapOfBindVariables.put(bindVariableName, Integer.valueOf(value));
//            }
//            else if (brFilterOperation.equalsIgnoreCase("between")) //BETWEEN
//            {
//
//            }
//            else if (brFilterOperation.equalsIgnoreCase("in")) //IN
//            {
//
//            }
//            else if (brFilterOperation.equalsIgnoreCase("notin")) //NOTIN
//            {
//
//            }
//            else if (brFilterOperation.equalsIgnoreCase("contains")) //CONTAINS
//            {
//
//            }
//            else if (brFilterOperation.equalsIgnoreCase("icontains")) //ICONTAINS
//            {
//
//            }
//            else if (brFilterOperation.equalsIgnoreCase("isnull")) //ISNULL
//            {
//
//            }
//            else if (brFilterOperation.equalsIgnoreCase("isnotnull")) //ISNOTNULL
//            {
//
//            }
            //Different clauses can take differing number of bind variables
            varNumber = varNumber + 2;
        }
        if(!sqlWhereClause.isEmpty()) {
            sqlWhereClause = StringUtils.substring(sqlWhereClause, 0, (sqlWhereClause.length() - 4));
        }
        FilterParams filterParams = new FilterParams();
        filterParams.setSqlWhereClause(sqlWhereClause);
        filterParams.setSqlParams(mapOfBindVariables);
        return filterParams;
    }

    /**
     * Convert the passed-in list of Strings into an Order By clause
     *
     * @param aSortFields holds a list of strings in the formt of
     *                          "field>"  --> ORDER BY field ASC
     *                          "field<"  --> ORDER BY field DESC
     * @return
     */
    public String getSqlOrderByClauseForSortFields(List<String> aSortFields) {
        logger.debug("getSqlOrderByClauseForSortFields() started.");
        return null;
    }
}