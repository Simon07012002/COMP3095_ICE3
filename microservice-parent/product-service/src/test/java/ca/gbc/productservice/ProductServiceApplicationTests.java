package ca.gbc.productservice;


import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ProductServiceApplicationTests {


    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void createProductTest() {

        String requestBody = """
        {
        "name" : "Samsung TV",
        "description": "Samsung TV - Model 2024",
        "price" : "2000" 
        }
        
        """;
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue() )
                .body("name", Matchers.equalTo("Samsung TV"))
                .body("description", Matchers.equalTo("Samsung TV - Model 2024")).
                body("price", Matchers.equalTo(2000));


    }

    @Test
    void getAllProductsTest() {
        String requestBody = """
            {
            "name" : "Samsung TV",
            "description": "Samsung TV - Model 2024",
            "price" : "2000"
            }
                    
            """;
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("Samsung TV"))
                .body("description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("price", Matchers.equalTo(2000));

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].name", Matchers.equalTo("Samsung TV"))
                .body("[0].description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("[0].price", Matchers.equalTo(2000));
    }



}
