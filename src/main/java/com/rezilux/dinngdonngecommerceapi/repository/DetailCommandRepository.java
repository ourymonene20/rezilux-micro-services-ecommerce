package com.rezilux.dinngdonngecommerceapi.repository;


import com.rezilux.dinngdonngecommerceapi.entities.DetailCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailCommandRepository extends JpaRepository<DetailCommand, Long> {
}
