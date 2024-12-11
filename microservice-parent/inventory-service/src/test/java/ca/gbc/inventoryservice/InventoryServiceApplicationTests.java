package ca.gbc.inventoryservice;

import ca.gbc.inventoryservice.model.Inventory;
import ca.gbc.inventoryservice.repository.InventoryRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class InventoryServiceApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgresDBContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_inventory_db")
            .withUsername("testuser")
            .withPassword("testpassword");

    @LocalServerPort
    private Integer port;

    @Autowired
    private InventoryRepository inventoryRepository;

    @DynamicPropertySource
    static void setDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDBContainer::getUsername);
        registry.add("spring.datasource.password", postgresDBContainer::getPassword);
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        if (!inventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual("sample_sku", 10)) {
            Inventory inventoryItem = new Inventory(null, "sample_sku", 20);
            inventoryRepository.save(inventoryItem);
        }
    }

    @Test
    void shouldReturnTrueWhenInStock() {
        given()
                .contentType("application/json")
                .queryParam("skuCode", "sample_sku")
                .queryParam("quantity", 10)
                .when()
                .get("/api/inventory")
                .then()
                .log().all()
                .statusCode(200)
                .body(equalTo("true"));
    }

    @Test
    void shouldReturnFalseWhenNotInStock() {
        given()
                .contentType("application/json")
                .queryParam("skuCode", "sample_sku")
                .queryParam("quantity", 30)
                .when()
                .get("/api/inventory")
                .then()
                .log().all()
                .statusCode(200)
                .body(equalTo("false"));
    }
}
