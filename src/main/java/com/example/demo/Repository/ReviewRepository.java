package com.example.demo.Repository;

import com.example.demo.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    void deleteByOrder_Users_Id(Long userId);
    Optional<Review> findByUsers_IdAndProduct_Id(Long userId, Long productId);
    Optional<Review> findByOrder_Id(Long orderId);


}
