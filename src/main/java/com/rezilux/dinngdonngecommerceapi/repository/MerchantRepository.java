package com.rezilux.dinngdonngecommerceapi.repository;

import com.rezilux.dinngdonngecommerceapi.entities.Merchant;
//import com.rezilux.yupay.entities.auth.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long>, JpaSpecificationExecutor<Merchant> {
    Optional<Merchant> findDistinctByCode(String code);

    //Optional<Merchant> findFirstByUserAndActiveTrue(User user);
}
