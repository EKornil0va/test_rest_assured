package org.example.testrestassured.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {
    protected static final String API_TOKEN = "perm-YWRtaW4=.NDEtMA==.6ol0xm9YIs1LjypDCjvjFp8aepKh8v";
    protected static final String BASE_URL = "http://localhost:8080";

    protected RequestSpecification reqSpec;

    @BeforeClass
    public void setupSpecifications() {
        reqSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addHeader("Authorization", "Bearer " + API_TOKEN)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
    }
}