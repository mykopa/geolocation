package com.kopa.geolocation.controller;

import com.kopa.geolocation.model.GeoCoordinate;
import com.kopa.geolocation.repository.GeoCoordinateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("geo")
public class GeoController {

    @Autowired
    private GeoCoordinateRepository geoCoordinateRepository;

    @GetMapping("{id}")
    public GeoCoordinate getVehicle(@PathVariable Long id) {
        return geoCoordinateRepository.findById(id).get();
    }

    @GetMapping
    public List<GeoCoordinate> findVehicles(@RequestParam BigDecimal leftDownLongitude,
                                            @RequestParam BigDecimal leftDownLatitude,
                                            @RequestParam BigDecimal rightUpLongitude,
                                            @RequestParam BigDecimal rightUpLatitude) {
        return geoCoordinateRepository.findAllByCoordinateRange(leftDownLongitude, leftDownLatitude, rightUpLongitude, rightUpLatitude);
    }

    @PostMapping
    public ResponseEntity addGeoCoordinate(@Valid @RequestBody GeoCoordinate geoCoordinate) {
        geoCoordinateRepository.save(geoCoordinate);
        return ResponseEntity.ok().build();
    }
}
