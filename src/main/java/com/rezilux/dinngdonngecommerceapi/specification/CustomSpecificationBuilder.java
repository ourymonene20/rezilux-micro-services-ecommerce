package com.rezilux.dinngdonngecommerceapi.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomSpecificationBuilder<T> {

    private final List<SearchCriteria> params;

    public CustomSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public CustomSpecificationBuilder<T> with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream().map(CustomSpecification::new).collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = !params.get(i).isOrPredicate() ? Specification.where(result).and(specs.get(i)) : Specification.where(result).or(specs.get(i));
        }
        return result;
    }
}
