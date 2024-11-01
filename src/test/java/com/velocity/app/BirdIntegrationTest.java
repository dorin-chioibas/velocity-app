package com.velocity.app;

import com.velocity.app.dto.response.BirdResponseDto;
import com.velocity.app.repository.BirdRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql({"classpath:init/01-cleanup.sql", "classpath:init/02-create-table-test.sql", "classpath:init/03-add-data-test.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BirdIntegrationTest {

    @LocalServerPort
    private Integer port;


    String path;

    @Autowired
    private BirdRepository birdRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        path = "http://localhost:" + port + "/api/v1";
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldGetAllBirds() {

        var birdResponseDtoList = given()
                .contentType(ContentType.JSON)
                .when()
                .get(path + "/birds")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", BirdResponseDto.class);

        assertThat(birdResponseDtoList, hasSize(3));
        assertThat(birdResponseDtoList, hasItem(allOf(hasProperty("id", is(1L)), hasProperty("name", is("Vrabie")), hasProperty("color", is("Maro")), hasProperty("weight", is(500.0)), hasProperty("height", is(10.0)))));
        assertThat(birdResponseDtoList, hasItem(allOf(hasProperty("id", is(2L)), hasProperty("name", is("Cocosar")), hasProperty("color", is("Rosu")), hasProperty("weight", is(2000.0)), hasProperty("height", is(20.0)))));
        assertThat(birdResponseDtoList, hasItem(allOf(hasProperty("id", is(3L)), hasProperty("name", is("Gaita")), hasProperty("color", is("Albastru")), hasProperty("weight", is(1000.0)), hasProperty("height", is(35.0)))));

    }

    @Test
    void shouldGetBirdById() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(path + "/birds/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Vrabie"));

    }

    @Test
    void shouldCreateBird() {
        String newBirdJson = "{ \"name\": \"Canar\", \"color\": \"Galben\", \"weight\": 501, \"height\": 201 }";
        var savedBirdResponse = given()
                .contentType(ContentType.JSON)
                .body(newBirdJson)
                .when()
                .post(path + "/birds");

        savedBirdResponse.then()
                .statusCode(201)
                .body("name", equalTo("Canar"))
                .body("color", equalTo("Galben"))
                .body("weight", equalTo(501F))
                .body("height", equalTo(201F));


        var savedBird = savedBirdResponse
                .jsonPath()
                .getObject(".", BirdResponseDto.class);


        birdRepository.findById(savedBird.getId()).ifPresent(createdBird -> {
            assertThat(createdBird.getName(), equalTo(savedBird.getName()));
            assertThat(createdBird.getColor(), equalTo(savedBird.getColor()));
        });
    }

    @Test
    void shouldUpdateBird() {
        String updatedBirdJson = "{ \"name\": \"Vultur\", \"color\": \"Gri\", \"weight\": 450, \"height\": 110 }";

        var updatedBirdResponse = given()
                .contentType(ContentType.JSON)
                .body(updatedBirdJson)
                .when()
                .put(path + "/birds/1");

        var updatedBird = updatedBirdResponse
                .jsonPath()
                .getObject(".", BirdResponseDto.class);

        updatedBirdResponse
                .then()
                .statusCode(200)
                .body("name", equalTo("Vultur"))
                .body("color", equalTo("Gri"))
                .body("weight", equalTo(450F))
                .body("height", equalTo(110F));

        birdRepository.findById(updatedBird.getId()).ifPresent(createdBird -> {
            assertThat(createdBird.getName(), equalTo(updatedBird.getName()));
            assertThat(createdBird.getColor(), equalTo(updatedBird.getColor()));
        });
    }

    @Test
    void shouldDeleteBird() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(path + "/birds/1")
                .then()
                .statusCode(204);

        assertTrue(birdRepository.findById(1L).isEmpty());
    }

    @Test
    void shouldSearchBirdsByName() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("name", "Vrabie")
                .when()
                .get(path + "/birds/search")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].name", equalTo("Vrabie"));
    }

    @Test
    void shouldSearchBirdsByColor() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("color", "Rosu")
                .when()
                .get(path + "/birds/search")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].color", equalTo("Rosu"));
    }

    @Test
    void shouldSearchBirdsByNameAndColor() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("name", "Gaita")
                .queryParam("color", "Albastru")
                .when()
                .get(path + "/birds/search")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].name", equalTo("Gaita"))
                .body("[0].color", equalTo("Albastru"));
    }

    @Test
    void shouldReturnAllBirdsNoQueryParams() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(path + "/birds/search")
                .then()
                .statusCode(200)
                .body("", hasSize(3));
    }

    @Test
    void shouldReturnNotFoundForInvalidBirdId() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(path + "/birds/999")
                .then()
                .statusCode(404)
                .body("message", equalTo("Entity not found in the database"));
    }

    @Test
    void shouldReturnNotFoundForUpdateInvalidBirdId() {
        String updatedBirdJson = "{ \"name\": \"Vultur\", \"color\": \"Maro\", \"weight\": 450, \"height\": 100 }";

        given()
                .contentType(ContentType.JSON)
                .body(updatedBirdJson)
                .when()
                .put(path + "/birds/999")
                .then()
                .statusCode(404)
                .body("message", equalTo("Entity not found in the database"));
    }
}
