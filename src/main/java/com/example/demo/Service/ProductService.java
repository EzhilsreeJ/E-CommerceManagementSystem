package com.example.demo.Service;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.OrdersRepository;
import com.example.demo.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrdersRepository ordersRepository;
    private final CategoryRepository categoryRepository;

    // Constructor Injection
    public ProductService(ProductRepository productRepository, CartRepository cartRepository, OrdersRepository ordersRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.ordersRepository = ordersRepository;
        this.categoryRepository = categoryRepository;
    }

    // CREATE
    public Product saveProduct(Product product) {

        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository
                    .findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            product.setCategory(category);
        }

        return productRepository.save(product);
    }



    // READ ALL
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // READ BY ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // UPDATE
    public Product updateProduct(Long id, Product product) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(product.getName());
        existing.setBrand(product.getBrand());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());
        existing.setSku(product.getSku());
        existing.setIsActive(product.getIsActive());
        existing.setRatingAvg(product.getRatingAvg());      // ✅ ADD
        existing.setRatingCount(product.getRatingCount()); // ✅ ADD

        // Load full category
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository
                    .findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existing.setCategory(category);
        }

        return productRepository.save(existing);
    }




    // DELETE
    @Transactional
    public void deleteProduct(Long id) {
        ordersRepository.deleteByUsers_Id(id);
        cartRepository.deleteByProduct_Id(id); // FIRST delete cart rows
        productRepository.deleteById(id);      // THEN delete product
    }
}
