package com.project.restaurant.base.specifications;

import com.project.restaurant.base.utils.FieldValuePair;
import com.project.restaurant.base.utils.SearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class SpecificationFilter{
    public <T> Specification<T> userFilter(SearchCriteria searchCriteria, Class<T> c, String fetch) {
        return new Specification<T>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> predicateList = new ArrayList<>();
                List<Field> fields = Arrays.asList(c.getDeclaredFields());
                fetch(root, fields, fetch);
                if (searchCriteria.getFieldValuePair() == null) {
                    return cb.and(predicateList.toArray(new Predicate[0]));
                }
                searchCriteria.getFieldValuePair().forEach(fieldValuePair -> {
                    if (isFieldPresent(fields, fieldValuePair.getField())) {
                        operationPerform(cb, root, predicateList, fieldValuePair);
                    } else {
                        log.info("Has to be nested");
                        String parentField = isObjectInside(fields, fieldValuePair.getField());
                        if (parentField != null) {
                            nestedOperation(cb, root, predicateList, fieldValuePair, parentField);
                        }
                    }
                });

                log.info("Order by {} Sort dir {}", searchCriteria.getOrderBy(), searchCriteria.getSortDirection());
                order(searchCriteria, cb, cq, root, fields);
                cq.distinct(true);
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }

        };
    }

    private <T> void fetch(Root<T> root, List<Field> fields, String fetch) {
        if (isStringValid(fetch)) {
            if (isFieldPresent(fields, fetch)) {
                root.fetch(fetch, JoinType.INNER);
            } else {
                log.info("hyn te elsi i fetchit");
                String parentField = isObjectInside(fields, fetch);
                if (parentField != null) {
                    String childField = fetch.replace(parentField, "");
                    log.info("Parent = {}, Child = {}", parentField, childField);
                    root.fetch(parentField).fetch(childField);
                }
            }
        }
    }

    private boolean isStringValid(String s) {
        return s != null && !s.isEmpty() && s.length() > 2;
    }

    private boolean isFieldPresent(List<Field> existing, String name) {
        for (Field field : existing) {
            if (field.getName().compareTo(name) == 0) {
                return true;
            }
        }
        return false;
    }

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

    private <T> void nestedOperation(CriteriaBuilder cb, Root<T> root, List<Predicate> predicateList, FieldValuePair fieldValuePair, String parentField) {
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

    private <T> void operationPerform(CriteriaBuilder cb, Root<T> root, List<Predicate> predicateList, FieldValuePair fieldValuePair) {
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
            if (!isBool(fieldValuePair.getValue()) && fieldValuePair.getOperation().compareToIgnoreCase("greaterThanOrEqualTo") == 0) {
                predicateList.add(cb.greaterThanOrEqualTo(root.get(fieldValuePair.getField()), fieldValuePair.getValue()));
            } else if (!isBool(fieldValuePair.getValue()) && fieldValuePair.getOperation().compareToIgnoreCase("lessThanOrEqualTo") == 0) {
                predicateList.add(cb.lessThanOrEqualTo(root.get(fieldValuePair.getField()), fieldValuePair.getValue()));
            }
        }
    }

    private boolean isBool(String s) {
        return s.compareToIgnoreCase("true") == 0 || s.compareToIgnoreCase("false") == 0;
    }

    private <T> void order(SearchCriteria searchCriteria, CriteriaBuilder cb, CriteriaQuery<?> cq, Root<T> root, List<Field> fields) {
        if (isStringValid(searchCriteria.getOrderBy()) && isFieldPresent(fields, searchCriteria.getOrderBy())) {

            if (isStringValid(searchCriteria.getSortDirection())) {

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
}
