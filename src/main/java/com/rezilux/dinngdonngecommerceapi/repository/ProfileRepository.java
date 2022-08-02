package com.rezilux.dinngdonngecommerceapi.repository;

import com.rezilux.dinngdonngecommerceapi.entities.Profile;
import com.rezilux.dinngdonngecommerceapi.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findDistinctByName(String name);
    //Optional<Profile> getByUsers();

    @Query("select u from User u join u.profiles p where p.id=:profilId")
    Page<User> findAllProfileByUsers(@Param("profilId") Long idP, Pageable pageable);
}
