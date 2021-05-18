package com.kopa.geolocation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Table(name="GEO_COORDINATE")
public class GeoCoordinate {

    public GeoCoordinate() {}

    public GeoCoordinate(Long id, BigDecimal longitude, BigDecimal latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Id
    @Column(name = "ID")
    private Long id;

    @DecimalMin(value = "-180.0000000")
    @DecimalMax(value = "180.0000000")
    @Column(name = "LONGITUDE", nullable = false, precision=11, scale=8)
    private BigDecimal longitude;

    @DecimalMin(value = "-90.0000000")
    @DecimalMax(value = "90.0000000")
    @Column(name = "LATITUDE", nullable = false, precision=10, scale=8)
    private BigDecimal latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

}
