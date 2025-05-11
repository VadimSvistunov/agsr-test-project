package by.svistunovvv.agsr_test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import by.svistunovvv.agsr_test.model.User;
import by.svistunovvv.agsr_test.model.sensor.Sensor;
import by.svistunovvv.agsr_test.repository.UserRepository;
import by.svistunovvv.agsr_test.service.JwtService;
import by.svistunovvv.agsr_test.service.SensorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;


@Testcontainers
@TestPropertySource("/application-test.yml")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SensorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SensorService service;
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    ).withInitScript("data.sql");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private String getToken(String email) {
        var user = userDetailsService.loadUserByUsername(email);
        return jwtService.generateToken(user);
    }

    @Test
    void initTest() {
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void getAllShouldReturn200() throws Exception {
        String token = getToken("viewer@example.com");

        mockMvc.perform(get("/api/v1/sensor/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNumber());
    }

    @Test
    void getNameContainsShouldReturnFilteredResults() throws Exception {
        String token = getToken("viewer@example.com");

        mockMvc.perform(get("/api/v1/sensor/name")
                        .header("Authorization", "Bearer " + token)
                        .queryParam("name", "Barom")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Barometer"));
    }

    @Test
    void getModelContainsShouldReturnFilteredResults() throws Exception {
        String token = getToken("viewer@example.com");

        mockMvc.perform(get("/api/v1/sensor/model")
                        .header("Authorization", "Bearer " + token)
                        .queryParam("model", "temp")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("temp-x2"));
    }

    @Test
    void saveShouldReturnId() throws Exception {
        String token = getToken("admin@example.com");

        String requestJson = """
            {
                "name": "MySensor",
                "model": "x100",
                "range": {
                    "from": 10,
                    "to": 50
                },
                "type": "Pressure",
                "unit": "bar",
                "location": "kitchen",
                "description": "Test sensor"
            }
            """;

        mockMvc.perform(post("/api/v1/sensor")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void updateShouldReturnId() throws Exception {
        String token = getToken("admin@example.com");
        List<Sensor> sensors = service.findAll();
        long id = sensors.getFirst().getId();

        String updateJson = """
            {
                "name": "UpdatedSensor",
                "model": "x200",
                "range": {
                    "from": 10,
                    "to": 50
                },
                "type": "Temperature",
                "unit": "°С",
                "location": "roof",
                "description": "Updated desc"
            }
            """;

        mockMvc.perform(put("/api/v1/sensor/update/{id}", id)
                        .header("Authorization", "Bearer " + token)
                        .content(updateJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @Test
    void deleteShouldReturnTrue() throws Exception {
        String token = getToken("admin@example.com");

        mockMvc.perform(delete("/api/v1/sensor/delete/{id}", 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getByIdShouldReturnSensorDto() throws Exception {
        String token = getToken("admin@example.com");

        mockMvc.perform(get("/api/v1/sensor/{id}", 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.model").isNotEmpty());
    }
}