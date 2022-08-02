package com.rezilux.dinngdonngecommerceapi.web;

import com.rezilux.dinngdonngecommerceapi.entities.Category;
import com.rezilux.dinngdonngecommerceapi.services.CategoryService;
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
@RequestMapping("/api/categories")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) { this.categoryService = categoryService; }

    /**
     * {@code POST  /categories} : Create a new category.
     *
     * @param category the category to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new category, or with status {@code 400 (Bad Request)} if the category has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) throws URISyntaxException, Exception {
        if (category.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID", "Article", "idexists");
        }
        Category result = categoryService.create(category);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /categories} : Updates an existing category.
     *
     * @param category the category to update.
     * @param id the id of the category to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated category,
     * or with status {@code 400 (Bad Request)} if the category is not valid,
     * or with status {@code 500 (Internal Server Error)} if the category couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category, @PathVariable("id") Long id) throws URISyntaxException, Exception {
        logger.debug("REST request to update Category : {}", category);
        if (category.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "Category", "idnull");
        }
        Category result = categoryService.update(category, id);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * {@code GET  /categories} : get all the categories.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping
    public ResponseEntity<Page<Category>> getAllCategories(Pageable pageable) {
        logger.debug("REST request to get a page of Categories");
        Page<Category> page = categoryService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * {@code GET  /categories/:id} : get the "id" categorie.
     *
     * @param id the id of the categorie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categorie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        logger.debug("REST request to get Category : {}", id);
        Optional<Category> categorie = categoryService.findOne(id);
        return ResponseEntity.of(categorie);
    }

    /**
     * {@code DELETE  /categories/:id} : delete the "id" categorie.
     *
     * @param id the id of the categorie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     * @throws Exception if the id of the categorie is incorrect.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) throws Exception {
        logger.debug("REST request to delete Category : {}", id);
        categoryService.delete(id);
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
    public ResponseEntity<Page<Category>> _search(@RequestParam(value = "search", required = false) String search, Pageable pageable) {
        logger.debug("REST request to get a page of Category");
        CustomSpecificationBuilder<Category> builder = new CustomSpecificationBuilder<Category>();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Category> spec = builder.build();
        Page<Category> page = categoryService.findAll(spec, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
