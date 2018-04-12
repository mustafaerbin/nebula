package com.tr.nebula.persistence.api.criteria;


public class JoinRelation {
    private final CriteriaJoin joinedCriteria;
    private final String joinedField;
    private final CriteriaParent relationCriteria;
    private final String relationField;

    public JoinRelation(CriteriaJoin joinedCriteria, String joinedField, CriteriaParent relationCriteria, String relationField) {
        this.joinedCriteria = joinedCriteria;
        this.joinedField = joinedField;
        this.relationCriteria = relationCriteria;
        this.relationField = relationField;
    }

    public CriteriaJoin getJoinedCriteria() {
        return joinedCriteria;
    }

    public String getJoinedField() {
        return joinedField;
    }

    public CriteriaParent getRelationCriteria() {
        return relationCriteria;
    }

    public String getRelationField() {
        return relationField;
    }
}
