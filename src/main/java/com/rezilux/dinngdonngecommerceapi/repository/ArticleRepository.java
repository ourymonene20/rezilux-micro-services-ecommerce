package com.rezilux.dinngdonngecommerceapi.repository;

import com.rezilux.dinngdonngecommerceapi.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    Page<Article> findAllByMerchantId(Long merchantId, Pageable pageable);

    @Query("select a from Article a where a.category.id=:idC")
    Page<Article> findArtilceByCategoryId(@Param("idC") Long categoryId, Pageable pageable);
}
