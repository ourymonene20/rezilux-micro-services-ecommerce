package com.rezilux.dinngdonngecommerceapi.services;

import com.rezilux.dinngdonngecommerceapi.entities.Category;
import com.rezilux.dinngdonngecommerceapi.repository.CategoryRepository;
import com.rezilux.dinngdonngecommerceapi.utils.FileUploadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;

    public CategoryService(CategoryRepository categoryRepository, FileUploadService fileUploadService) {
        this.categoryRepository = categoryRepository;
        this.fileUploadService = fileUploadService;
    }

    public Category create(Category category) throws Exception {
        String extension;
        String imagePath = "";
        if (category.getImage() != null && category.getImage().length() > 0) {
            extension = (category.getImage().split("base64,")[0]).split("/")[1];
            String contentBase64 = "";
            for (String part: category.getImage().split("base64,")) {
                if (!part.startsWith("data:image")) contentBase64 = part;
            }
            System.out.println(contentBase64);
            imagePath = fileUploadService.upload("images", contentBase64, extension.split(";")[0]);
            if (imagePath.length() == 0 && contentBase64.length() > 0) throw new Exception("Echec de l'upload de l'image");
        }
        category.setImage(imagePath);
        return categoryRepository.save(category);
    }

    public Category update(Category category, Long id) throws Exception{
        String extension;
        String imagePath = "";
        if (category.getImage().length() > 0) {
            extension = (category.getImage().split("base64,")[0]).split("/")[1];
            String contentBase64 = "";
            for (String part: category.getImage().split("base64,")) {
                if (!part.startsWith("data:image")) contentBase64 = part;
            }
            System.out.println(contentBase64);
            imagePath = fileUploadService.upload("images", contentBase64, extension.split(";")[0]);
            category.setImage(imagePath);
            if (imagePath.length() == 0 && contentBase64.length() > 0) throw new Exception("Echec de l'upload de l'image");
        }
        return categoryRepository.findById(id).map(c -> {
            c.setName(category.getName());
            c.setDescription(category.getDescription());
            c.setImage(category.getImage());

            return categoryRepository.save(c);
        }).orElse(null);
    }

    public Optional<Category> findOne(Long id) {
        return categoryRepository.findById(id);
    }

    public void delete(Long id) throws Exception {
        categoryRepository.delete(findOne(id).orElseThrow(() -> new Exception("Category not found")));
    }

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Page<Category> findAll(Specification<Category> spec, Pageable pageable) {
        return categoryRepository.findAll(spec, pageable);
    }
}
