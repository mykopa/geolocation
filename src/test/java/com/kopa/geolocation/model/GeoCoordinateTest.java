package com.kopa.geolocation.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;


import java.math.BigDecimal;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GeoCoordinateTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        //Hardcode to avoid test fails in IntelliJ IDEA due of error messages i18n
        Locale.setDefault(Locale.ENGLISH);

        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void whenLongitudeMoreThan180_thenShouldGiveConstraintViolations() {
        //Given
        GeoCoordinate geoCoordinate = new GeoCoordinate(123456789L, new BigDecimal(181), new BigDecimal(45));

        //When
        Set<ConstraintViolation<GeoCoordinate>> violations = validator.validate(geoCoordinate);

        //Then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("must be less than or equal to 180.0000000");
    }

    @Test
    public void whenLongitudeLessThanMinus180_thenShouldGiveConstraintViolations() {
        //Given
        GeoCoordinate geoCoordinate = new GeoCoordinate(123456789L, new BigDecimal(-181.00000001), new BigDecimal(45));

        //When
        Set<ConstraintViolation<GeoCoordinate>> violations = validator.validate(geoCoordinate);

        //Then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("must be greater than or equal to -180.0000000");
    }

    @Test
    public void whenLatitudeMoreThan90_thenShouldGiveConstraintViolations() {
        //Given
        GeoCoordinate geoCoordinate = new GeoCoordinate(123456789L, new BigDecimal(90), new BigDecimal(90.01));

        //When
        Set<ConstraintViolation<GeoCoordinate>> violations = validator.validate(geoCoordinate);

        //Then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("must be less than or equal to 90.0000000");
    }

    @Test
    public void whenLatitudeLessThanMinus90_thenShouldGiveConstraintViolations() {
        //Given
        GeoCoordinate geoCoordinate = new GeoCoordinate(123456789L, new BigDecimal(90), new BigDecimal(-91));

        //When
        Set<ConstraintViolation<GeoCoordinate>> violations = validator.validate(geoCoordinate);

        //Then
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("must be greater than or equal to -90.0000000");
    }

    @Test
    public void whenLongitudeLessOrEq180AndLatitudeLessOrEq90_thenShouldNotConstraintViolations() {
        //Given
        GeoCoordinate geoCoordinate = new GeoCoordinate(123456789L, new BigDecimal(180.000000000), new BigDecimal(90));

        //When
        Set<ConstraintViolation<GeoCoordinate>> violations = validator.validate(geoCoordinate);

        //Then
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenLongitudeMoreOrEqMinus180AndLatitudeMoreOrEqMinus90_thenShouldNotConstraintViolations() {
        //Given
        GeoCoordinate geoCoordinate = new GeoCoordinate(123456789L, new BigDecimal(-180.000000000), new BigDecimal(-90));

        //When
        Set<ConstraintViolation<GeoCoordinate>> violations = validator.validate(geoCoordinate);

        //Then
        assertThat(violations).isEmpty();
    }

}