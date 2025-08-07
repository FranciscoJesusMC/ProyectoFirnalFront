package com.ecommerce.service;

import com.ecommerce.entity.OrderProduct;
import jakarta.mail.MessagingException;

import java.util.List;

public interface EmailService {

    void sendOrderConfirmation(String to, List<OrderProduct> orderProducts, double total,String orderDate) throws MessagingException;
}
