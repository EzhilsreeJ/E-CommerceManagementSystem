package com.example.demo.Service;

import com.example.demo.Handler.BadRequestException;
import com.example.demo.Model.Cart;
import com.example.demo.Model.Product;
import com.example.demo.Model.Users;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;
    // Constructor injection
    public CartService(CartRepository cartRepository,ProductRepository productRepository,UsersRepository usersRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.usersRepository = usersRepository;
    }

    // CREATE
    public Cart addToCart(Cart cart) {

        if (cart.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        Long userId = cart.getUsers().getId();
        Long productId = cart.getProduct().getId();

        // ✅ LOAD FULL USER
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ LOAD FULL PRODUCT
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Cart> existingCart =
                cartRepository.findByUsers_IdAndProduct_Id(userId, productId);

        if (existingCart.isPresent()) {
            Cart oldCart = existingCart.get();
            oldCart.setQuantity(oldCart.getQuantity() + cart.getQuantity());
            oldCart.setUpdatedAt(LocalDateTime.now());
            return cartRepository.save(oldCart);
        }

        // 🆕 new insert
        cart.setUsers(user);          // ✅ FULL USER
        cart.setProduct(product);    // ✅ FULL PRODUCT
        cart.setPriceAtAdded(product.getPrice());

        return cartRepository.save(cart);
    }




    // READ ALL
    public List<Cart> getAllCartItems() {
        return cartRepository.findAll();
    }

    // READ BY ID
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));
    }

    // UPDATE
    public Cart updateCart(Cart cart) {

        Cart existingCart = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (cart.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        // DO NOT change user & product (avoids duplicate key error)
        existingCart.setQuantity(cart.getQuantity());
        existingCart.setPriceAtAdded(cart.getPriceAtAdded());

        existingCart.setUpdatedAt(LocalDateTime.now());

        return cartRepository.save(existingCart);
    }




    // DELETE
    public void removeFromCart(Long id) {
        cartRepository.deleteById(id);
    }
}
