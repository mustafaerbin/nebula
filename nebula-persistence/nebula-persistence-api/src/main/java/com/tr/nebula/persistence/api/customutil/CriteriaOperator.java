package com.tr.nebula.persistence.api.customutil;

/**
 * Created by Mustafa Erbin on 20.07.2017.
 */
public enum CriteriaOperator {
    EQ, NE, BT, GT, GE, LT, LE, LIKE, NULL, NOT_NULL, IN, NOT_IN;

    @Override
    public String toString() {
        switch (this) {
            case EQ:
                return "=";
            case NE:
                return "!=";
            case GT:
                return ">";
            case GE:
                return ">=";
            case LT:
                return "<";
            case LE:
                return "<=";
            case BT:
                return "BETWEEN";
            case NOT_NULL:
                return "IS NOT NULL";
            case NULL:
                return "IS NULL";
            case IN:
                return "IN";
            case NOT_IN:
                return "NOT IN";
            case LIKE:
                return "LIKE";
            default:
                return null;
        }
    }
}
