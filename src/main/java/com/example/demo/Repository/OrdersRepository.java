package com.example.demo.Repository;

import com.example.demo.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
    void deleteByUsers_Id(Long usersId);

    Optional<Object> findByOrderGroupId(String orderGroupId);
}
