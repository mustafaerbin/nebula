package com.tr.nebula.persistence.api.criteria.projection;

/**
 * Created by Mustafa Erbin on 16/01/2017.
 */
public class EnhancedProjection implements Projection {
    /**
     *
     */
    private final String alias;
    /**
     *
     */
    private final Projection projection;

    /**
     *
     * @param projection
     * @param alias
     */
    public EnhancedProjection(Projection projection, String alias) {
        this.projection = projection;
        this.alias = alias;
    }

    /**
     *
     * @return
     */
    public String getAlias() {
        return alias;
    }

    /**
     *
     * @return
     */
    public Projection getProjection() {
        return projection;
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
     * @return
     */
    @Override
    public String toString() {
        return "EnhancedProjection{" +
                "alias='" + alias + '\'' +
                ", projection=" + projection +
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
        if (!(o instanceof EnhancedProjection)) return false;

        EnhancedProjection that = (EnhancedProjection) o;

        if (getAlias() != null ? !getAlias().equals(that.getAlias()) : that.getAlias() != null) return false;
        return getProjection() != null ? getProjection().equals(that.getProjection()) : that.getProjection() == null;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = getAlias() != null ? getAlias().hashCode() : 0;
        result = 31 * result + (getProjection() != null ? getProjection().hashCode() : 0);
        return result;
    }
}
