package org.example.testrestassured.utils;

public class ApiConstants {
    public static final String BASE_URL = ConfigReader.getProperty("base.url");
    public static final String API_TOKEN = ConfigReader.getProperty("api.token");

    public static final String ENDPOINT_PROJECTS = "/api/admin/projects";
    public static final String ENDPOINT_USERS_ME = "/api/users/me";
    public static final String FAKE_PROJECT_PREFIX = "FAKE_PROJECT";
}
