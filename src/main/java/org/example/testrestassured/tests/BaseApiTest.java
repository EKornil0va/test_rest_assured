package org.example.testrestassured.tests;

import io.restassured.specification.RequestSpecification;
import org.example.testrestassured.utils.SpecBuilder;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {
    protected RequestSpecification reqSpec;

    @BeforeClass
    public void setupSpecifications() {
        this.reqSpec = SpecBuilder.getRequestSpec();
    }
}