package org.system.monitoring.infrastructure.adapter;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.system.monitoring.application.mapper.FakeEntity;
import org.system.monitoring.domain.collection.dto.request.BonusRequestDTO;
import org.system.monitoring.infrastructure.firebase.util.ResponseStatusDTO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BonusControllerTest {

    private static final String TEST_BONUS_ID = "testBonus123";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void create() {
        BonusRequestDTO request = FakeEntity.bonusRequestDTO();
        ResponseEntity<ResponseStatusDTO> response = restTemplate.postForEntity(
                "/bonus/create?uid=" + TEST_BONUS_ID,
                request,
                ResponseStatusDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).isSuccess()).isTrue();
    }

    @Test
    @Order(2)
    void byId() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/bonus/byId/" + TEST_BONUS_ID, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        DocumentContext json = JsonPath.parse(response.getBody());
        String nameBonus = json.read("$.nameBonus");
        String description = json.read("$.description");
        Integer requiredTickets = json.read("$.requiredTickets");
        Double status = json.read("$.status");

        assertNotNull(nameBonus);
        assertNotNull(description);
        assertNotNull(requiredTickets);
        assertNotNull(status);
    }

    @Test
    @Order(3)
    void setValuesPerField() {
        ResponseEntity<?> response = restTemplate.getForEntity(
                "/bonus/setValuesPerField/nameBonus",
                Object.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(4)
    void update() {
        BonusRequestDTO updated = new BonusRequestDTO(
                "Eco Bono Actualizado",
                "Descripción actualizada",
                20,
                1.0
        );

        ResponseEntity<ResponseStatusDTO> response = restTemplate.exchange(
                "/bonus/update?uid=" + TEST_BONUS_ID,
                HttpMethod.PUT,
                new HttpEntity<>(updated),
                ResponseStatusDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).isSuccess()).isTrue();

        ResponseEntity<Object> getResponse = restTemplate.getForEntity("/bonus/byId/" + TEST_BONUS_ID, Object.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext json = JsonPath.parse(getResponse.getBody());
        String nameBonus = json.read("$.nameBonus");
        Integer requiredTickets = json.read("$.requiredTickets");

        assertThat(nameBonus).isEqualTo("Eco Bono Actualizado");
        assertThat(requiredTickets).isEqualTo(20);
    }

    @Test
    @Order(5)
    void list() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/bonus/list", Object.class);

        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(6)
    void search() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/bonus/search?search=Eco", Object.class);

        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NO_CONTENT, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(7)
    void delete() {
        ResponseEntity<ResponseStatusDTO> deleteResponse = restTemplate.exchange(
                "/bonus/delete/" + TEST_BONUS_ID,
                HttpMethod.DELETE,
                null,
                ResponseStatusDTO.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(deleteResponse.getBody()).isSuccess()).isTrue();

        ResponseEntity<Object> getResponse = restTemplate.getForEntity("/bonus/byId/" + TEST_BONUS_ID, Object.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
