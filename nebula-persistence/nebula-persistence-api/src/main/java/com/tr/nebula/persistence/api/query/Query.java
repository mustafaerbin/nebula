package com.tr.nebula.persistence.api.query;

import com.tr.nebula.common.util.StringJoiner;
import com.tr.nebula.common.util.Strings;
import com.tr.nebula.common.util.reflection.Fields;
import com.tr.nebula.persistence.api.criteria.*;
import com.tr.nebula.persistence.api.criteria.*;
import com.tr.nebula.persistence.api.criteria.cache.FieldMeta;
import com.tr.nebula.persistence.api.criteria.criterion.Operator;
import com.tr.nebula.persistence.api.criteria.criterion.Restrictions;
import com.tr.nebula.persistence.api.criteria.projection.ProjectionList;
import com.tr.nebula.persistence.api.criteria.projection.Projections;
import com.tr.nebula.persistence.api.query.search.SearchModel;

import java.util.LinkedList;
import java.util.List;
/**
 * Created by kamilbukum on 10/01/2017.
 */
public class Query<E> {
    /**
     * provides to transform criteria to query
     */
    private Transformer<E> transformer;

    /**
     *
     * @param transformer
     */
    public Query(Transformer<E> transformer) {
        this.transformer = transformer;
    }

    /**
     *
     * @param entityClass
     * @param search
     * @return
     */
    public Criteria<E> createCriteria(Class<?> entityClass, SearchModel search) {
        Criteria<E> criteria = Criteria.createCriteria(entityClass, transformer);
        if(search == null) {
            return criteria;
        }

        if(search.getQ() != null && search.getQ().length() > 0) {
            String[] queries = new String[]{search.getQ()};
            QueryUtility.configureCriteriaByQ(criteria, queries);
        }

        if(search.getFilter() != null && search.getFilter().length > 0) {
            QueryUtility.configureFilters(criteria, search.getFilter(), 0);
        }

        if(search.getSort() != null && search.getSort().length > 0) {
            QueryUtility.configureSorts(criteria, search.getSort());
        }

        QueryUtility.configureSelectFields(criteria, search);

        if(search.getLimit() != null) {
            criteria.setLimit(search.getLimit());
        }
        if(search.getOffset() != null) {
            criteria.setOffset(search.getOffset());
        }
        return criteria;
    }


    public static <E> void addRestrictions(Criteria<E> criteria, String[][] filters){
        if(filters != null && filters.length > 0) {
            for(String[] filter: filters) {
                if(filter == null || filter.length == 0) continue;
                String name = filter[0];
                if(!Strings.has(name)) continue;

                String op = filter.length > 1 ? filter[1]: null;
                String rawValue = filter.length > 2 ? filter[2]: null;
                Holder<E> holder = configureFieldByName(criteria, name);
                if(holder == null) continue;

                FieldMeta fieldMeta = holder.currentFieldMeta;
                Object value = Strings.has(rawValue) ? getValue(op, rawValue, fieldMeta.getField().getType()): null;
                addRestriction(holder, op, value);
            }
        }
    }

    private static <E> void addRestriction(Holder<E> holder, String op, Object value){
        if(holder.currentFieldMeta.isCollection()) {
            CriteriaJoin<E> criteriaJoin = holder.currentCriteria.getJoin(holder.currentFieldName);
            if(criteriaJoin == null) {
                holder.currentCriteria.createJoin(holder.currentFieldName, String.class);
            }
            criteriaJoin.add(Restrictions.filter(holder.currentFieldName, op, value));
        } else {
            holder.currentCriteria.add(Restrictions.filter(holder.currentFieldName, op, value));
        }
    }

