package com.tr.nebula.persistence.api.criteria.projection;


public class PropertyProjection implements Projection {
    /**
     *
     */
    private final String property;
    /**
     *
     */
    private final boolean groupped;

    /**
     *
     * @param property
     * @param groupped
     */
    public PropertyProjection(String property, boolean groupped) {
        this.property = property;
        this.groupped = groupped;
    }

    /**
     *
     * @return
     */
    public String getProperty() {
        return property;
    }


    @Override
    public boolean isGrouped() {
        return this.groupped;
    }

    @Override
    public String toString() {
        return "PropertyProjection{" +
                "property='" + property + '\'' +
                ", groupped=" + groupped +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertyProjection)) return false;

        PropertyProjection that = (PropertyProjection) o;

        if (groupped != that.groupped) return false;
        return getProperty() != null ? getProperty().equals(that.getProperty()) : that.getProperty() == null;
    }

    @Override
    public int hashCode() {
        int result = getProperty() != null ? getProperty().hashCode() : 0;
        result = 31 * result + (groupped ? 1 : 0);
        return result;
    }
}
