package com.kopa.geolocation.controller;

import com.kopa.geolocation.model.GeoCoordinate;
import com.kopa.geolocation.repository.GeoCoordinateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
class GeoControllerTest {

    @MockBean
    private GeoCoordinateRepository geoCoordinateRepository;

    @Autowired
    private GeoController geoController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestToLocationAndValidLocation_thenCorrectResponse() throws Exception {
        String validCoordinate = "{\"id\": 111111, \"longitude\" : 90.54321, \"latitude\" : 45.12345}";
        mockMvc.perform(MockMvcRequestBuilders.post("/geo")
                .content(validCoordinate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenPostRequestToLocationAndInvalidLocation_thenCorrectResponse() throws Exception {
        String invalidCoordinate = "{\"id\": 111111, \"longitude\" : 190, \"latitude\" : 45.12345}";
        mockMvc.perform(MockMvcRequestBuilders.post("/geo")
                .content(invalidCoordinate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage().contains("Validation failed for argument [0]")));
    }

    @Test
    public void whenGetRequestToLocationsAndValidRequest_thenCorrectResponse() throws Exception {
        List<GeoCoordinate> result = Arrays.asList(new GeoCoordinate(123456789L, new BigDecimal("2.3456789"), new BigDecimal("41.987654321")));
        when(geoCoordinateRepository.findAllByCoordinateRange(any(), any(), any(), any())).thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/geo")
                .param("leftDownLongitude", "2")
                .param("leftDownLatitude", "41")
                .param("rightUpLongitude", "3")
                .param("rightUpLatitude", "42")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].latitude").value("41.987654321"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].longitude").value("2.3456789"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}