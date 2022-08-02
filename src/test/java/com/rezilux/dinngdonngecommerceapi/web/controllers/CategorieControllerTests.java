package com.rezilux.dinngdonngecommerceapi.web.controllers;


import com.rezilux.dinngdonngecommerceapi.entities.Category;
import com.rezilux.dinngdonngecommerceapi.repository.CategoryRepository;
import com.rezilux.dinngdonngecommerceapi.services.CategoryService;
import com.rezilux.dinngdonngecommerceapi.web.CategoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategorieControllerTests {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        CategoryController categoryController = new CategoryController(categoryService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .build();
    }


    @Test
    void createCategory() throws Exception {
        Category category = new Category();
        category.setName("category-test");
        category.setImage("");
        mockMvc.perform(post("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.name").value(category.getName()));

    }

    @Test
    void getAllCategories() throws Exception {

        List<Category> categories = Arrays.asList(
                new Category("category-test-1"),
                new Category("category-test-2"),
                new Category("category-test-3")
        );

        categoryRepository.saveAll(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[*].name").value(hasItem(categories.get(0).getName())))
                .andExpect(jsonPath("$.content[*].name").value(hasItem(categories.get(1).getName())))
                .andExpect(jsonPath("$.content[*].name").value(hasItem(categories.get(2).getName())));

    }


}
