package org.example.testrestassured.specifications;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class ResponseSpec {
    public static ResponseSpecification withStatusCode(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectContentType(JSON)
                .log(ALL)
                .build();
    }

    public static ResponseSpecification successResponse() {
        return withStatusCode(200);
    }

    public static ResponseSpecification notFoundResponse() {
        return withStatusCode(404);
    }

    public static ResponseSpecification badRequestResponse() {
        return withStatusCode(400);
    }

    public static ResponseSpecification createdResponse() {
        return withStatusCode(201);
    }
}