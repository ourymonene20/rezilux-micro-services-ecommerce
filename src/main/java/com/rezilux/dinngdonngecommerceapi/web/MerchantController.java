package com.rezilux.dinngdonngecommerceapi.web;

import com.rezilux.dinngdonngecommerceapi.entities.Article;
import com.rezilux.dinngdonngecommerceapi.entities.Merchant;
import com.rezilux.dinngdonngecommerceapi.services.ArticleService;
import com.rezilux.dinngdonngecommerceapi.services.MerchantService;
import com.rezilux.dinngdonngecommerceapi.specification.CustomSpecificationBuilder;
import com.rezilux.dinngdonngecommerceapi.web.exceptions.BadRequestAlertException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final Logger logger = LoggerFactory.getLogger(MerchantController.class);
    private final MerchantService merchantService;
    private final ArticleService articleService;

    public MerchantController(MerchantService merchantService, ArticleService articleService) {
        this.merchantService = merchantService;
        this.articleService = articleService;
    }

    /**
     * {@code POST  /merchants} : Create a new merchant.
     *
     * @param merchant the merchant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new merchant, or with status {@code 400 (Bad Request)} if the merchant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<Merchant> createMerchant(@Valid @RequestBody Merchant merchant) throws URISyntaxException, Exception {
        if (merchant.getId() != null) {
            throw new BadRequestAlertException("A new merchant cannot already have an ID", "Merchant", "idexists");
        }
        Merchant result = merchantService.create(merchant);
        return ResponseEntity.created(new URI("/api/merchants/" + result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /merchants} : Updates an existing merchant.
     *
     * @param merchant the merchant to update.
     * @param id the id of the merchant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated merchant,
     * or with status {@code 400 (Bad Request)} if the merchant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the merchant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws Exception if the file upload process failed.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Merchant> updateMerchant(@Valid @RequestBody Merchant merchant, @PathVariable("id") Long id) throws URISyntaxException, Exception {
        logger.debug("REST request to update Merchant : {}", merchant);
        if (merchant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "Merchant", "idnull");
        }
        Merchant result = merchantService.update(merchant, id);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * {@code GET  /merchants} : get all the merchants.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of merchants in body.
     */
    @GetMapping
    public ResponseEntity<Page<Merchant>> getAllMerchants(Pageable pageable) {
        logger.debug("REST request to get a page of Merchants");
        Page<Merchant> page = merchantService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * {@code GET  /merchants/:id} : get the "id" merchant.
     *
     * @param id the id of the merchant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the merchant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Merchant> getMerchant(@PathVariable Long id) {
        logger.debug("REST request to get Merchant : {}", id);
        Optional<Merchant> merchant = merchantService.findOne(id);
        return ResponseEntity.of(merchant);
    }

    /**
     * {@code DELETE  /merchants/:id} : delete the "id" merchant.
     *
     * @param id the id of the merchant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     * @throws Exception if the id of the merchant is incorrect.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) throws Exception {
        logger.debug("REST request to delete Merchant : {}", id);
        merchantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code GET  /merchants/{id}/articles} : get all the articles for .
     *
     * @param pageable the pagination information.
     * @param id the id of the merchant that owns the articles.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articles for a merchant in body.
     */
    @GetMapping("/{id}/articles")
    public ResponseEntity<Page<Article>> getAllArticlesByMerchant(@PathVariable(name = "id") Long id, Pageable pageable) {
        logger.debug("REST request to get a page of articles for a merchant");
        Page<Article> page = articleService.findAllByMerchantId(id, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/_search")
    public ResponseEntity<Page<Merchant>> _search(@RequestParam(value = "search", required = false) String search, Pageable pageable){
        logger.debug("REST request to get a page of Merchant");
        CustomSpecificationBuilder<Merchant> builder = new CustomSpecificationBuilder<Merchant>();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Merchant> spec = builder.build();
        Page<Merchant> page = merchantService.findAll(spec, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
