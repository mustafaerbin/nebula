package com.tr.nebula.persistence.api.criteria.projection;


public class IdentifierProjection implements Projection {
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
     * @return
     */
    @Override
    public String toString() {
        return "IdentifierProjection{}";
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdentifierProjection)) return false;
        return true;
    }
}
