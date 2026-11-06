package org.example.testrestassured.tests;

import io.restassured.response.Response;
import org.example.testrestassured.dto.ProjectRequest;
import org.example.testrestassured.dto.ProjectResponse;
import org.example.testrestassured.specifications.RequestSpec;
import org.example.testrestassured.specifications.ResponseSpec;
import org.example.testrestassured.utils.CsvReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.example.testrestassured.utils.ApiConstants.Endpoint.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class ProjectTests extends BaseApiTest {

    @DataProvider(name = "positiveProjectsData", parallel = true)
    public Object[][] getPositiveProjectsData() {
        return CsvReader.getData("projects_data.csv", "POS");
    }

    @DataProvider(name = "negativeProjectsData", parallel = true)
    public Object[][] getNegativeProjectsData() {
        return CsvReader.getData("projects_data.csv", "NEG");
    }

    @Test(dataProvider = "positiveProjectsData", description = "Создание проекта с валидными данными")
    public void testProjectCreationPositive(String testCaseId, String projectId, String projectName,
                                            String shortName, int expectedStatus, String description) {
        long timestamp = System.currentTimeMillis();
        String uniqueProjectId = projectId + "_" + timestamp;
        String uniqueShortName = shortName + String.valueOf(timestamp).substring(9);

        ProjectRequest request = new ProjectRequest(uniqueProjectId, projectName, uniqueShortName);

        Response response = given()
                .spec(RequestSpec.projectRequestSpec())
                .body(request)
                .when()
                .post(PROJECTS.getPath())
                .then()
                .spec(ResponseSpec.withStatusCode(expectedStatus))
                .extract().response();

        ProjectResponse createdProject = response.as(ProjectResponse.class);
        Assert.assertNotNull(createdProject.getId(), "ID проекта не должен быть null");
    }

    @Test(dataProvider = "negativeProjectsData", description = "Создание проекта с невалидными данными")
    public void testProjectCreationNegative(String testCaseId, String projectId, String projectName,
                                            String shortName, int expectedStatus, String description) {
        ProjectRequest request = new ProjectRequest(projectId, projectName, shortName);

        given()
                .spec(RequestSpec.projectRequestSpec())
                .body(request)
                .when()
                .post(PROJECTS.getPath())
                .then()
                .spec(ResponseSpec.withStatusCode(expectedStatus));
    }

    @Test(description = "Получение несуществующего проекта")
    public void testGetNonExistentProject() {
        String fakeId = FAKE_PROJECT_PREFIX.getPath() + System.currentTimeMillis();

        given()
                .spec(RequestSpec.projectRequestSpec())
                .pathParam("id", fakeId)
                .when()
                .get(PROJECT_BY_ID.getPath())
                .then()
                .spec(ResponseSpec.notFoundResponse());
    }

    @Test(description = "Полный цикл: Создание и немедленное получение проекта")
    public void testCreateAndGetProjectIndependently() {
        long timestamp = System.currentTimeMillis();
        String uniqueId = "CYCLE" + timestamp;
        String uniqueName = "Cycle Test Project " + timestamp;
        String uniqueShort = "C" + String.valueOf(timestamp).substring(9);

        ProjectRequest request = new ProjectRequest(uniqueId, uniqueName, uniqueShort);

        Response postResponse = given()
                .spec(RequestSpec.projectRequestSpec())
                .body(request)
                .when()
                .post(PROJECTS.getPath())
                .then()
                .spec(ResponseSpec.successResponse())
                .extract().response();

        ProjectResponse createdProject = postResponse.as(ProjectResponse.class);
        String internalId = createdProject.getId();
        Assert.assertNotNull(internalId, "ID созданного проекта не должен быть null");

        given()
                .spec(RequestSpec.projectRequestSpec())
                .pathParam("id", internalId)
                .when()
                .get(PROJECT_BY_ID.getPath())
                .then()
                .spec(ResponseSpec.successResponse())
                .body("id", equalTo(internalId));
    }
}