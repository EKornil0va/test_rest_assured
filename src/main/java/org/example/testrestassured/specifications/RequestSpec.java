package org.example.testrestassured.specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.config;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.filter.log.LogDetail.ALL;
import static org.example.testrestassured.utils.ApiConstants.API_TOKEN;
import static org.example.testrestassured.utils.ApiConstants.BASE_URL;

public class RequestSpec {
    public static RequestSpecification baseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addHeader("Authorization", "Bearer " + API_TOKEN)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .log(ALL)
                .setConfig(config().redirect(redirectConfig().followRedirects(false)))
                .build();
    }

    public static RequestSpecification projectRequestSpec() {
        return baseRequestSpec();
    }

    public static RequestSpecification userRequestSpec() {
        return baseRequestSpec();
    }
}