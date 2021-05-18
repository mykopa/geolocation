package com.kopa.geolocation.repository;

import com.kopa.geolocation.model.GeoCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface GeoCoordinateRepository extends JpaRepository<GeoCoordinate, Long> {

    @Query(value = "SELECT gc FROM GeoCoordinate gc WHERE gc.longitude > :leftDownLongitude AND gc.longitude < :rightUpLongitude " +
            "AND gc.latitude > :leftDownLatitude AND gc.latitude < :rightUpLatitude")
    public List<GeoCoordinate> findAllByCoordinateRange(@Param("leftDownLongitude") BigDecimal leftDownLongitude,
                                                        @Param("leftDownLatitude") BigDecimal leftDownLatitude,
                                                        @Param("rightUpLongitude") BigDecimal rightUpLongitude,
                                                        @Param("rightUpLatitude") BigDecimal rightUpLatitude);
}
