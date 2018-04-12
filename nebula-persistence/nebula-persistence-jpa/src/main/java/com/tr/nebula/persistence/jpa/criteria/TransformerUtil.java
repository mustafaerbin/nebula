package com.tr.nebula.persistence.jpa.criteria;

import com.tr.nebula.common.lang.BooleanHolder;
import com.tr.nebula.common.lang.Increment;
import com.tr.nebula.common.lang.Pair;
import com.tr.nebula.common.util.StringJoiner;
import com.tr.nebula.common.util.Strings;
import com.tr.nebula.persistence.api.criteria.Criteria;
import com.tr.nebula.persistence.api.criteria.CriteriaJoin;
import com.tr.nebula.persistence.api.criteria.CriteriaParent;
import com.tr.nebula.persistence.api.criteria.Order;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kamilbukum on 30/01/2017.
 */
public class TransformerUtil {
    public static class Elements {
        Map<String, String> elementsMap = null;
        String elementsQuery = null;
    }

    /**
     * @param criteria
     * @param <T>
     * @return
     */
    public static <T> Pair<String, Map<String, Object>> query(Criteria<T> criteria, Elements elements, List<String> selectList) {
        StringBuilder listBuilder = new StringBuilder();
        // configure select
        StringJoiner selectJoiner = new StringJoiner(", ");
        BooleanHolder groupBy = new BooleanHolder(false);
        Pair<String, Map<String, Object>> commonPair = criteriaToQuery(criteria, selectJoiner, elements, groupBy, selectList);
        listBuilder
                .append("SELECT ")
                .append(selectJoiner.length() > 0 ? selectJoiner.toString() : criteria.getAlias())
                .append(" ")
                .append(commonPair.getLeft())
                .append("\n")
                .append(orderBy(criteria));
        String query = listBuilder.toString();
        Pair<String, Map<String, Object>> pair = new Pair<>(listBuilder.toString(), commonPair.getRight());
        return pair;
    }

    /**
     * @param criteria
     * @param groupBy
     * @param <T>
     * @return
     */
    public static <T> Pair<String, Map<String, Object>> count(Criteria<T> criteria, BooleanHolder groupBy, List<String> selectList) {
        StringBuilder countBuilder = new StringBuilder();
        Pair<String, Map<String, Object>> commonPair = criteriaToQuery(criteria, null, null, groupBy, selectList);
        countBuilder
                .append("SELECT ")
                .append("count(1)")
                .append(" ")
                .append(commonPair.getLeft());
        return new Pair<>(countBuilder.toString(), commonPair.getRight());
    }

    /**
     * @param criteria
     * @param groupBy
     * @param <T>
     * @return
     */
    public static <T> Pair<String, Pair<String, Map<String, Object>>> pairList(Criteria<T> criteria, Elements elements, BooleanHolder groupBy, List<String> selectList) {
        StringBuilder countBuilder = new StringBuilder();
        // configure select
        StringJoiner selectJoiner = new StringJoiner(", ");
        for (String select : selectList) {
            selectJoiner.add(select);
        }

        Pair<String, Map<String, Object>> commonPair = criteriaToQuery(criteria, selectJoiner, elements, groupBy, selectList);
        String listQuery = "SELECT " + (selectJoiner.length() > 0 ? selectJoiner.toString() : criteria.getAlias());
        String commonQueryForList = " " + commonPair.getLeft() + "\n" + orderBy(criteria);
        if (elements != null && elements.elementsMap != null && elements.elementsMap.size() > 0) {
            elements.elementsQuery = "SELECT " + criteria.getAlias() + commonQueryForList;
        }
        listQuery = listQuery + commonQueryForList;
        countBuilder
                .append("SELECT ")
                .append("count(1)")
                .append(" ")
                .append(commonPair.getLeft());
        return new Pair<>(listQuery, new Pair<>(countBuilder.toString(), commonPair.getRight()));
    }