    public static <E> void addProjections(Criteria<E> criteria, String[] projections){
        if(projections != null && projections.length > 0) {
            for(String projection: projections) {
                // if projection is empty then ignore it
                if(!Strings.has(projection)) continue;

                Holder<E> holder = configureFieldByName(criteria, projection);
                if(holder == null) continue;

                ProjectionList projectionList = null;
                if(holder.currentCriteria.getProjection() != null) {
                    if(!(holder.currentCriteria.getProjection() instanceof ProjectionList)) {
                        projectionList = Projections.projectionList();
                        holder.currentCriteria.setProjection(projectionList.add(holder.currentCriteria.getProjection()));
                    }
                } else {
                    projectionList = Projections.projectionList();
                    holder.currentCriteria.setProjection(projectionList.add(projectionList));
                }
                projectionList.add(Projections.property(projection));
            }
        }
    }

    public static <E> void addOrder(Criteria<E> criteria, String[] sorts){
        if(sorts != null && sorts.length > 0) {
            for(String sort: sorts) {
                // if projection is empty then ignore it
                sort = checkStringAndTrim(sort);
                if(sort == null) continue;

                if(sort.length() < 2) continue;
                String op = sort.substring(0, 1);
                Order.Type type = Order.Type.value(op);
                sort = sort.substring(1);
                Holder<E> holder = configureFieldByName(criteria, sort);
                if(holder == null) continue;
                Order order = type == Order.Type.ASC ? Order.asc(holder.currentFieldName): Order.desc(holder.currentFieldName);
                holder.currentCriteria.addOrder(order);
            }
        }
    }

    private static class Holder<T> {
        private String currentFieldName;
        private CriteriaParent<T> currentCriteria;
        private FieldMeta currentFieldMeta;
    }

    public static String checkStringAndTrim(String name){
        if(name != null) {
            name = name.trim();
            if(name.equals("")) return null;
        }
        return name;
    }

    // roleOid.name
    // roleOid.permissionOid.name
    // roleOid.permissionOid.code
    public static <E> Holder<E> configureFieldByName(Criteria<E> criteria, String name){
        if(!Strings.has(name)) return null;
        // parse x.y name as [x, y]
        String[] names = name.split("\\.");
        // uses to keep current name by names index.
        String currentName;
        // init start index of names.
        int step = 0;
        // always will be current criteria
        CriteriaParent<E> currentCriteria = criteria;
        FieldMeta currentFieldMeta;
        // use aliasJoiner to use as alias.
        StringJoiner aliasJoiner = new StringJoiner("$");
        do {
            // get current name of field by index. like x.y.z => if step = 1 then currentName = y
            currentName = names[step];
            if(!Strings.has(currentName)) {
                throw new RuntimeException(currentName + " defined name is wrong ! ");
            }
            currentFieldMeta = criteria.getMeta().getFieldMap().get(currentName);
            step++;
            aliasJoiner.add(currentCriteria.getAlias());
            if(step >= names.length) {
                break;
            }
            if(currentFieldMeta.getReference() == null) {
                throw new RuntimeException("" + currentName + " join field of " + name + "'s reference target information must defined ! ");
            }
            CriteriaJoin<E> criteriaJoin = currentCriteria.getJoin(currentName);
            if(criteriaJoin == null) {
                currentCriteria.createJoin(currentName, currentFieldMeta.getReference().getTargetEntity(), currentFieldMeta.getReference().getReferenceId());
            }
            currentCriteria = criteriaJoin;
        }while (step >= names.length);

        Holder<E> holder = new Holder<>();
        holder.currentFieldName = currentName;
        holder.currentCriteria = currentCriteria;
        holder.currentFieldMeta = currentFieldMeta;
        return holder;
    }

    /**
     *
     * @param operator
     * @param rawValue
     * @param fieldType
     * @return
     */
    public static Object getValue(String operator, String rawValue, Class<?> fieldType){
        return getValue(Operator.value(operator), rawValue, fieldType);
    }
    /**
     *
     * @param operator
     * @param rawValue
     * @param fieldType
     * @return
     */
    public static Object getValue(Operator operator, String rawValue, Class<?> fieldType){
        if(Operator.IN.equals(operator)) {
            String[] svalues = rawValue.split("\\|");
            List<Object> lvalues = new LinkedList<>();
            for (String svalue : svalues) {
                lvalues.add(Fields.castValue(fieldType, svalue));
            }
            return lvalues;
        }
        return Fields.castValue(fieldType, rawValue);
    }
}
