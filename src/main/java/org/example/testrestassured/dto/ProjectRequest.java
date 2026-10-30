package org.example.testrestassured.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class ProjectRequest {
    private String id;
    private String name;
    private String shortName;
    private Integer startingNumber = 1;
    private Leader leader = new Leader("admin");

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Leader {
        private String login = "admin";
    }

    public ProjectRequest(String id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }
}
