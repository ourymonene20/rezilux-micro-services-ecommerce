package com.rezilux.dinngdonngecommerceapi.repository;


import com.rezilux.dinngdonngecommerceapi.entities.Command;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long>, JpaSpecificationExecutor<Command> {
    Optional<Command> findDistinctByCode(String code);

  /*  @Query("SELECT c FROM Command c WHERE c.code = :code or c.status = :status or c.client.lastName = :lastName or c.client.firstName = :firstName")
    Page<Command> searchCommandByParam(@Param("code") String code, @Param("status") int status, @Param("lastName") String lastName, @Param("firstName") String firstName, Pageable pageable);
*/
    /*List<Command> findAllByClientId(Long id);*/
}
