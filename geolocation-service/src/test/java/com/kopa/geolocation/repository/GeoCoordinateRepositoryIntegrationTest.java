package com.kopa.geolocation.repository;

import com.kopa.geolocation.model.GeoCoordinate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(initializers = {GeoCoordinateRepositoryIntegrationTest.Initializer.class})
@Testcontainers
class GeoCoordinateRepositoryIntegrationTest {

    @Autowired
    private GeoCoordinateRepository geoCoordinateRepository;

    private BigDecimal leftDownLongitude = new BigDecimal(2);
    private BigDecimal leftDownLatitude= new BigDecimal(41);
    private BigDecimal rightUpLongitude= new BigDecimal(3);
    private BigDecimal rightUpLatitude= new BigDecimal(42);

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:13.3-alpine")
            .withDatabaseName("integration-tests-db")
            .withUsername("geouser")
            .withPassword("geopassword");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    @Transactional
    public void givenCoordinatesInDb_WhenFindByRectangleCoordinates_ThenReturnOneUser() {
        insertCoordinates();

        List<GeoCoordinate> coordinates = geoCoordinateRepository.findAllByCoordinateRange(leftDownLongitude, leftDownLatitude, rightUpLongitude, rightUpLatitude);

        assertThat(coordinates.size()).isEqualTo(1);
        assertThat(coordinates.get(0).getId()).isEqualTo(2L);
    }

    @Test
    @Transactional
    public void givenCoordinatesInDb_WhenFindByRectangleCoordinates_ThenReturnTwoUsers() {
        insertCoordinates();
        geoCoordinateRepository.save(new GeoCoordinate(5L, new BigDecimal(2.7777777777777), new BigDecimal(41.3333333)));

        List<GeoCoordinate> coordinates = geoCoordinateRepository.findAllByCoordinateRange(leftDownLongitude, leftDownLatitude, rightUpLongitude, rightUpLatitude);

        assertThat(coordinates.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void givenCoordinatesInDb_WhenUpdateCoordinateAndThenFindByRectangleCoordinates_ThenReturnTwoUsers() {
        insertCoordinates();
        geoCoordinateRepository.save(new GeoCoordinate(3L, new BigDecimal(2.7777777777777), new BigDecimal(41.3333333)));

        List<GeoCoordinate> coordinates = geoCoordinateRepository.findAllByCoordinateRange(leftDownLongitude, leftDownLatitude, rightUpLongitude, rightUpLatitude);

        assertThat(coordinates.size()).isEqualTo(2);
    }

    private void insertCoordinates() {
        geoCoordinateRepository.save(new GeoCoordinate(1L, new BigDecimal(-12), new BigDecimal(89.2345634)));
        geoCoordinateRepository.save(new GeoCoordinate(2L, new BigDecimal(2.3456789), new BigDecimal(41.987654321)));
        geoCoordinateRepository.save(new GeoCoordinate(3L, new BigDecimal(30.3457647), new BigDecimal(-21.987654)));
        geoCoordinateRepository.save(new GeoCoordinate(4L, new BigDecimal(89.1241234234234), new BigDecimal(-41.987654)));
        geoCoordinateRepository.flush();
    }

}