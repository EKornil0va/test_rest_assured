package org.example.testrestassured.utils;

public class ApiConstants {
    public static final String BASE_URL = ConfigReader.getProperty("base.url");
    public static final String API_TOKEN = ConfigReader.getProperty("api.token");

    public enum Endpoint {
        PROJECTS("/api/admin/projects"),
        PROJECT_BY_ID("/api/admin/projects/{id}"),
        USERS_ME("/api/users/me");

        private final String path;

        Endpoint(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;

        }
    }
}
