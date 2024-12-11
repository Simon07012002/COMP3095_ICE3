package ca.gbc.orderservice.controller;

import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            boolean isOrderPlaced = orderService.placeOrder(orderRequest);

            if (isOrderPlaced) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Order placed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inventory limits reached");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }
}
