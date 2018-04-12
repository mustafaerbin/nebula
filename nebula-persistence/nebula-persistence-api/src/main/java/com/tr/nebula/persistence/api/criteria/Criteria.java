package com.tr.nebula.persistence.api.criteria;

import com.tr.nebula.common.lang.TypeReference;
import com.tr.nebula.persistence.api.criteria.criterion.Restriction;
import com.tr.nebula.persistence.api.criteria.projection.Projection;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Criteria<E> extends CriteriaParent<E> {
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {};
    public static final  Class<Map<String, Object>> MAP_CLASS = MAP_TYPE_REFERENCE.getClazz();
    /**
     *
     */
    private List<Order> orders = new LinkedList<>();
    /**
     * Starting index for the paged fetches.
     */
    private Integer offset;
    /**
     * Maximum number of results per page.
     */
    private Integer limit;
    /**
     * @param entityClass
     */
    protected Criteria(Class<?> entityClass, Transformer<E> transformer) {
        this(null, entityClass, transformer);
    }

    /**
     * @param entityClass
     */
    protected Criteria(String alias, Class<?> entityClass, Transformer<E> transformer) {
        super(alias, entityClass, transformer, new LinkedHashMap<String, Integer>());
    }

    /**
     *
     * creates {@link CriteriaParent}
     * @param entityClass
     * @return
     */
    public static <E> Criteria<E> createCriteria(Class<?> entityClass, Transformer<E> transformer) {
        return new Criteria<>(entityClass, transformer);
    }

    /**
     *
     * creates {@link CriteriaParent}
     * @param entityClass
     * @return
     */
    public static <E> Criteria<E> createCriteria(String alias, Class<?> entityClass, Transformer<E> transformer) {
        return new Criteria<>(alias, entityClass, transformer);
    }
    /**
     * Sets starting index for the paged fetches.
     * @param offset
     */
    public Criteria<E> setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Sets maximum number of results per page.
     * @param limit
     */
    public Criteria<E> setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Gets starting index for the paged fetches.
     * @return
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * Gets maximum number of results per page.
     * @return
     */
    public Integer getLimit() {
        return limit;
    }

    public List<E> list(){
        assertTransformerIsExist();
        return getTransformer().list(this);
    }

    public Result<E> pairList(){
        assertTransformerIsExist();
        return getTransformer().pairList(this);
    }

    public Long count(){
        assertTransformerIsExist();
        return getTransformer().count(this);
    }


    public Object uniqueResult(){
        assertTransformerIsExist();
        return getTransformer().uniqueResult(this);
    }

    @Override
    public Criteria<E> add(Restriction criterion) {
        super.add(criterion);
        return this;
    }

    @Override
    public Criteria<E> addOrder(Order order) {
        if(order.getCriteriaAlias() == null) {
            order.setCriteriaAlias(this.getAlias());
        }
        orders.add(order);
        return this;
    }

    @Override
    public Criteria<E> setProjection(Projection projection) {
        super.setProjection(projection);
        return this;
    }

    private void assertTransformerIsExist(){
        if(getTransformer() == null) {
            throw new RuntimeException("Transformer not provided for CriteriaParent");
        }
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public boolean isRoot() {
        return true;
    }
}
