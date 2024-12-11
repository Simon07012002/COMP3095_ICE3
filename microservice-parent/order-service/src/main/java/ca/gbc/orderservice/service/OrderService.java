package ca.gbc.orderservice.service;

import ca.gbc.orderservice.dto.OrderRequest;
public interface OrderService {

    boolean placeOrder(OrderRequest orderRequest);

}
