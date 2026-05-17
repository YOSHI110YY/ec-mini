package com.example.ecmini.service;

import com.example.ecmini.repository.OrderRepository;
import com.example.ecmini.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 今日の売上
    public int getTodaySales() {
        return orderRepository.sumTotalPriceByDate(LocalDate.now()).orElse(0);
    }

    // 未発送件数
    public long getPendingShipments() {
        return orderRepository.countByStatus("ORDERED");
    }

    // 商品数
    public long getProductCount() {
        return productRepository.count();
    }
    // 注文数
    public long getOrderCount() {
        return orderRepository.count();
    }
    // 在庫切れ件数
    public long getOutOfStockCount() {
        return productRepository.countByStockLessThanEqual(0);
    }

    // 新着注文
    public long getNewOrders() {
        return orderRepository.countByCreatedAtAfter(LocalDate.now().atStartOfDay());
    }


}
