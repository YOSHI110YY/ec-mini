package com.example.ecmini.controller.admin;

import com.example.ecmini.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("todaySales",
                dashboardService.getTodaySales());

        model.addAttribute("orderCount",
                dashboardService.getOrderCount());

        model.addAttribute("outOfStockCount",
                dashboardService.getOutOfStockCount());

        model.addAttribute("newOrders",
                dashboardService.getNewOrders());

        model.addAttribute("pendingShipments",
                dashboardService.getPendingShipments());

        return "admin/dashboard/index";
    }
}