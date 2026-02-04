package com.example.demo.Service;

import com.example.demo.Model.Orders;
import com.example.demo.Model.Product;
import com.example.demo.Model.Review;
import com.example.demo.Model.Users;
import com.example.demo.Repository.OrdersRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.ReviewRepository;
import com.example.demo.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         UsersRepository usersRepository,
                         ProductRepository productRepository,
                         OrdersRepository ordersRepository) {
        this.reviewRepository = reviewRepository;
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
        this.ordersRepository = ordersRepository;
    }

    // CREATE
    public Review addReview(Review review) {

        Long orderId = review.getOrder().getId();

        // 🔴 Prevent duplicate per order
        Review existing = reviewRepository.findByOrder_Id(orderId).orElse(null);

        if (existing != null) {
            throw new RuntimeException("Review already exists for this order");
        }

        Users user = usersRepository.findById(review.getUsers().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(review.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Orders orderEntity = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        review.setUsers(user);
        review.setProduct(product);
        review.setOrder(orderEntity);

        return reviewRepository.save(review);
    }


    // UPDATE
    public Review addOrUpdateReview(Review review) {

        Long userId = review.getUsers().getId();
        Long productId = review.getProduct().getId();

        Review existing = reviewRepository
                .findByUsers_IdAndProduct_Id(userId, productId)
                .orElse(null);

        if (existing != null) {
            existing.setRating(review.getRating());
            existing.setComment(review.getComment());
            return reviewRepository.save(existing);
        }

        return addReview(review); // reuse logic
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
