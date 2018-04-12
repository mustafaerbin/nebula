package com.tr.nebula.persistence.jpa.util;


import com.tr.nebula.persistence.api.customutil.CriteriaOperator;
import com.tr.nebula.persistence.api.customutil.QueryCriteria;

import javax.persistence.criteria.*;
import java.util.Date;

/**
 * Created by  Mustafa Erbin on 27.02.2017.
 */
public class PredGenerator {

    QueryCriteria queryCriteria;

    private Root from;

    private Join join;

    private CriteriaBuilder builder;

    private Expression expression;

    public PredGenerator() {

    }

    public PredGenerator(QueryCriteria queryCriteria, Root from, Join join, CriteriaBuilder builder) {
        this.queryCriteria = queryCriteria;
        this.from = from;
        this.join = join;
        this.builder = builder;

    }

    /**
     * @return expression
     */
    private Expression getExpression() {
        expression = join != null ? join.get(getField()) : from.get(getField());
        return expression;
    }

    /**
     * @return predicate object
     */
    public Predicate getPredicate() {
        Predicate pred = null;
        switch (queryCriteria.getOperator()) {
            case EQ://Equal =
                pred = builder.equal(getExpression(), getValue());
                break;
            case NE://Not Equal <>
                pred = builder.notEqual(getExpression(), getValue());
                break;
            case LIKE://Like %%
                pred = builder.like(getExpression(), "%" +getStringValue() + "%");
                break;
            case IN://in
                pred = getExpression().in(getValue());
                break;
            case NOT_IN://Not in
                //TODO
                break;
            case BT://Between
                //TODO
                break;
            case NULL://isNull
                pred = getExpression().isNull();
                break;
            case NOT_NULL://isNotNull
                pred = getExpression().isNotNull();
                break;
            case GT://greaterThan >
                pred = generateGreaterThanCriteria();
                break;
            case GE://greaterThanOrEqualTo >=
                pred = generateGreaterThanOrEqualToCriteria();
                break;
            case LT://lessThan <
                pred = generateLessThanCriteria();
                break;
            case LE://lessThanOrEqualTo <=
                pred = generateLessThanOrEqualToCriteria();
                break;
            default:
                break;
        }

        return pred;
    }

    public CriteriaBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(CriteriaBuilder builder) {
        this.builder = builder;
    }

    public CriteriaOperator getOperator() {
        return queryCriteria.getOperator();
    }

    public String getField() {
        return queryCriteria.getField();
    }

    public Object getValue() {
        return queryCriteria.getValue();
    }


    public Root getFrom() {
        return from;
    }

    public void setFrom(Root from) {
        this.from = from;
    }

    public Join getJoin() {
        return join;
    }

    public void setJoin(Join join) {
        this.join = join;
    }

    public String getType() {
        return queryCriteria.getType();
    }


    private Date getDateValue() {
        return (Date) queryCriteria.getValue();
    }

    private Integer getNumericValue() {
        return (Integer) queryCriteria.getValue();
    }

    private String getStringValue() {
        return queryCriteria.getValue().toString();
    }

    /**
     * @return lessThanCriteria predicate object
     */
    private Predicate generateLessThanCriteria() {
        Predicate pred = null;
        if (getType().equals("date")) {
            Expression<Date> expressionDate = getExpression();
            pred = getBuilder().lessThan(expressionDate, getDateValue());
        } else if (getType().equals("int")) {
            Expression<Integer> expressionInteger = getExpression();
            pred = getBuilder().lessThan(expressionInteger, getNumericValue());
        } else {
            Expression<String> expressionString = getExpression();
            pred = getBuilder().lessThan(expressionString, getStringValue());
        }
        return pred;
    }

    /**
     * @return lessThanOrEqualToCriteria predicate object
     */
    private Predicate generateLessThanOrEqualToCriteria() {
        Predicate pred = null;
        if (getType().equals("date")) {
            Expression<Date> expressionDate = getExpression();
            pred = getBuilder().lessThanOrEqualTo(expressionDate, getDateValue());
        } else if (getType().equals("int")) {
            Expression<Integer> expressionInteger = getExpression();
            pred = getBuilder().lessThanOrEqualTo(expressionInteger, getNumericValue());
        } else {
            Expression<String> expressionString = getExpression();
            pred = getBuilder().lessThanOrEqualTo(expressionString, getStringValue());
        }
        return pred;
    }

    /**
     * @return greaterThanCriteria predicate object
     */
    private Predicate generateGreaterThanCriteria() {
        Predicate pred = null;
        if (getType().equals("date")) {
            Expression<Date> expressionDate = getExpression();
            pred = getBuilder().greaterThan(expressionDate, getDateValue());
        } else if (getType().equals("int")) {
            Expression<Integer> expressionInteger = getExpression();
            pred = getBuilder().greaterThan(expressionInteger, getNumericValue());
        } else {
            Expression<String> expressionString = getExpression();
            pred = getBuilder().greaterThan(expressionString, getStringValue());
        }
        return pred;
    }

    /**
     * @return greaterThanOrEqualToCriteria predicate object
     */
    private Predicate generateGreaterThanOrEqualToCriteria() {
        Predicate pred = null;
        if (getType().equals("date")) {
            Expression<Date> expressionDate = getExpression();
            pred = getBuilder().greaterThanOrEqualTo(expressionDate, getDateValue());
        } else if (getType().equals("int")) {
            Expression<Integer> expressionInteger = getExpression();
            pred = getBuilder().greaterThanOrEqualTo(expressionInteger, getNumericValue());
        } else {
            Expression<String> expressionString = getExpression();
            pred = getBuilder().greaterThanOrEqualTo(expressionString, getStringValue());
        }
        return pred;
    }
}
