package com.tr.nebula.persistence.jpa.criteria;

import com.tr.nebula.common.lang.BooleanHolder;
import com.tr.nebula.common.lang.Pair;
import com.tr.nebula.persistence.api.converter.Converter;
import com.tr.nebula.persistence.api.criteria.Criteria;
import com.tr.nebula.persistence.api.criteria.CriteriaJoin;
import com.tr.nebula.persistence.api.criteria.Result;
import com.tr.nebula.persistence.api.criteria.Transformer;
import com.tr.nebula.persistence.api.criteria.cache.EntityMetaFinder;
import com.tr.nebula.persistence.api.criteria.cache.FieldMeta;
import com.tr.nebula.persistence.api.criteria.projection.ProjectionList;
import com.tr.nebula.persistence.api.criteria.projection.PropertyProjection;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public class TransformerImpl<E> extends Transformer<E> {
    private static EntityMetaFinder finder = new EntityMetaFinderImpl();
    private EntityManager entityManager;

    public TransformerImpl(EntityManager entityManager) {
        this(entityManager, null);
    }

    public TransformerImpl(EntityManager entityManager, Class<E> transformClass) {
        super(transformClass, finder);
        this.entityManager = entityManager;
    }

    @Override
    public List<E> list(Criteria<E> criteria) {
        TransformerUtil.Elements elements = new TransformerUtil.Elements();
        List<String> selectList = new LinkedList<>();
        Pair<String, Map<String, Object>> pair = TransformerUtil.query(criteria, elements, selectList);

        Query query = entityManager.createQuery(pair.getLeft());
        if (criteria.getLimit() != null) {
            query.setMaxResults(criteria.getLimit());
        }
        if (criteria.getOffset() != null) {
            query.setFirstResult(criteria.getOffset());
        }
        for (Map.Entry<String, Object> parameter : pair.getRight().entrySet()) {
            setParameter(query, parameter.getKey(), parameter.getValue());
        }
        List<E> destinationList = Converter.convert(criteria, criteria.getEntityClass(), getTransformClass(), finder, selectList, query.getResultList());
        setElementsToList(criteria, pair.getRight(), elements, destinationList);
        return destinationList;
    }

    public void setElementsToList(Criteria<E> criteria, Map<String, Object> variableMap, TransformerUtil.Elements elements, List<?> destinationList) {
        if (elements.elementsQuery == null || elements.elementsMap == null || elements.elementsMap.size() == 0) return;
        Query elementsQuery = entityManager.createQuery(elements.elementsQuery);
        if (criteria.getLimit() != null) {
            elementsQuery.setMaxResults(criteria.getLimit());
        }
        if (criteria.getOffset() != null) {
            elementsQuery.setFirstResult(criteria.getOffset());
        }
        for (Map.Entry<String, Object> parameter : variableMap.entrySet()) {
            setParameter(elementsQuery, parameter.getKey(), parameter.getValue());
        }
        List<?> sourceList = elementsQuery.getResultList();
        if (sourceList.size() > 0) {
            for (int i = 0; i < destinationList.size(); i++) {
                Object sourceObject = sourceList.get(i);
                Object destinationObject = destinationList.get(i);
                for (Map.Entry<String, String> elementEntry : elements.elementsMap.entrySet()) {
                    FieldMeta srcField = criteria.getMeta().getFieldMap().get(elementEntry.getKey());
                    FieldMeta destinationField = criteria.getTransformer().getMeta().getFieldMap().get(elementEntry.getKey());
                    try {
                        if (!destinationField.getField().isAccessible()) {
                            destinationField.getField().setAccessible(true);
                        }
                        if (!srcField.getField().isAccessible()) {
                            srcField.getField().setAccessible(true);
                        }
                        destinationField.getField().set(destinationObject, srcField.getField().get(sourceObject));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    public Result<E> pairList(Criteria<E> criteria) {
        Result<E> result = new Result<>();
        BooleanHolder groupBy = new BooleanHolder(false);
        TransformerUtil.Elements elements = new TransformerUtil.Elements();
        List<String> selectList = new LinkedList<>();
        selectList = getSelectionList(criteria);
        Pair<String, Pair<String, Map<String, Object>>> pair = TransformerUtil.pairList(criteria, elements, groupBy, selectList);
        Query listQuery = entityManager.createQuery(pair.getLeft());
        if (elements.elementsMap != null && elements.elementsMap.size() > 0) {

        }
        if (criteria.getLimit() != null) {
            listQuery.setMaxResults(criteria.getLimit());
        }
        if (criteria.getOffset() != null) {
            listQuery.setFirstResult(criteria.getOffset());
        }
        Query countQuery = entityManager.createQuery(pair.getRight().getLeft());
        for (Map.Entry<String, Object> parameter : pair.getRight().getRight().entrySet()) {
            setParameter(listQuery, parameter.getKey(), parameter.getValue());
            setParameter(countQuery, parameter.getKey(), parameter.getValue());
        }
        List<E> destinationList = null;
        if (selectList != null && selectList.size() > 0) {
            destinationList = Converter.convert(criteria, criteria.getEntityClass(), getTransformClass(), finder, selectList, listQuery.getResultList());
        } else {
            destinationList = listQuery.getResultList();
        }
        setElementsToList(criteria, pair.getRight().getRight(), elements, destinationList);
        result.setList(destinationList);
        result.setTotalCount(groupBy.is() ? countQuery.getResultList().size() : (long) countQuery.getSingleResult());
        return result;
    }

    public void setParameter(Query query, String key, Object value) {
        query.setParameter(key, value);
    }

    @Override
    public Long count(Criteria<E> criteria) {
        BooleanHolder groupBy = new BooleanHolder(false);
        List<String> selectList = new LinkedList<>();
        Pair<String, Map<String, Object>> pair = TransformerUtil.count(criteria, groupBy, selectList);
        Query query = entityManager.createQuery(pair.getLeft());
        for (Map.Entry<String, Object> parameter : pair.getRight().entrySet()) {
            setParameter(query, parameter.getKey(), parameter.getValue());
        }
        return groupBy.is() ? query.getResultList().size() : (long) query.getSingleResult();
    }

    @Override
    public E uniqueResult(Criteria<E> criteria) {
        TransformerUtil.Elements elements = new TransformerUtil.Elements();
        List<String> selectList = new LinkedList<>();
        Pair<String, Map<String, Object>> pair = TransformerUtil.query(criteria, elements, selectList);
        Query query = entityManager.createQuery(pair.getLeft());

        for (Map.Entry<String, Object> parameter : pair.getRight().entrySet()) {
            setParameter(query, parameter.getKey(), parameter.getValue());
        }
        return Converter.convert(
                criteria.getEntityClass(),
                getTransformClass(),
                getFinder(),
                selectList,
                (Object[]) query.getSingleResult()
        );
    }

    @Override
    public EntityMetaFinder getFinder() {
        return finder;
    }

    private List<String> getSelectionList(Criteria<E> criteria) {
        List<String> selectionList = new ArrayList<>();
        ProjectionList selects = ((ProjectionList) criteria.getProjection());
        loadProjectionList(selectionList, selects, criteria.getAlias());

        for (String joinKey : criteria.getJoins().keySet()) {
            CriteriaJoin join = criteria.getJoin(joinKey);
            selects = ((ProjectionList) join.getProjection());
            loadProjectionList(selectionList, selects, join.getAlias());
        }
        return selectionList;
    }


    private void loadProjectionList(List<String> selectionList, ProjectionList selects, String alias) {
        if (selects != null) {
            for (int i = 0; i < selects.getLength(); i++) {
                if (selects.getProjection(i) instanceof PropertyProjection)
                    selectionList.add(alias + "." + ((PropertyProjection) selects.getProjection(i)).getProperty());
            }
        }
    }

}
