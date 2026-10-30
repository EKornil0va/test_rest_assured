package org.example.testrestassured.tests;

import io.restassured.response.Response;
import org.example.testrestassured.dto.ProjectRequest;
import org.example.testrestassured.dto.ProjectResponse;
import org.example.testrestassured.utils.CsvReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ApiTests extends BaseApiTest {

    @DataProvider(name = "projectsData")
    public Object[][] getProjectsData() {
        return CsvReader.getData("projects_data.csv");
    }

    @Test(dataProvider = "projectsData", description = "Создание проекта (позитивные и негативные сценарии из CSV)")
    public void testProjectCreationDataDriven(String testCaseId, String projectId, String projectName, String shortName, int expectedStatus, String description) {
        if (testCaseId.startsWith("POS")) {
            long timestamp = System.currentTimeMillis();
            projectId = projectId + "_" + timestamp;
            shortName = shortName + String.valueOf(timestamp).substring(9);
            expectedStatus = 200;
        }

        ProjectRequest request = new ProjectRequest(projectId, projectName, shortName);

        Response response = given()
                .spec(reqSpec)
                .body(request)
                .when()
                .post("/api/admin/projects");

        int actualStatus = response.statusCode();
        System.out.println("\n" + "Ожидаемый статус: " + expectedStatus + " | Фактический статус: " + actualStatus);
        System.out.println("Ответ сервера: " + response.getBody().asString());

        Assert.assertEquals(actualStatus, expectedStatus, "Статус код не совпадает для тест-кейса: " + testCaseId);

        if (actualStatus == 200 || actualStatus == 201) {
            ProjectResponse createdProject = response.as(ProjectResponse.class);
            Assert.assertNotNull(createdProject.getId(), "ID проекта не должен быть null.");
            System.out.println("Успешно создан проект с ID: " + createdProject.getId());
        } else {
            System.out.println("Успешно. Некорректные данные отклонены сервером.");
        }
    }

    @Test(description = "Получение данных текущего пользователя по токену")
    public void testGetCurrentUser() {
        int expectedStatus = 200;

        Response response = given()
                .spec(reqSpec)
                .when()
                .get("/api/users/me");

        int actualStatus = response.statusCode();
        System.out.printf("\n" + "Ожидаемый статус: "+ expectedStatus + " | Фактический статус: " + actualStatus);
        System.out.println("Ответ сервера: " + response.getBody().asString());

        response.then()
                .statusCode(expectedStatus)
                .body("$type", equalTo("Me"))
                .body("id", notNullValue());

        System.out.println("Успешно. Данные пользователя получены.");
    }

    @Test(description = "Получение несуществующего проекта")
    public void testGetNonExistentProject() {
        int expectedStatus = 404;
        String fakeId = "FAKE_PROJECT_" + System.currentTimeMillis();

        Response response = given()
                .spec(reqSpec)
                .when()
                .get("/api/admin/projects/{id}", fakeId);

        int actualStatus = response.statusCode();
        System.out.println("\n" + "Ожидаемый статус: " + expectedStatus + " | Фактический статус: " + actualStatus);
        System.out.println("Ответ сервера: " + response.getBody().asString());

        response.then().statusCode(expectedStatus);
        System.out.println("Успешно. Получен 404.");
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
                .post("/api/admin/projects");

        int postActualStatus = postResponse.statusCode();
        System.out.println("\n" + "Ожидаемый статус: 200 | Фактический статус: " + postActualStatus);
        System.out.println("Ответ сервера: " + postResponse.getBody().asString());

        Assert.assertEquals(postActualStatus, 200, "Не удалось создать проект");

        ProjectResponse createdProject = postResponse.as(ProjectResponse.class);
        String internalId = createdProject.getId();
        Assert.assertNotNull(internalId, "Внутренний ID созданного проекта не должен быть null");
        System.out.println("Успешно. Проект создан с ID: " + internalId);

        Response getResponse = given()
                .spec(reqSpec)
                .pathParam("id", internalId)
                .when()
                .get("/api/admin/projects/{id}");

        int getActualStatus = getResponse.statusCode();
        System.out.println("\n" + "Ожидаемый статус: 200 | Фактический статус: " + getActualStatus);
        System.out.println("Ответ сервера: " + getResponse.getBody().asString());

        Assert.assertEquals(getActualStatus, 200, "Не удалось получить созданный проект");

        ProjectResponse fetchedProject = getResponse.as(ProjectResponse.class);
        Assert.assertEquals(fetchedProject.getId(), internalId, "ID полученного проекта не совпадает с созданным");
        System.out.println("Успешно. Проект найден по ID: " + fetchedProject.getId());
    }
}