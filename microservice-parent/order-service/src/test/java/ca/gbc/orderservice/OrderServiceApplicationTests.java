package ca.gbc.orderservice;

import ca.gbc.orderservice.client.InventoryClient;
import ca.gbc.orderservice.repository.OrderRepository;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class OrderServiceApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_order_db")
            .withUsername("testuser")
            .withPassword("testpassword");

    @LocalServerPort
    private Integer port;

    @MockBean
    private InventoryClient inventoryClient;

    @Autowired
    private OrderRepository orderRepository;

    @DynamicPropertySource
    static void setDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgresSQLContainer::getPassword);
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;


        Mockito.when(inventoryClient.isInStock(anyString(), anyInt())).thenReturn(true);
    }

    @Test
    void shouldSubmitOrder() {
        String submitOrderJson = """
        {
            "skuCode": "samsung_tv_2024",
            "price": 5000,
            "quantity": 10
        }
        """;

        var responseBodyString = given()
                .contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();

        assertThat(responseBodyString, Matchers.is("Order placed successfully"));

    }
}
