package com.ecommerce.serviceImpl;

import com.ecommerce.dto.request.OrderProductRequestDTO;
import com.ecommerce.dto.request.OrderRequestDTO;
import com.ecommerce.dto.response.OrderResponseDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderProduct;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.OrderMapper;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.OrderService;
import com.ecommerce.utils.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()){
            throw new ResourceNotFoundException("List of orders is empty");
        }
        return orders.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found with username :"+ username));
        List<Order> orders = orderRepository.findByUserId(user.getId());
        if (orders.isEmpty()){
            throw new ResourceNotFoundException("List of orders is empty");
        }
        return orders.stream().map(mapper::toDTO).toList();
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order orderResponseDTO = orderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not found with id :" + id));
        return mapper.toDTO(orderResponseDTO);
    }

    @Transactional
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User not found with username:" + username));

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(RequestStatus.PENDING);

        double total = 0.0;
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (OrderProductRequestDTO item : orderRequestDTO.getOrderProductRequestDTOS()) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(() ->
                    new ResourceNotFoundException("Product not found with id: " + item.getProductId()));

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setOrder(order);
            orderProduct.setQuantity(item.getQuantity());
            orderProduct.setSubtotal(product.getPrice() * item.getQuantity());

            orderProducts.add(orderProduct);
            total += orderProduct.getSubtotal();
        }

        order.setOrderProducts(orderProducts);
        order.setTotal(total);
        order = orderRepository.save(order);

        log.info("Order created with ID: {}, total: {}", order.getId(), order.getTotal());

        return mapper.toDTO(order);
    }


    @Override
    public OrderResponseDTO cancelOrder(Long id) {
        log.info("Attempt to cancel order with id : {}", id);

        Order order = orderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not found with id:" + id));

        if(!order.getStatus().equals(RequestStatus.PENDING)){
            log.warn("Order with id : {} can not be canceled because it is in status : {}",id,order.getStatus());
            throw new IllegalStateException("Only pending orders can be canceled");
        }

        for(OrderProduct orderProduct : order.getOrderProducts()){
            Product product = orderProduct.getProduct();
            product.setStock(product.getStock() + orderProduct.getQuantity());
            productRepository.save(product);
            log.info("Restored stock for product {} - new stock : {}",product.getName(),product.getStock());

        }

        order.setStatus(RequestStatus.CANCELED);
        orderRepository.save(order);
        log.info("Order with id : {} has been canceled",order.getId());

        return mapper.toDTO(order);
    }

    @Override
    public void deleteOrder(Long id) {

    }
}
