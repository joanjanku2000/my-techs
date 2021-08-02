package com.project.Restaurant.repository;

import com.project.Restaurant.entities.Orders;
import com.project.Restaurant.utils.FieldValuePair;
import com.project.Restaurant.utils.SearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Repository
public class Filters {

    private final EntityManager entityManager;

    @Autowired
    public Filters(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    //first filter specific only to Order
    public List<Orders> orderFilter(SearchCriteria searchCriteria) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> root = cq.from(Orders.class);
        List<Predicate> predicateList = new ArrayList<>();

        List<Field> fields = Arrays.asList(Orders.class.getDeclaredFields());
        log.info("Fields of Orders.class = {}", fields.get(0).getName());

        for (FieldValuePair fieldValuePair : searchCriteria.getFieldValuePair()) {
            if (isFieldPresent(fields, fieldValuePair.getField())) {
                log.info("Field {} is present ", fieldValuePair.getField());
                if (fieldValuePair.getValue().compareToIgnoreCase("false") == 0
                        || fieldValuePair.getValue().compareToIgnoreCase("true") == 0) {
                    predicateList.add(cb.equal(root.get(fieldValuePair.getField())
                            , Boolean.valueOf(fieldValuePair.getValue())));
                } else {
                    predicateList.add(cb.equal(root.get(fieldValuePair.getField())
                            , fieldValuePair.getValue()));
                }

            }
        }
        log.info("Predicates {} ", predicateList);

        cq.select(root).where(predicateList.toArray(new Predicate[predicateList.size()]));
        if (searchCriteria.getOrderBy() != null && !searchCriteria.getOrderBy().isBlank()) {
            if (isFieldPresent(fields, searchCriteria.getOrderBy())) {
                if (searchCriteria.getSortDirection() != null
                        && !searchCriteria.getSortDirection().isBlank()) {
                    if (searchCriteria.getSortDirection().compareToIgnoreCase("asc") == 0) {
                        cq.orderBy(
                                cb.asc(root.get(searchCriteria.getOrderBy()))
                        );
                    } else {
                        cq.orderBy(
                                cb.desc(root.get(searchCriteria.getOrderBy()))
                        );
                    }
                }

            }
        }
        TypedQuery<Orders> filter = entityManager.createQuery(cq);
        return filter.getResultList();
    }

    public List genericFilter(Class c, SearchCriteria searchCriteria, String fetch) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(c);
        Root root = cq.from(c);
        List<Predicate> predicateList = new ArrayList<>();
        List<Field> fields = Arrays.asList(c.getDeclaredFields());

        if (isStringValid(fetch)) {
            if (isFieldPresent(fields, fetch)) {
                root.fetch(fetch, JoinType.INNER);
            }
        }
        if (searchCriteria == null) {
            return entityManager.createQuery(cq.select(root)).getResultList();
        }

        if (searchCriteria.getFieldValuePair() != null) {
            for (FieldValuePair fieldValuePair : searchCriteria.getFieldValuePair()) {
                if (isFieldPresent(fields, fieldValuePair.getField())) {
                    if (!isStringValid(fieldValuePair.getOperation())) {
                        if (fieldValuePair.getField() != null && fieldValuePair.getValue() != null) {
                            log.info("Field {} is present ", fieldValuePair.getField());
                            predicateList.add(
                                    isBool(fieldValuePair.getValue()) ?
                                            cb.equal(root.get(fieldValuePair.getField()), Boolean.valueOf(fieldValuePair.getValue()))
                                            :
                                            cb.equal(root.get(fieldValuePair.getField()), fieldValuePair.getValue())
                            );
                        }
                    } else {
                        if (!isBool(fieldValuePair.getValue())) {
                            if (fieldValuePair.getOperation().compareToIgnoreCase("greaterThanOrEqualTo") == 0) {
                                predicateList.add(cb.greaterThanOrEqualTo(root.get(fieldValuePair.getField()), fieldValuePair.getValue()));
                            } else if (fieldValuePair.getOperation().compareToIgnoreCase("lessThanOrEqualTo") == 0) {
                                predicateList.add(cb.lessThanOrEqualTo(root.get(fieldValuePair.getField()), fieldValuePair.getValue()));
                            }
                        }

                    }

                }
            }
        }
        cq.select(root).where(predicateList.toArray(new Predicate[predicateList.size()]));

        log.info("Order by {} Sort dir {}", searchCriteria.getOrderBy(), searchCriteria.getSortDirection());

        if (isStringValid(searchCriteria.getOrderBy())) {
            log.info("Order by ain't blank {} " + searchCriteria.getOrderBy());
            if (isFieldPresent(fields, searchCriteria.getOrderBy())) {
                log.info("Field is present {}" + searchCriteria.getOrderBy());
                if (isStringValid(searchCriteria.getSortDirection())) {
                    log.info("Sort direction isn't blank");
                    cq.orderBy(
                            isDirAsc(searchCriteria.getSortDirection())
                                    ?
                                    cb.asc(root.get(searchCriteria.getOrderBy()))
                                    :
                                    cb.desc(root.get(searchCriteria.getOrderBy()))
                    );
                } else {
                    cq.orderBy(
                            cb.desc(root.get(searchCriteria.getOrderBy()))
                    );
                }

            }
        }
        return entityManager.createQuery(cq).getResultList();
    }

    private boolean isFieldPresent(List<Field> existing, String name) {
        for (Field field : existing) {
            if (field.getName().compareTo(name) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isStringValid(String s) {
        return s != null && !s.isBlank();
    }

    private boolean isBool(String s) {
        return s.compareToIgnoreCase("true") == 0 || s.compareToIgnoreCase("false") == 0;
    }

    private boolean isDirAsc(String s) {
        return s.compareToIgnoreCase("asc") == 0;
    }
}
