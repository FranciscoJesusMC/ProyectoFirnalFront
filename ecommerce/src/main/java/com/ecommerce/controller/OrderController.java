package com.ecommerce.controller;

import com.ecommerce.dto.request.OrderRequestDTO;
import com.ecommerce.dto.response.OrderResponseDTO;
import com.ecommerce.serviceImpl.OrderServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class OrderController {

    private final OrderServiceImpl orderService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(){
        List<OrderResponseDTO> orderResponseDTOS = orderService.getAllOrders();
        return ResponseEntity.ok(orderResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable(name = "id")Long id){
        OrderResponseDTO orderResponseDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(Principal principal){
        List<OrderResponseDTO> orderResponseDTOS = orderService.getOrdersByUsername(principal.getName());
        return ResponseEntity.ok(orderResponseDTOS);
    }

    @PostMapping()
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO, Principal principal){
        String username = principal.getName();
        OrderResponseDTO orderResponseDTO =orderService.createOrder(orderRequestDTO,username);
        return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable(name = "id")Long id){
        OrderResponseDTO orderResponseDTO = orderService.cancelOrder(id);
        return ResponseEntity.ok(orderResponseDTO);
    }
}
