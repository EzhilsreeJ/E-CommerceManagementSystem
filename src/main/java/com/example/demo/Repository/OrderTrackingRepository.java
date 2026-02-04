package com.example.demo.Repository;

import com.example.demo.Model.OrderTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderTrackingRepository extends JpaRepository<OrderTracking,Long> {
    Optional<OrderTracking> findByOrderGroupId(String orderGroupId);
}
