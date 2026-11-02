package org.example.testrestassured.tests;

import org.example.testrestassured.utils.SpecBuilder;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.example.testrestassured.utils.ApiConstants.ENDPOINT_USERS_ME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserTests extends BaseApiTest {

    @Test(description = "Получение данных текущего пользователя по токену")
    public void testGetCurrentUser() {
        given()
                .spec(reqSpec)
                .when()
                .get(ENDPOINT_USERS_ME)
                .then()
                .spec(SpecBuilder.getResponseSpec(200))
                .body("$type", equalTo("Me"))
                .body("id", notNullValue());
    }
}