package com.lessons.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public enum FilterOperation {

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

    EQUALS("EQUALS", "=", 3),
    GREATER("GREATER", ">", 3),
    GREATER_EQUAL("GREATER_EQUAL", ">=", 3),
    LESS("LESS", "<", 3),
    LESS_EQUAL("LESS_EQUAL", "<=", 3),
    BETWEEN("BETWEEN", "==", 4),
    IN("IN", "", 3),
    NOTIN("NOTIN", "", 5),
    CONTAINS("CONTAINS", "", 3),
    ICONTAINS("ICONTAINS", "", 3),
    ISNULL("ISNULL", "=", 2),
    ISNOTNULL("ISNOTNULL", "", 2);


    private static final Logger logger = LoggerFactory.getLogger(FilterOperation.class);

    private int tokenCount;
    private String compareOperation;
    private String filterOperation;

    public int getTokenCount() {
        return tokenCount;
    }

    public String getCompareOperation() {
        return compareOperation;
    }

    public String getFilterOperation() {
        return filterOperation;
    }

    private FilterOperation(String filterOperation, String compareOperation, int tokenCount) {
        this.filterOperation = filterOperation;
        this.compareOperation = compareOperation;
        this.tokenCount = tokenCount;
    }

    public FilterOperation getOperation(String operationName) {
        List<FilterOperation> allOperations = Arrays.asList(FilterOperation.values());
        for (int i=0; i< allOperations.size(); ++i) {
            if(allOperations.get(i).getFilterOperation() == operationName)
            {
                return allOperations.get(i);
            }
        }
        return null;
    }
}
