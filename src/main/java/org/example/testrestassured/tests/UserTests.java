package org.example.testrestassured.tests;

import org.example.testrestassured.specifications.RequestSpec;
import org.example.testrestassured.specifications.ResponseSpec;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.example.testrestassured.utils.ApiConstants.Endpoint.USERS_ME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserTests extends BaseApiTest {

    @Test(description = "Получение данных текущего пользователя по токену")
    public void testGetCurrentUser() {
        given()
                .spec(RequestSpec.userRequestSpec())
                .when()
                .get(USERS_ME.getPath())
                .then()
                .spec(ResponseSpec.successResponse())
                .body("$type", equalTo("Me"))
                .body("id", notNullValue());
    }
}