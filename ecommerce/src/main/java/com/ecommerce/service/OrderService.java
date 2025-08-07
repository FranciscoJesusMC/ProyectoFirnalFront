package com.ecommerce.service;

import com.ecommerce.dto.request.OrderRequestDTO;
import com.ecommerce.dto.response.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    List<OrderResponseDTO> getAllOrders();

    List<OrderResponseDTO> getOrdersByUsername(String username);

    OrderResponseDTO getOrderById(Long id);

    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO,String username);

    OrderResponseDTO cancelOrder(Long id);

    void deleteOrder(Long id);
}
