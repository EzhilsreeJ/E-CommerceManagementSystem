package com.example.demo.Service;

import com.example.demo.Model.OrderTracking;
import com.example.demo.Model.Orders;
import com.example.demo.Repository.OrderTrackingRepository;
import com.example.demo.Repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTrackingService {

    private final OrderTrackingRepository orderTrackingRepository;
    private final OrdersRepository ordersRepository;

    public OrderTrackingService(OrderTrackingRepository orderTrackingRepository,
                                OrdersRepository ordersRepository) {
        this.orderTrackingRepository = orderTrackingRepository;
        this.ordersRepository = ordersRepository;
    }

    // CREATE
    public OrderTracking save(OrderTracking orderTracking) {

        String orderGroupId = orderTracking.getOrderGroupId();

        // validate order exists
        ordersRepository.findByOrderGroupId(orderGroupId)
                .orElseThrow(() -> new RuntimeException("Order not found with this orderGroupId"));

        // prevent duplicate tracking
        OrderTracking existing = orderTrackingRepository
                .findByOrderGroupId(orderGroupId)
                .orElse(null);

        if (existing != null) {
            throw new RuntimeException("Order tracking already exists for this order");
        }

        return orderTrackingRepository.save(orderTracking);
    }

    // READ ALL
    public List<OrderTracking> getAll() {
        return orderTrackingRepository.findAll();
    }

    // READ BY ID
    public OrderTracking getById(Long id) {
        return orderTrackingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderTracking not found with id: " + id));
    }

    // UPDATE
    public OrderTracking update(Long id, OrderTracking orderTracking) {

        OrderTracking existing = orderTrackingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderTracking not found"));

        String orderGroupId = orderTracking.getOrderGroupId();

        OrderTracking duplicate = orderTrackingRepository
                .findByOrderGroupId(orderGroupId)
                .orElse(null);

        if (duplicate != null && !duplicate.getId().equals(id)) {
            throw new RuntimeException("Another tracking exists with this orderGroupId");
        }

        existing.setOrderGroupId(orderGroupId);
        existing.setStatus(orderTracking.getStatus());
        existing.setDescription(orderTracking.getDescription());
        existing.setLocation(orderTracking.getLocation());
        existing.setUpdatedBy(orderTracking.getUpdatedBy());

        return orderTrackingRepository.save(existing);
    }

    // DELETE
    public void delete(Long id) {
        orderTrackingRepository.deleteById(id);
    }
}
