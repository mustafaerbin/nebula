package com.tr.nebula.persistence.api.criteria.projection;


/**
 *
 */
public class FunctionProjection implements Projection {
    /**
     *
     */
    private final String property;
    /**
     *
     */
    private final Type fnType;

    /**
     *
     * @param property
     * @param fnType
     */
    public FunctionProjection(String property, Type fnType) {
        this.property = property;
        this.fnType = fnType;
    }

    /**
     *
     * @return
     */
    public String getProperty() {
        return property;
    }

    /**
     *
     * @return
     */
    public Type getFnType() {
        return fnType;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isGrouped() {
        return false;
    }

    /**
     *
     */
    public enum Type {
        COUNT, MIN, MAX , AVG, SUM
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "FunctionProjection{" +
                "property='" + property + '\'' +
                ", fnType=" + fnType +
                '}';
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionProjection)) return false;

        FunctionProjection that = (FunctionProjection) o;

        if (getProperty() != null ? !getProperty().equals(that.getProperty()) : that.getProperty() != null)
            return false;
        return getFnType() == that.getFnType();
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = getProperty() != null ? getProperty().hashCode() : 0;
        result = 31 * result + (getFnType() != null ? getFnType().hashCode() : 0);
        return result;
    }
}
