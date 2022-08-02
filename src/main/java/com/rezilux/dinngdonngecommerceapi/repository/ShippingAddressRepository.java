package com.rezilux.dinngdonngecommerceapi.repository;

//import com.rezilux.yupay.entities.auth.User;

import com.rezilux.dinngdonngecommerceapi.entities.ShippingAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
    //List<ShippingAddress> findAllByUser(User user);
    //Page<ShippingAddress> findAllByUser(User user, Pageable pageable);
}
