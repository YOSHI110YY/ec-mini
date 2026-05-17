package com.example.ecmini.repository;

import com.example.ecmini.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUsername(String username);

    Optional<Order> findByIdAndUsername(Long id, String username);

    List<Order> findAllByOrderByCreatedAtDesc();

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE DATE(o.createdAt) = :date")
    Optional<Integer> sumTotalPriceByDate(LocalDate date);

    long countByStatus(String status);

    long countByCreatedAtAfter(LocalDateTime dateTime);

}
