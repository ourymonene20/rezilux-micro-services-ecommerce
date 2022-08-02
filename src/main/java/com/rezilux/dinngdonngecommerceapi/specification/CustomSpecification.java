package com.rezilux.dinngdonngecommerceapi.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CustomSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    public CustomSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                try{
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                } catch(NumberFormatException ex){ // handle your exception
                    ex.printStackTrace();
                }
            }
        }
        else if (criteria.getOperation().equalsIgnoreCase("!")) {
            return builder.notEqual(
                    root.get(criteria.getKey()), criteria.getValue());
        }
        return null;
    }
}
