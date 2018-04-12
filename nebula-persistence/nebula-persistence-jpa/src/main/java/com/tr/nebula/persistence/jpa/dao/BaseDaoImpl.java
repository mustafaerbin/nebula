package com.tr.nebula.persistence.jpa.dao;

import com.tr.nebula.persistence.api.criteria.Criteria;
import com.tr.nebula.persistence.api.criteria.Result;
import com.tr.nebula.persistence.api.customutil.CriteriaOperator;
import com.tr.nebula.persistence.api.customutil.CustomQuery;
import com.tr.nebula.persistence.api.customutil.OrderType;
import com.tr.nebula.persistence.api.customutil.Order;
import com.tr.nebula.persistence.api.customutil.QueryCriteria;
import com.tr.nebula.persistence.api.dao.BaseDao;
import com.tr.nebula.persistence.api.query.search.SearchModel;
import com.tr.nebula.persistence.jpa.util.PredGenerator;
import org.hibernate.jpa.criteria.OrderImpl;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic Dao Class which limits {@link BaseDao} to take
 *
 * @param <T> Type of the entity parameter.
 */
@Transactional
public class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    private Map<String, Join> joinMap;

    protected final Class<T> entityClass;


    public BaseDaoImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public T create(Object entity) {
        T entityObject = (T) entity;
        entityManager.persist(entityObject);
        return entityObject;
    }

    @Override
    public T update(Object entity) {
        T entityObject = (T) entity;
        return entityManager.merge(entityObject);
    }

    @Override
    public T delete(Serializable id) {
        T entityObject = (T) findById(id);
        entityManager.remove(entityObject);
        return entityObject;
    }

    @Override
    public T findById(Serializable id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public Criteria queryAllStrict(SearchModel search) {
        return null;
    }

    @Override
    public Criteria<Map<String, Object>> queryAll(SearchModel search) {
        return null;
    }

    @Override
    public Criteria queryAll(SearchModel search, Class transformClass) {
        return null;
    }

    @Override
    public Result findAllStrict(SearchModel search) {
        Result result = new Result();
        Map<String, Object> resultMap = getResults(convertSearchModelToQuery(search));
        result.setList((List) resultMap.get("data"));
        result.setTotalCount(Long.valueOf((Long) resultMap.get("count")));
        return result;
    }

    @Override
    public Result<Map<String, Object>> findAll(SearchModel search) {
        return null;
    }

    @Override
    public Result findAll(SearchModel search, Class transformClass) {
        return findAllStrict(search);
    }

    @Override
    public List findAll() {
        return (List) getResults(new CustomQuery()).get("data");
    }


    public <E extends Serializable> E findById(Class<E> clazz, Serializable oid) {
        return entityManager.find(clazz, oid);
    }

    @Override
    public T delete(T entity) {
        entityManager.remove(entity);
        return entity;
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public T merge(T entity) {
        return update(entity);
    }

    @Override
    public T detach(T entity) {
        entityManager.detach(entity);
        return entity;
    }

    @Override
    public List getProperties() {
        return getProperties(this.entityClass);
    }

    @Override
    public List getProperties(Class propertyClass) {
        Metamodel meta = entityManager.getMetamodel();
        EntityType<T> entityType = meta.entity(propertyClass);

        // Check whether @Table annotation is present on the class.
        Table t = propertyClass.getClass().getAnnotation(Table.class); //typedClass.getAnnotation(Table.class);
        String tableName = (t == null) ? entityType.getName().toUpperCase() : t.name();
        //List<PropertyObj> propertyList = new ArrayList<PropertyObj>();
        List propertyList = new ArrayList<>();
        for (Attribute<? super T, ?> a : entityType.getAttributes()) {
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put("field", a.getName());
            propertyMap.put("type", a.getJavaType().getName());
            propertyList.add(propertyMap);
        }
        return propertyList;
    }

    @Override
    public Result findByPropertiesAsResult(Map propertyMap) {
        List<QueryCriteria> queryCriterias = new ArrayList<>();
        QueryCriteria queryCriteria;
        for (int i = 0; i < propertyMap.keySet().size(); i++) {
            queryCriteria = new QueryCriteria();
            String key = propertyMap.keySet().toArray()[i].toString();
            Object value = propertyMap.get(key);
            queryCriteria.setField(key);
            queryCriteria.setValue(value);
            queryCriteria.setOperator(CriteriaOperator.EQ);
            queryCriterias.add(queryCriteria);
        }

        CustomQuery query = new CustomQuery();
        query.setCriteria(queryCriterias);

        Map<String, Object> resultMap = getResults(query);
        Result result = new Result();
        result.setTotalCount((Long) resultMap.get("count"));
        result.setList((List) resultMap.get("data"));

        return result;
    }

    @Override
    public List findByProperties(Map propertyMap) {
        return findByPropertiesAsResult(propertyMap).getList();
    }

    @Override
    public void truncate(String tableName) {
        entityManager.createNativeQuery("truncate table " + tableName).executeUpdate();
    }

    public List findAll(CustomQuery query) {
        return (List) getResults(query).get("data");
    }

    public Map getResults(CustomQuery queryObj) {
        clearJoin();
        Map<String, Object> retMap = new HashMap<>();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = queryObj.getSelectionStrings() != null ? builder.createTupleQuery() : builder.createQuery(entityClass);
        Root<T> from = criteriaQuery.from(entityClass);

        if (queryObj.getSelectionStrings() != null) {
            criteriaQuery.multiselect(getSelections(queryObj.getSelectionStrings(), from));
        }

        Predicate predicate = getCriteria(queryObj, from, builder);

        if (predicate != null) {
            criteriaQuery.where(predicate);
        }

        if (queryObj.getOrderList() != null && queryObj.getOrderList().size() > 0) {
            criteriaQuery = addOrder(criteriaQuery, from, queryObj.getOrderList());
        }

        retMap.put("count", count(queryObj, builder));

        TypedQuery typedQuery = entityManager.createQuery(criteriaQuery);
        if (queryObj.getOffset() > 0) {
            typedQuery.setFirstResult(queryObj.getOffset());
        }
        if (queryObj.getMaxResults() > 0) {
            typedQuery.setMaxResults(queryObj.getMaxResults());
        }
        List retList;
        if (queryObj.getSelectionStrings() != null) {
            retList = parseSelectionsAndAddList(typedQuery, queryObj.getSelectionStrings());
        } else {
            retList = typedQuery.getResultList();
        }

        retMap.put("data", retList);

        return retMap;

    }

    private Predicate getPredicate(QueryCriteria queryCriteria, Root from, Join join, CriteriaBuilder builder) {
        PredGenerator predGenerator = new PredGenerator(queryCriteria, from, join, builder);
        return predGenerator.getPredicate();
    }

    private List<Selection<?>> getSelections(Map<String, String[]> selectionStrings, Root from) {
        List<Selection<?>> selections = new ArrayList<Selection<?>>();

        for (String key : selectionStrings.keySet()) {
            String[] selectionList = selectionStrings.get(key);
            Join<Object, Object> join = null;
            if (!key.equals("this")) {
                join = getJoin(key);
                if (join == null) {
                    join = from.join(key, JoinType.LEFT);
                    addJoin(key, join);
                }
            }

            for (String select : selectionList) {
                if (key.equals("this")) {
                    Selection<?> s = from.get(select).alias(select);
                    selections.add(s);
                } else {
                    Selection<?> s = join.get(select).alias(key + "_" + select);
                    selections.add(s);
                }
            }
        }
        return selections;
    }

    private Join getJoin(String name) {
        if (joinMap != null) {
            return joinMap.get(name);
        }
        return null;
    }

    private void addJoin(String name, Join join) {
        if (joinMap == null)
            joinMap = new HashMap<>();
        joinMap.put(name, join);
    }

    private List parseSelectionsAndAddList(TypedQuery typedQuery, Map<String, String[]> selectionMap) {
        List retList = new ArrayList();
        List<Tuple> set = typedQuery.getResultList();
        for (Tuple tuple : set) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (String key : selectionMap.keySet()) {
                if (key.equals("this")) {
                    for (String select : selectionMap.get(key)) {
                        map.put(select, tuple.get(select));
                    }
                } else {
                    Map<String, Object> innerMap = new HashMap<String, Object>();
                    for (String select : selectionMap.get(key)) {
                        innerMap.put(select, tuple.get(key + "_" + select));
                    }
                    map.put(key, innerMap);
                }
            }
            retList.add(map);
        }
        return retList;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    private void clearJoin() {
        this.joinMap = null;
    }

    private CriteriaQuery addOrder(CriteriaQuery criteriaQuery, Root from, List<Order> orders) {
        List<OrderImpl> orderImplList = new ArrayList<>();
        for (Order order : orders) {
            OrderImpl orderImpl;
            String[] key = order.getField().split("\\.");
            String field = order.getField();
            if (key.length > 1) {
                Join<Object, Object> join = null;
                for (int i = 0; i < key.length - 1; i++) {
                    join = getJoin(key[i]);
                    if (join == null) {
                        join = join != null ? join.join(key[i], JoinType.LEFT) : from.join(key[i], JoinType.LEFT);
                        addJoin(key[i], join);
                    }
                }
                field = key[key.length - 1];
                orderImpl = new OrderImpl(join.get(field), order.getType() == OrderType.ASC);
            } else {
                orderImpl = new OrderImpl(from.get(field), order.getType() == OrderType.ASC);
            }

            orderImplList.add(orderImpl);
        }

        return criteriaQuery.orderBy(orderImplList);
    }

    private CustomQuery convertSearchModelToQuery(SearchModel model) {
        CustomQuery query = new CustomQuery();
        if (model.getOffset() != null)
            query.setOffset(model.getOffset());
        if (model.getLimit() != null)
            query.setMaxResults(model.getLimit());

        if (model.getSort() != null) {
            query.setOrderList(new ArrayList<>());
            for (String sort : model.getSort()) {
                String field = sort.substring(1, sort.length());
                boolean desc = sort.charAt(0) == '-';
                Order order = new Order();
                order.setField(field);
                order.setType(desc ? OrderType.DESC : OrderType.ASC);
                query.getOrderList().add(order);
            }
        }

        if (model.getFilter() != null) {
            query.setCriteria(new ArrayList<>());
            for (String[] filter : model.getFilter()) {
                QueryCriteria criteria = new QueryCriteria();
                criteria.setField(filter[0]);
                criteria.setValue(filter[2]);
                criteria.setOperator(getCriteriaOperator(filter[1]));
                query.getCriteria().add(criteria);
            }
        }

        if (model.getFields() != null) {
            query.setSelectionStrings(new HashMap<>());
            Map<String, List<String>> tempMap = new HashMap<>();
            for (String field : model.getFields()) {
                String[] fieldArr = field.split("\\.");
                if (fieldArr.length > 1) {
                    List<String> fieldList = tempMap.get(fieldArr[0]);
                    if (fieldList == null)
                        fieldList = new ArrayList<String>();
                    fieldList.add(fieldArr[1]);
                    tempMap.put(fieldArr[0], fieldList);
                } else {
                    List<String> fieldList = tempMap.get("this");
                    if (fieldList == null)
                        fieldList = new ArrayList<String>();
                    fieldList.add(fieldArr[0]);
                    tempMap.put("this", fieldList);
                }
            }
            for (String key : tempMap.keySet()) {
                query.getSelectionStrings().put(key, tempMap.get(key).toArray(new String[tempMap.get(key).size()]));
            }
        }

        return query;
    }

    private CriteriaOperator getCriteriaOperator(String operator) {
        switch (operator) {
            case "==":
                return CriteriaOperator.EQ;
            case "~=":
                return CriteriaOperator.LIKE;
            case "!=":
                return CriteriaOperator.NE;
            case "<":
                return CriteriaOperator.LT;
            case "<=":
                return CriteriaOperator.LE;
            case ">":
                return CriteriaOperator.GT;
            case ">=":
                return CriteriaOperator.GE;
            case "|=":
                return CriteriaOperator.IN;
            case "IS NULL":
                return CriteriaOperator.NULL;
            case "IS_NOT_NULL":
                return CriteriaOperator.NOT_NULL;
            default:
                break;
        }

        return null;
    }

    private Long count(CustomQuery queryObj, CriteriaBuilder builder) {
        CriteriaQuery<Long> criteriaQueryCount = builder.createQuery(Long.class);
        Root from = criteriaQueryCount.from(entityClass);
        criteriaQueryCount.select(builder.count(from));
        Predicate predicate = getCriteria(queryObj, from, builder);
        if (predicate != null) {
            criteriaQueryCount.where(predicate);
        }

        TypedQuery countQuery = entityManager.createQuery(criteriaQueryCount);

        return (Long) countQuery.getSingleResult();
    }

    private Predicate getCriteria(CustomQuery queryObj, Root from, CriteriaBuilder builder) {
        Predicate predicate = null;
        if (queryObj.getCriteria() != null && queryObj.getCriteria().size() > 0) {
            Predicate pred = null;
            for (QueryCriteria entry : queryObj.getCriteria()) {
                if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
                    String[] key = entry.getField().split("\\.");
                    if (key.length > 1) {
                        Join<Object, Object> join = null;
                        for (int i = 0; i < key.length - 1; i++) {
                            join = getJoin(key[i]);
                            if (join == null) {
                                join = join != null ? join.join(key[i], JoinType.LEFT) : from.join(key[i], JoinType.LEFT);
                                addJoin(key[i], join);
                            }
                        }
                        entry.setField(key[key.length - 1]);
                        pred = getPredicate(entry, from, join, builder);

                    } else {
                        pred = getPredicate(entry, from, null, builder);
                    }
                    predicate = predicate == null ? pred : builder.and(predicate, pred);
                }
            }
        }

        return predicate;
    }

}