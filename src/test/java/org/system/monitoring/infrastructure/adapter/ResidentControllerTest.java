package org.system.monitoring.infrastructure.adapter;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.system.monitoring.application.mapper.FakeEntity;
import org.system.monitoring.domain.collection.dto.request.ResidentRequestDTO;
import org.system.monitoring.infrastructure.firebase.util.ResponseStatusDTO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResidentControllerTest {

    private static final String TEST_RESIDENT_ID = "testResident123";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void create() {
        ResidentRequestDTO request = FakeEntity.residentRequestDTO();
        ResponseEntity<ResponseStatusDTO> response = restTemplate.postForEntity(
                "/resident/create?uid=" + TEST_RESIDENT_ID,
                request,
                ResponseStatusDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).isSuccess()).isTrue();
    }

    @Test
    @Order(2)
    void byId() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/resident/byId/" + TEST_RESIDENT_ID, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        DocumentContext json = JsonPath.parse(response.getBody());
        String fullName = json.read("$.fullName");
        String dni = json.read("$.dni");
        String phone = json.read("$.phone");

        assertNotNull(fullName);
        assertNotNull(dni);
        assertNotNull(phone);
    }

    @Test
    @Order(3)
    void setValuesPerField() {
        ResponseEntity<?> response = restTemplate.getForEntity(
                "/resident/setValuesPerField/fullName",
                Object.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(4)
    void update() {
        ResidentRequestDTO updated = new ResidentRequestDTO(1, true, "testUserRef");

        ResponseEntity<ResponseStatusDTO> response = restTemplate.exchange(
                "/resident/update?uid=" + TEST_RESIDENT_ID,
                HttpMethod.PUT,
                new HttpEntity<>(updated),
                ResponseStatusDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).isSuccess()).isTrue();

        ResponseEntity<Object> getResponse = restTemplate.getForEntity("/resident/byId/" + TEST_RESIDENT_ID, Object.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext json = JsonPath.parse(getResponse.getBody());
        String fullName = json.read("$.fullName");

        assertThat(fullName).isEqualTo("Nombre Actualizado");
    }

    @Test
    @Order(5)
    void list() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/resident/list", Object.class);

        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(6)
    void search() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/resident/search?search=Nombre", Object.class);

        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NO_CONTENT, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(7)
    void delete() {
        ResponseEntity<ResponseStatusDTO> deleteResponse = restTemplate.exchange(
                "/resident/delete/" + TEST_RESIDENT_ID,
                HttpMethod.DELETE,
                null,
                ResponseStatusDTO.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(deleteResponse.getBody()).isSuccess()).isTrue();

        ResponseEntity<Object> getResponse = restTemplate.getForEntity("/resident/byId/" + TEST_RESIDENT_ID, Object.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
