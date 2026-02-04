package com.example.demo.Service;

import com.example.demo.Handler.ResourceNotFoundException;
import com.example.demo.Model.Category;
import com.example.demo.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Constructor injection
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // CREATE
    public Category saveCategory(Category category) {
        if (category.getParent() != null && category.getParent().getId() != 0) {
            Category parent = categoryRepository.findById(category.getParent().getId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }
        return categoryRepository.save(category);
    }
    // READ ALL
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // READ BY ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    // UPDATE
    public Category updateCategory(Long id, Category category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setActive(category.isActive());

        if (category.getParent() != null && category.getParent().getId() != 0) {
            Category parent = categoryRepository.findById(category.getParent().getId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            existing.setParent(parent);
        } else {
            existing.setParent(null);
        }

        return categoryRepository.save(existing);
    }

    // DELETE
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
