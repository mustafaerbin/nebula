package com.tr.nebula.persistence.api.criteria.projection;

/**
 *
 */
public class ProjectionElements implements Projection {
    /**
     *
     */
    private final String property;

    /**
     *
     * @param property
     */
    public ProjectionElements(String property) {
        this.property = property;
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
    @Override
    public boolean isGrouped() {
        return false;
    }
}
