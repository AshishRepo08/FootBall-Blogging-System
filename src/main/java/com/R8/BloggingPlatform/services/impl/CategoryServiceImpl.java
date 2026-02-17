package com.R8.BloggingPlatform.services.impl;

import com.R8.BloggingPlatform.domain.entites.Category;
import com.R8.BloggingPlatform.repositories.CategoryRepository;
import com.R8.BloggingPlatform.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        String categoryName = category.getName();
        if(categoryRepository.existsByNameIgnoreCase(categoryName)) {
            throw new IllegalArgumentException("Category already exists with name: "+categoryName);
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        //Delete only the categories with no post
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            if(category.get().getPosts().size()>0){
                throw new IllegalStateException("Category has posts associated with it.");
            } else {
                //If no posts are associated with a category, its safe to delete it.
                categoryRepository.deleteById(id);
            }
        }

    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id" +id));
    }
}
