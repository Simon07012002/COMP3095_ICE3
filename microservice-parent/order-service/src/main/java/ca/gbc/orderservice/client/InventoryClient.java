package ca.gbc.orderservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface InventoryClient {

    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @GetExchange("/api/inventory")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    boolean isInStock(@RequestParam("skuCode") String skuCode, @RequestParam("quantity") Integer quantity);

    default boolean fallbackMethod(String skuCode, Integer quantity, Throwable throwable) {
        log.error("Fallback triggered for InventoryClient. SKU: {}, Quantity: {}, Reason: {}",
                skuCode, quantity, throwable.getMessage());
        throw new RuntimeException("Service is Temporarily Unavailable, please try again later");
    }
}
