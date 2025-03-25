package com.vvelc.booking;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class BookingResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/bookings")
          .then()
             .statusCode(200)
             .body(is("Hello from Quarkus REST"));
    }

}