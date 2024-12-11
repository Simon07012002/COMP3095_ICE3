package ca.gbc.apigateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
@Slf4j

public class Routes {


    @Value("${services.product-url}")
    private String productServiceUrl;

    @Value("${services.order-url}")
    private String orderServiceUrl;

    @Value("${services.inventory-url}")
    private String inventoryServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoutes(){


        log.info("Initializing product service route with URL: {} ", productServiceUrl);

        return route("product-service")
                .route(RequestPredicates.path("/api/product"), request-> {

                    log.info("Recieved request for product-service: {}", request.uri());

                      return HandlerFunctions.http(productServiceUrl).handle(request);
                })
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("productServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> orderServiceRoutes(){

        //print
        log.info("Initializing order service route with URL: {} ", orderServiceUrl);

        return route("order-service")
                .route(RequestPredicates.path("/api/order"), request-> {

                    log.info("Recieved request for order-service: {}", request.uri());

                    return HandlerFunctions.http(orderServiceUrl).handle(request);
                })
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("orderServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoutes(){

        //print
        log.info("Initializing inventory service route with URL: {} ", inventoryServiceUrl);

        return route("inventory-service")
                .route(RequestPredicates.path("/api/inventory"), request-> {

                    log.info("Recieved request for inventory-service: {}", request.uri());

                    return HandlerFunctions.http(inventoryServiceUrl).handle(request);
                })
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("inventoryServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute() {
        return route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
                        HandlerFunctions.http("http://product-service:8084/api-docs"))
                .filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("ProductServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
        return route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                        HandlerFunctions.http("http://order-service:8082/api-docs"))
                .filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("OrderServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {
        return route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
                        HandlerFunctions.http("http://inventory-service:8083/api-docs"))
                .filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("InventoryServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }



    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .route(RequestPredicates.all()
                                .and(request -> !request.path().contains("/swagger-ui"))
                                .and(request -> !request.path().contains("/api-docs")),
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Service is Temporarily Unavailable, please try again later"))
                .build();
    }


}