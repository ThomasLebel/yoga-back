package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;

    @BeforeAll
    static void setup(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@gmail.com");
        loginRequest.setPassword("test!1234");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        token = objectMapper.readTree(responseBody).get("token").asText();
    }

    @Test
    @DisplayName("Should return a session by id")
    void findById() throws Exception {
        mockMvc.perform(get("/api/session/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hatha Yoga"))
                .andExpect(jsonPath("$.description").value("Seance relaxante de Hatha Yoga pour debutants"));
    }

    @Test
    @DisplayName("Should return Not Found if session not found")
    void findByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/session/99").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return Bad Request if parameter is not a number")
    void findByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/session/abc").header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return list of sessions")
    void findAll() throws Exception {
        mockMvc.perform(get("/api/session").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hatha Yoga"))
                .andExpect(jsonPath("$[1].name").value("Yin Yoga"))
                .andExpect(jsonPath("$[2].name").value("Power Yoga"));
    }

    @Test
    @DisplayName("Should save a new session")
    void create() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDescription("New Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDate(new Date());


        mockMvc.perform(post("/api/session")
                        .header("Authorization", "Bearer " + token)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should update session by id")
    void update() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDescription("Updated Description");
        sessionDto.setTeacher_id(2L);
        sessionDto.setDate(new Date());

        mockMvc.perform(put("/api/session/1")
                        .header("Authorization", "Bearer " + token)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Session"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

    }

    @Test
    @DisplayName("Should delete session by id")
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/3").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return Not Found if session doesn't exist")
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/99").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return Bad Request if parameter is not number")
    void deleteBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/abc").header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should add user id to the session")
    void participate() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/1").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
        mockMvc.perform(get("/api/session/1").header("Authorization", "Bearer " + token)).andExpect(jsonPath("$.users[*]", hasItem(1)));
    }

    @Test
    @DisplayName("Should return Bad Request if parameter is not number")
    void participateBadRequest() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/abc").header("Authorization", "Bearer " + token)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should remove user id to the session")
    void noLongerParticipate() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/session/1").header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.users[*]", hasItem(1)));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1/participate/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/session/1").header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.users[*]", hasSize(0)));
    }

    @Test
    @DisplayName("Should return Bad Request if parameter is not number")
    void noLongerParticipateBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1/participate/abc").header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }
}