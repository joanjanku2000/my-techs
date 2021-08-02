package com.project.Restaurant.repository;

import com.project.Restaurant.utils.FieldValuePair;
import com.project.Restaurant.utils.SearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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

    public List genericFilter(Class c, SearchCriteria searchCriteria, String fetch) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(c);
        Root root = cq.from(c);
        List<Predicate> predicateList = new ArrayList<>();
        List<Field> fields = Arrays.asList(c.getDeclaredFields());
        Fetch f = null;

        if (isStringValid(fetch)) {
            if (isFieldPresent(fields, fetch)) {
                f = root.fetch(fetch, JoinType.INNER);
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

                } else {
                    log.info("Has to be nested");
                    String parentField = isObjectInside(fields, fieldValuePair.getField());
                    if (parentField != null) {
                        String childField = fieldValuePair.getField().replace(parentField, "");
                        log.info("ParentField name {} , childFieldName {}", parentField, childField);
                        if (fieldValuePair.getOperation() != null) {
                            if (fieldValuePair.getOperation().compareToIgnoreCase("greaterThanOrEqualTo") == 0) {
                                predicateList.add(
                                        cb.greaterThanOrEqualTo(root.join(parentField).get(childField), fieldValuePair.getValue())
                                );
                            } else if (fieldValuePair.getOperation().compareToIgnoreCase("lessThanOrEqualTo") == 0) {
                                predicateList.add(
                                        cb.lessThanOrEqualTo(root.join(parentField).get(childField), fieldValuePair.getValue())
                                );
                            }
                        } else {
                            log.info("Equals operation");
                            log.info("Parent name {}", parentField);
                            predicateList.add(
                                    cb.equal(root.join(parentField).get(childField), fieldValuePair.getValue())
                            );
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
        cq.distinct(true);
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

    /**
     * Return field name
     *
     * @param existing
     * @param name
     * @return
     */
    private String isObjectInside(List<Field> existing, String name) {
        for (Field field : existing) {
            if (name.contains(field.getName()) && field.getName().compareToIgnoreCase("id") != 0) {
                return field.getName();
            }
        }
        return null;
    }

    private boolean isDirAsc(String s) {
        return s.compareToIgnoreCase("asc") == 0;
    }
}