    /**
     * @param criteria
     * @param selectJoiner
     * @param groupBy
     * @param <E>
     * @return
     */
    public static <E> Pair<String, Map<String, Object>> criteriaToQuery(Criteria<E> criteria, StringJoiner selectJoiner, Elements elements, BooleanHolder groupBy, List<String> selectList) {
        Map<String, Object> variableMap = new LinkedHashMap<>();
        String from = from(criteria);
        StringJoiner joinJoiner = new StringJoiner("\n");
        StringJoiner restrictionJoiner = new StringJoiner(" AND ");
        StringJoiner qJoiner = new StringJoiner(" OR ");
        StringJoiner groupByJoiner = new StringJoiner(", ");
        criteriaToQuery(criteria, selectJoiner, elements, joinJoiner, restrictionJoiner, qJoiner, groupByJoiner, variableMap, selectList);

        StringBuilder builder = new StringBuilder();

        if (joinJoiner.length() > 0) {
            builder.append("\n").append(joinJoiner.toString());
        }
        if (restrictionJoiner.length() > 0) {
            builder.append("\nWHERE ");
            builder.append(restrictionJoiner.toString());
            if (qJoiner.length() > 0) {
                if (qJoiner.length() > 1) {
                    builder.append(" AND ( ").append(qJoiner.toString()).append(" ) ");
                } else {
                    builder.append(" AND ").append(qJoiner.toString());
                }
            }
        } else if (qJoiner.length() > 0) {
            builder.append("\nWHERE ");
            builder.append(qJoiner.toString());
        }

        if (groupByJoiner.length() > 0) {
            groupBy.set(true);
            builder.append("\nGROUP BY ").append(groupByJoiner.toString());
        }
        return new Pair<>(from + builder.toString(), variableMap);
    }

    /**
     * @param criteria
     * @param selectJoiner
     * @param elements
     * @param joinJoiner
     * @param restrictionJoiner
     * @param qJoiner
     * @param groupJoiner
     * @param variableMap
     * @param <E>
     */
    public static <E> void criteriaToQuery(
            CriteriaParent<E> criteria,
            StringJoiner selectJoiner,
            Elements elements,
            StringJoiner joinJoiner,
            StringJoiner restrictionJoiner,
            StringJoiner qJoiner,
            StringJoiner groupJoiner,
            Map<String, Object> variableMap,
            List<String> selectList
    ) {
//        if (selectJoiner != null) {
//            if (criteria.getProjection() != null) {
//                String select = JpaQueryUtil.selectForListByProjection(criteria, criteria.getProjection(), null, elements, groupJoiner, selectList);
//                if (Strings.has(select)) {
//                    selectJoiner.add(select);
//                }
//            }
//        }
        if (criteria.getRestrictions() != null && criteria.getRestrictions().size() > 0) {
            JpaQueryUtil.generateRestrictions(
                    criteria,
                    criteria.getRestrictions(),
                    restrictionJoiner,
                    qJoiner,
                    variableMap,
                    new Increment(0)
            );
        }

        if (criteria.getJoins() != null && criteria.getJoins().size() > 0) {
            for (Map.Entry<String, CriteriaJoin<E>> joinEntry : criteria.getJoins().entrySet()) {
                String joinToString = JpaQueryUtil.joinToString(joinEntry.getValue(), criteria);
                if (Strings.has(joinToString)) {
                    joinJoiner.add(joinToString);
                }
                criteriaToQuery(joinEntry.getValue(), selectJoiner, elements, joinJoiner, restrictionJoiner, qJoiner, groupJoiner, variableMap, selectList);
            }
        }
    }

    /**
     * @param criteria
     * @param <E>
     * @return
     */
    public static <E> String orderBy(Criteria<E> criteria) {
        if (criteria.getOrders() != null && criteria.getOrders().size() > 0) {
            StringJoiner joiner = new StringJoiner(", ");
            for (Order order : criteria.getOrders()) {
                String name;
                if (order.isAlias()) {
                    name = order.getName();
                } else {
                    if (!Strings.has(order.getCriteriaAlias())) {
                        name = order.getName();
                    } else {
                        name = order.getCriteriaAlias() + "." + order.getName();
                    }
                }
                joiner.add(name + " " + order.getType().name());
            }
            return "ORDER BY " + joiner.toString();
        }
        return "";
    }

    /**
     * @param criteria
     * @return
     */
    public static String from(Criteria criteria) {
        return "\nFROM " + criteria.getEntityClass().getName() + " " + criteria.getAlias();
    }
}
