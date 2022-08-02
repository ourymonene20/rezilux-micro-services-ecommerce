package com.rezilux.dinngdonngecommerceapi.web;

import com.rezilux.dinngdonngecommerceapi.entities.Article;
import com.rezilux.dinngdonngecommerceapi.repository.ArticleRepository;
import com.rezilux.dinngdonngecommerceapi.services.ArticleService;
import com.rezilux.dinngdonngecommerceapi.specification.CustomSpecificationBuilder;
import com.rezilux.dinngdonngecommerceapi.web.exceptions.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    public final ArticleService articleService;
    public final ArticleRepository articleRepository;

    public ArticleController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    /**
     * {@code POST  /articles} : Create a new article.
     *
     * @param article the article to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new article, or with status {@code 400 (Bad Request)} if the article has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws Exception if the file upload process failed or the user is not a merchant.
     */
    @PostMapping
    public ResponseEntity<Article> createArticle(@Valid @RequestBody Article article) throws URISyntaxException, Exception {
        if (article.getId() != null) {
            throw new BadRequestAlertException("A new article cannot already have an ID", "Article", "idexists");
        }
        Article result = articleService.create(article);
        return ResponseEntity.created(new URI("/api/articles/" + result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /articles} : Updates an existing article.
     *
     * @param article the article to update.
     * @param id the id of the article to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated article,
     * or with status {@code 400 (Bad Request)} if the article is not valid,
     * or with status {@code 500 (Internal Server Error)} if the article couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws Exception if the the file upload process failed.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@Valid @RequestBody Article article, @PathVariable("id") Long id) throws URISyntaxException, Exception {
        logger.debug("REST request to update Article : {}", article);
        if (article.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "Article", "idnull");
        }
        Article result = articleService.update(article, id);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * {@code GET  /articles} : get all the articles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articles in body.
     */
    @GetMapping
    public ResponseEntity<Page<Article>> getAllArticles(Pageable pageable) {
        logger.debug("REST request to get a page of Articles");
        Page<Article> page = articleService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * {@code GET  /articles/:id} : get the "id" article.
     *
     * @param id the id of the article to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the article, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {
        logger.debug("REST request to get Article : {}", id);
        Optional<Article> article = articleService.findOne(id);
        return ResponseEntity.of(article);
    }

    /**
     * {@code DELETE  /articles/:id} : delete the "id" article.
     *
     * @param id the id of the article to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     * @throws Exception if the id of the article is incorrect.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) throws Exception {
        logger.debug("REST request to delete Article : {}", id);
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * {@code GET  /users/_search} : search for the users.
     *
     * @param pageable the pagination information.
     * @param search the search criteria.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of users in body.
     */
    @GetMapping("/_search")
    public ResponseEntity<Page<Article>> _search(@RequestParam(value = "search", required = false) String search, Pageable pageable) {
        logger.debug("REST request to get a page of Articles");
        CustomSpecificationBuilder<Article> builder = new CustomSpecificationBuilder<Article>();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        Specification<Article> spec = builder.build();
        Page<Article> page = articleService.findAll(spec, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/_research")
    public ResponseEntity<Page<Article>> searchArticlesByCategoryId(@RequestParam Long categoryId, Pageable pageable) {
        logger.debug("REST request to get all Articles associated with category");
        Page<Article> page = articleService.findArticleByCategory(categoryId, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/export-excel-article")
    public ResponseEntity<InputStreamResource> exportArticleExcel() throws Exception{
        List<Article> articles = articleRepository.findAll();
        ByteArrayInputStream byteArrayInputStream = articleService.articleExcelReport(articles);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=articles.xlsx");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(byteArrayInputStream));
    }
}
