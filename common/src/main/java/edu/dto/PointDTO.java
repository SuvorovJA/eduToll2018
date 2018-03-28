package edu.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by jdev on 06.03.2017.
 * Copy by sua on 05.03.2018
 */
public class PointDTO {
    private double lat;
    private double lon;
    private String autoId;
    private long time;

//    public double getLat() {
//        return lat;
//    }

    public void setLat(double lat) {
        this.lat = lat;
    }

//    public double getLon() {
//        return lon;
//    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

//    не используется
//    @Override
//    public String toString() {
//        return "PointDTO{" +
//                "lat=" + lat +
//                ", lon=" + lon +
//                ", autoId='" + autoId + '\'' +
//                ", time=" + time +
//                '}';
//    }

}
