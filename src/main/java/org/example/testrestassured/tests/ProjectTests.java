package org.example.testrestassured.tests;

import io.restassured.response.Response;
import org.example.testrestassured.dto.ProjectRequest;
import org.example.testrestassured.dto.ProjectResponse;
import org.example.testrestassured.utils.CsvReader;
import org.example.testrestassured.utils.SpecBuilder;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.example.testrestassured.utils.ApiConstants.ENDPOINT_PROJECTS;
import static org.example.testrestassured.utils.ApiConstants.FAKE_PROJECT_PREFIX;
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
    public void testProjectCreationPositive(String testCaseId, String projectId, String projectName, String shortName, int expectedStatus, String description) {
        long timestamp = System.currentTimeMillis();
        String uniqueProjectId = projectId + "_" + timestamp;
        String uniqueShortName = shortName + String.valueOf(timestamp).substring(9);

        ProjectRequest request = new ProjectRequest(uniqueProjectId, projectName, uniqueShortName);

        Response response = given()
                .spec(reqSpec)
                .body(request)
                .when()
                .post(ENDPOINT_PROJECTS)
                .then()
                .spec(SpecBuilder.getResponseSpec(expectedStatus))
                .extract().response();

        ProjectResponse createdProject = response.as(ProjectResponse.class);
        Assert.assertNotNull(createdProject.getId(), "ID проекта не должен быть null");
        System.out.println("Успешно создан проект с ID: " + createdProject.getId());
    }

    @Test(dataProvider = "negativeProjectsData", description = "Создание проекта с невалидными данными")
    public void testProjectCreationNegative(String testCaseId, String projectId, String projectName, String shortName, int expectedStatus, String description) {
        ProjectRequest request = new ProjectRequest(projectId, projectName, shortName);

        given()
                .spec(reqSpec)
                .body(request)
                .when()
                .post(ENDPOINT_PROJECTS)
                .then()
                .spec(SpecBuilder.getResponseSpec(expectedStatus));
    }

    @Test(description = "Получение несуществующего проекта")
    public void testGetNonExistentProject() {
        String fakeId = FAKE_PROJECT_PREFIX + System.currentTimeMillis();

        given()
                .spec(reqSpec)
                .pathParam("id", fakeId)
                .when()
                .get(ENDPOINT_PROJECTS + "/{id}")
                .then()
                .spec(SpecBuilder.getResponseSpec(404));
    }

    @Test(description = "Полный цикл: Создание и немедленное получение проекта")
    public void testCreateAndGetProjectIndependently() {
        long timestamp = System.currentTimeMillis();
        String uniqueId = "CYCLE" + timestamp;
        String uniqueName = "Cycle Test Project " + timestamp;
        String uniqueShort = "C" + String.valueOf(timestamp).substring(9);

        ProjectRequest request = new ProjectRequest(uniqueId, uniqueName, uniqueShort);

        Response postResponse = given()
                .spec(reqSpec)
                .body(request)
                .when()
                .post(ENDPOINT_PROJECTS)
                .then()
                .spec(SpecBuilder.getResponseSpec(200))
                .extract().response();

        ProjectResponse createdProject = postResponse.as(ProjectResponse.class);
        String internalId = createdProject.getId();
        Assert.assertNotNull(internalId, "ID созданного проекта не должен быть null");

        given()
                .spec(reqSpec)
                .pathParam("id", internalId)
                .when()
                .get(ENDPOINT_PROJECTS + "/{id}")
                .then()
                .spec(SpecBuilder.getResponseSpec(200))
                .body("id", equalTo(internalId));
    }
}