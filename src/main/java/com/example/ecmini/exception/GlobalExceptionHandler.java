package com.example.ecmini.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockException.class)
    public String handleStockException(
            StockException e,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/cart";
    }

    @ExceptionHandler(OrderException.class)
    public String handleOrderException(
            OrderException e,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/cart";
    }

    @ExceptionHandler(PaymentException.class)
    public String handlePaymentException(
            PaymentException e,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/cart";
    }


}