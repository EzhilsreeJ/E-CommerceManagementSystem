package com.example.demo.Controller;

import com.example.demo.Model.Category;
import com.example.demo.Service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // Constructor injection (recommended)
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // CREATE
    @PostMapping("/add")
    public Category addCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    // READ ALL
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id,
                                   @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }



    // DELETE
    @DeleteMapping("/{id}")
    public String deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "Category Deleted Successfully!";
    }
}
