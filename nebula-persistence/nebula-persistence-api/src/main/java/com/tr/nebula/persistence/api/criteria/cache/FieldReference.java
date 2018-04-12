package com.tr.nebula.persistence.api.criteria.cache;

public class FieldReference {
    /**
     *
     */
    private final Class<?> targetEntity;
    /**
     *
     */
    private String[] selects;
    /**
     *
     */
    private String[] filters;
    /**
     *
     */
    private final String identityName;
    /**
     *
     */
    private final String referenceId;

    /**
     *
     * @param targetEntity
     * @param selects
     * @param filters
     * @param identityName
     * @param referenceId
     */
    public FieldReference(Class<?> targetEntity, String[] selects, String[] filters, String identityName, String referenceId) {
        this.targetEntity = targetEntity;
        this.selects = selects;
        this.filters = filters;
        this.identityName = identityName;
        this.referenceId = referenceId;
    }

    /**
     *
     * @return
     */
    public Class<?> getTargetEntity() {
        return targetEntity;
    }

    /**
     *
     * @return
     */
    public String[] getSelects() {
        return selects;
    }

    /**
     *
     * @param selects
     */
    public void setSelects(String[] selects) {
        this.selects = selects;
    }

    /**
     *
     * @param filters
     */
    public void setFilters(String[] filters) {
        this.filters = filters;
    }

    /**
     *
     * @return
     */
    public String[] getFilters() {
        return filters;
    }

    /**
     *
     * @return
     */
    public String getIdentityName() {
        return identityName;
    }

    /**
     *
     * @return
     */
    public String getReferenceId() {
        return referenceId;
    }
}
