package com.integration;

import com.actions.model.ActionStatus;
import com.api.model.StoryInput;
import com.api.output.ExecutionHistoryJSON;
import com.api.output.ExecutionStepJSON;
import com.util.TestEntityGenerator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {com.server.MainSpringBootApplication.class, com.api.config.JPAConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActionControllerTest {

    @LocalServerPort
    private int serverPort;

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost:" + serverPort;
    }

    @AfterAll
    void afterAll() {
        RestAssured.reset();
    }

    @Test
    void test_execute_action_endpoint() {

        final StoryInput storyInput = TestEntityGenerator.getStoryInput();

        RestAssured.basePath = "/v1/action";

        ExecutionHistoryJSON executionHistoryJSON =
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .body(storyInput)
                .when()
                        .post("/execute")
                .then()
                        .assertThat()
                        .statusCode(200)
                        .extract().as(ExecutionHistoryJSON.class);

        List<String> expectedActionNames = Arrays.asList("ASSIGN_STORY", "IMPLEMENT_REQUIREMENTS", "SEND_PULL_REQUEST_EVENT");
        List<String> actualActionNames = executionHistoryJSON.getExecutionSteps().stream().map(ExecutionStepJSON::getActionName).collect(Collectors.toList());

        assertIterableEquals(expectedActionNames, actualActionNames);
        assertEquals(ActionStatus.WAITING, executionHistoryJSON.getExecutionSteps().get(2).getActionResult().getStatus());
    }
}
