package org.example.testrestassured.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectResponse {
    private String id;
    private String name;
    private boolean archived;
}
