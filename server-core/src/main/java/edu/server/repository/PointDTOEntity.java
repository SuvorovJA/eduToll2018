package edu.server.repository;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "points")
public class PointDTOEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "ID")
    int id;

    @Column(name = "LAT")
    double lat;

    @Column(name = "LON")
    double lon;

    @Column(name = "AUTOID")
    String autoId;

    @Column(name = "TIME")
    long time;

    public PointDTOEntity() {
    }

    public PointDTOEntity(int id, double lat, double lon, String autoId, long time) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.autoId = autoId;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PointDTOEntity{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", autoId='" + autoId + '\'' +
                ", time=" + time +
                '}';
    }
}
