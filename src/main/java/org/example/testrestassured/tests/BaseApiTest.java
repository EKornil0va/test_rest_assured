package org.example.testrestassured.tests;

import io.restassured.specification.RequestSpecification;
import org.example.testrestassured.specifications.RequestSpec;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {
    protected RequestSpecification reqSpec;

    @BeforeClass
    public void setupSpecifications() {
        this.reqSpec = RequestSpec.baseRequestSpec();
    }
}