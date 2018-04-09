package edu.server.repository;



//import lombok.Getter;                         //
//import lombok.NoArgsConstructor;              //
//import lombok.Setter;                         //
//import lombok.ToString;                       // lombok no work
//import lombok.extern.log4j.Log4j;             //
//import lombok.extern.slf4j.Slf4j;             //

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

// Err: No identifier specified for entity: edu.server.repository.PointDTOEntity
//import org.springframework.data.annotation.Id;  conflict with
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;


//@Slf4j                //
//@Getter               //
//@Setter               // lombok no work
//@ToString             //
//@NoArgsConstructor    //

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

    //

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


}
