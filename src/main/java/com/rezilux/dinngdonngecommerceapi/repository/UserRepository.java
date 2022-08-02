package com.rezilux.dinngdonngecommerceapi.repository;


import com.rezilux.dinngdonngecommerceapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("select u from User u where ((u.login=:login or u.email=:login) and u.active=:active) and u.creationStep = 1")
    Optional<User> connexion(@Param("login") String login, @Param("active") Boolean active);

    @Query("SELECT u FROM User u WHERE u.lastName = :lastName or u.firstName = :firstName or u.phone = :phone or u.email = :email")
    Page<User> searchByUser(@Param("lastName") String lastName, @Param("firstName") String firstName, @Param("phone") String phone, @Param("email") String email, Pageable pageable);

    Page<User> findAllByActive(boolean active, Pageable pageable);

    Optional<User> findFirstByPhoneAndCreationStep(String phone, int step);

    Optional<User> findFirstByEmailAndCreationStep(String telephone, int step);

    Optional<User> findFirstByIdentification(String identification);

  //  Optional<User> getByUser();


}
