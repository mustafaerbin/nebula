package com.tr.nebula.persistence.api.query;

import com.tr.nebula.common.util.Strings;
import com.tr.nebula.common.util.reflection.Fields;
import com.tr.nebula.persistence.api.criteria.Criteria;
import com.tr.nebula.persistence.api.criteria.CriteriaJoin;
import com.tr.nebula.persistence.api.criteria.CriteriaParent;
import com.tr.nebula.persistence.api.criteria.Order;
import com.tr.nebula.persistence.api.criteria.cache.EntityMeta;
import com.tr.nebula.persistence.api.criteria.cache.FieldMeta;
import com.tr.nebula.persistence.api.criteria.criterion.Operator;
import com.tr.nebula.persistence.api.criteria.criterion.Restriction;
import com.tr.nebula.persistence.api.criteria.criterion.Restrictions;
import com.tr.nebula.persistence.api.criteria.projection.ProjectionList;
import com.tr.nebula.persistence.api.criteria.projection.Projections;
import com.tr.nebula.persistence.api.query.search.SearchModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Mustafa Erbin on 17/01/2017.
 */
public class QueryUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryUtility.class);

    /**
     * @param criteria
     * @param queries
     */
    static <E> void configureCriteriaByQ(CriteriaParent<E> criteria, String[] queries) {
        Set<String> joinedClassNames = new HashSet<>();
        configureCriteriaByQ(criteria, queries, criteria.getMeta().getFieldMap().keySet(), joinedClassNames);
    }


    static <E> void configureCriteriaByQ(CriteriaParent<E> criteria, String[] queries, Collection<String> fieldSet, Set<String> joinedClassNames) {
        List<Restriction> restrictions = new LinkedList<>();
        for (String fieldName : fieldSet) {
            FieldMeta fieldMeta = criteria.getMeta().getFieldMap().get(fieldName);
            if (fieldMeta == null) {
                LOGGER.warn("Not found defined field name in the filtering section in the " + criteria.getEntityClass().getName() + " class ! ");
            }
            if (!fieldMeta.isSearchIgnore() && !fieldMeta.isTransient() && fieldMeta.getField().getType().equals(String.class)) {
                configureQForField(fieldName, fieldMeta, queries, restrictions);
            }
            if (fieldMeta.getReference() != null) {
                if (fieldMeta.getReference().getFilters() != null && fieldMeta.getReference().getFilters().length > 0) {
                    String className = fieldMeta.getReference().getTargetEntity().getName();
                    if (joinedClassNames.contains(className)) {
                        return;
                    }
                    joinedClassNames.add(className);
                    CriteriaJoin<E> criteriaJoin = addOrGetJoin(fieldName, fieldMeta, criteria);
                    List<String> joinFieldSet = Arrays.asList(fieldMeta.getReference().getFilters());
                    configureCriteriaByQ(criteriaJoin, queries, joinFieldSet, joinedClassNames);
                }
            }
        }
        if (restrictions.size() > 0) {
            criteria.add(restrictions.size() == 1 ? restrictions.get(0) : Restrictions.or(restrictions));
        }
    }


    private static void configureQForField(String fieldName, FieldMeta fieldMeta, String[] queries, List<Restriction> restrictions) {
        // create filter
        for (int i = 0; i < queries.length; i++) {
            Operator op = Operator.Q;
            String rawValue = queries[i];
            Object value = getValue(op, rawValue, fieldMeta.getField());
            Restriction restriction = Restrictions.filter(fieldName, op, value);
            restriction.setValueAlias("$_query_" + i);
            restrictions.add(restriction);
        }
    }

    /**
     * @param criteria
     * @param filters
     * @param <E>
     */
    static <E> void configureFilters(CriteriaParent<E> criteria, String[][] filters, Integer restrictionOrder) {
        if (filters == null || filters.length == 0) return;
        Map<String, List<Restriction>> restrictionMap = new LinkedHashMap<>();
        Map<String, CriteriaParent<E>> criteriaMap = new LinkedHashMap<>();
        for (String[] filter : filters) {
            String name = filter[0];
            if (!Strings.has(name) && !Strings.has(filter[1])) continue;
            Operator operator = Operator.value(filter[1]);
            String rawValue = filter[2];
            Parent<E> parent = new Parent<>(criteria, name);
            if (!createCriteriaByGivenName(parent)) continue;
            FieldMeta fieldMeta = parent.criteria.getMeta().getFieldMap().get(parent.name);
            Object value = getValue(operator, rawValue, fieldMeta.getField());
            Restriction restriction = Restrictions.filter(parent.name, operator, value);
            if (restriction == null) continue;

            restriction.setValueAlias(parent.criteria.getAlias() + "_" + parent.name + "_" + restrictionOrder++);
            List<Restriction> restrictions = restrictionMap.get(parent.criteria.getAlias());
            if (restrictions == null) {
                restrictions = new LinkedList<>();
                restrictionMap.put(parent.criteria.getAlias(), restrictions);
                criteriaMap.put(parent.criteria.getAlias(), parent.criteria);
            }
            restrictions.add(restriction);
            parent.criteria = criteria;
        }
        for (Map.Entry<String, List<Restriction>> entry : restrictionMap.entrySet()) {
            criteriaMap.get(entry.getKey()).add(entry.getValue().size() == 1 ? entry.getValue().get(0) : Restrictions.and(entry.getValue()));
        }
    }

    /**
     * @param criteria
     * @param sorts
     * @param <E>
     */
    public static <E> void configureSorts(CriteriaParent<E> criteria, String[] sorts) {
        if (sorts == null || sorts.length == 0) return;
        for (String sort : sorts) {
            if (!Strings.has(sort)) continue;
            sort = sort.trim();
            if (sort.length() < 2) continue;
            String op = sort.substring(0, 1);
            Order.Type type = null;
            try {
                type = Order.Type.value(op);
                sort = sort.substring(1);
            } catch (Exception ex){
                type = Order.Type.ASC;
            }
            Parent<E> parent = new Parent<>(criteria, sort);
            if (!createCriteriaByGivenName(parent)) continue;
            Order order = type == Order.Type.ASC ? Order.asc(parent.name) : Order.desc(parent.name);
            parent.criteria.addOrder(order);
        }
    }

    public static <E> void configureSelectFields(Criteria<E> criteria, SearchModel search) {
        switch (criteria.getTransformer().getTransformType()) {
            case ENTITY:
                return;
            case DTO:
                QueryUtility.configureDtoSelects(criteria);
                break;
            case MAP:
                if (search.getFields() != null && search.getFields().length > 0) {
                    QueryUtility.configureFields(criteria, search.getFields());
                }
                break;
        }
    }

    public static <E> void configureDtoSelects(Criteria<E> criteria) {
        EntityMeta transformerMeta = criteria.getTransformer().getMeta();
        for (Map.Entry<String, FieldMeta> entry : transformerMeta.getFieldMap().entrySet()) {
            FieldMeta fieldMeta = entry.getValue();
            String fieldName = fieldMeta.hasRelation() ? fieldMeta.getRelationName() : entry.getKey();
            Parent<E> parent = new Parent<>(criteria, fieldName);
            ProjectionList projectionList = configureField(parent);
            if (projectionList != null) {
                projectionList.add(Projections.alias(fieldMeta.isCollection() ? Projections.elements(parent.name) : Projections.property(parent.name), entry.getKey()));
            }
        }
    }

    /**
     * @param criteria
     * @param fields
     * @param <E>
     */
    static <E> void configureFields(CriteriaParent<E> criteria, String[] fields) {
        if (fields == null || fields.length == 0) return;
        for (String field : fields) {
            if (!Strings.has(field)) continue;
            field = field.trim();
            Parent<E> parent = new Parent<>(criteria, field);
            ProjectionList projectionList = configureField(parent);
            if (projectionList != null) {
                FieldMeta fieldMeta = parent.criteria.getMeta().getFieldMap().get(parent.name);
                projectionList.add(fieldMeta.isCollection() ? Projections.elements(parent.name) : Projections.property(parent.name));
            }
        }
    }

    static <E> ProjectionList configureField(Parent<E> parent) {
        if (!createCriteriaByGivenName(parent)) return null;
        ProjectionList projection = (ProjectionList) parent.criteria.getProjection();
        if (parent.criteria.getProjection() == null) {
            projection = Projections.projectionList();
            parent.criteria.setProjection(projection);
        }
        return projection;
    }

    /**
     *
     */
    private static class Parent<E> {
        CriteriaParent<E> criteria;
        String name;

        public Parent(CriteriaParent<E> criteria, String name) {
            this.criteria = criteria;
            this.name = name;
        }
    }

    /**
     * @param parent
     * @param <E>
     * @return
     */
    private static <E> boolean createCriteriaByGivenName(Parent<E> parent) {
        EntityMeta meta = parent.criteria.getMeta();
        String[] names;
        if (meta.getFieldMap().get(parent.name) != null) {
            names = new String[]{parent.name};
        } else {
            names = parent.name.split("\\.");
        }
        int lastParentIndex = names.length - 1;
        int step = 0;

        FieldMeta fieldMeta;
        if (lastParentIndex <= step) {
            String fieldName = names[0];
            fieldMeta = meta.getFieldMap().get(fieldName);
            if (fieldMeta == null || fieldMeta.isTransient()) {
                return false;
            }
        } else {
            while (lastParentIndex > step) {
                String fieldName = names[step];
                fieldMeta = meta.getFieldMap().get(fieldName);
                if (fieldMeta == null) return false;
                if (fieldMeta.getReference() == null) {
                    throw new RuntimeException("Field is parent field. @SearchFrom is not defined on field ! ");
                }
                Class<?> joinClass = fieldMeta.getReference().getTargetEntity();
                EntityMeta joinMeta = parent.criteria.getTransformer().getMeta(joinClass);
                parent.criteria = addOrGetJoin(names[step], fieldMeta, parent.criteria);
                meta = joinMeta;
                step++;
            }
        }
        parent.name = names[step];
        return true;
    }

    /**
     * @param name
     * @param meta
     * @param criteriaParent
     * @return
     */
    private static <E> CriteriaJoin<E> addOrGetJoin(String name, FieldMeta meta, CriteriaParent<E> criteriaParent) {
        Class<?> joinClass = meta.getReference().getTargetEntity();
        CriteriaJoin<E> join = criteriaParent.getJoin("$" + name);
        if (join == null) {
            join = criteriaParent.createJoin(name, joinClass, name);
        }
        return join;
    }


    /**
     * @param operator
     * @param rawValue
     * @param field
     * @return
     */
    public static Object getValue(String operator, String rawValue, Field field) {
        return getValue(Operator.value(operator), rawValue, field);
    }

    /**
     * @param operator
     * @param rawValue
     * @param field
     * @return
     */
    public static Object getValue(Operator operator, String rawValue, Field field) {
        if (Operator.IN.equals(operator)) {
            if (!field.getType().isAssignableFrom(Collection.class)) {
                String[] svalues = rawValue.split("\\|");
                List<Object> lvalues = new LinkedList<>();
                for (String svalue : svalues) {
                    lvalues.add(Fields.castValue(field.getType(), svalue));
                }
                return lvalues;
            } else {
                return Fields.castValue(Fields.getTypeOfList(field), rawValue);
            }
        }
        return Fields.castValue(field.getType(), rawValue);
    }
}
