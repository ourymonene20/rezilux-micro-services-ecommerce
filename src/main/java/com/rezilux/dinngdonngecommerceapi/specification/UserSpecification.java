/*
package com.rezilux.dinngdonngecommerceapi.specification;

import com.rezilux.yupay.entities.auth.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {

    private User criteria;

    public UserSpecification(User criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (this.criteria.getId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("id"), this.criteria.getId()));
        }
        if ((this.criteria.getLastName() != null) && (!this.criteria.getLastName().equals(""))) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + this.criteria.getLastName().toLowerCase() + "%"));
        }
        if ((this.criteria.getFirstName() != null) && (!this.criteria.getFirstName().equals(""))) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + this.criteria.getFirstName().toLowerCase() + "%"));
        }
        if ((this.criteria.getPhone() != null) && (!this.criteria.getPhone().equals(""))) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + this.criteria.getPhone().toLowerCase() + "%"));
        }
        if ((this.criteria.getEmail() != null) && (!this.criteria.getEmail().equals(""))) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + this.criteria.getEmail().toLowerCase() + "%"));
        }
        */
/*if (this.criteria.getCreatedDate() != null) {
            predicates.add(criteriaBuilder.equal(root.<Instant>get("createdDate"), this.criteria.getCreatedDate()));

        }*//*

        */
/*if ((this.criteria.getProfiles() != null) && (!this.criteria.getProfiles().equals(""))) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("profiles")), "%" + this.criteria.getProfiles() + "%"));
        }*//*


        if (predicates.size() > 0) {
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }

        return null;
    }
}


*/
/*
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, 2005);
				cal.set(Calendar.MONTH, Calendar.APRIL);
				cal.set(Calendar.DATE, 8);
				cal.set(Calendar.HOUR_OF_DAY, 0);
		        cal.set(Calendar.MINUTE, 0);
		        cal.set(Calendar.SECOND, 0);
		        cal.set(Calendar.MILLISECOND, 0);
		        java.util.Date myDate = cal.getTime();
		        System.out.println("myDate: " + myDate);

				predicates.add(criteriaBuilder.equal(root.<Date>get("birthDate"), criteriaBuilder.literal(myDate)));
*/
