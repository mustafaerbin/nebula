package com.tr.nebula.persistence.api.criteria.criterion;


public class Restriction {
    /**
     *
     */
    private final Operator operator;
    /**
     *
     */
    private final Object value;
    /**
     *
     */
    private final String name;
    /**
     *
     */
    private String valueAlias;

    /**
     *
     * @param operator
     * @param name
     */
    public Restriction(Operator operator, String name) {
        this(operator, name, null);
    }

    /**
     *
     * @param operator
     * @param name
     * @param value
     */
    public Restriction(Operator operator, String name, Object value) {
        this.operator = operator;
        this.name = name;
        this.value = value;
    }

    /**
     *
     * @return
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     *
     * @param valueAlias
     */
    public void setValueAlias(String valueAlias) {
        this.valueAlias = valueAlias;
    }

    /**
     *
     * @return
     */
    public String getValueAlias() {
        return valueAlias;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Restriction{" +
                "operator=" + operator +
                ", value=" + value +
                ", name='" + name + '\'' +
                ", valueAlias='" + valueAlias + '\'' +
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
        if (!(o instanceof Restriction)) return false;

        Restriction that = (Restriction) o;

        if (getOperator() != that.getOperator()) return false;
        if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getValueAlias() != null ? getValueAlias().equals(that.getValueAlias()) : that.getValueAlias() == null;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = getOperator() != null ? getOperator().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getValueAlias() != null ? getValueAlias().hashCode() : 0);
        return result;
    }
}
