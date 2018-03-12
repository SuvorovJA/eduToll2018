// TEST-DRIVE-PROJECT. no Spring integration
package edu.gpxreader;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

import java.time.ZonedDateTime;
import java.util.List;

public class Main {


    public static void main(String... args) throws Exception {
//        get resorce file path
        String file = Main.class.getResource("/11060.gpx").getPath().toString();

//        lenient - if true, out-of-range and syntactical errors are ignored.
//        E.g. a WayPoint with lat values not in the valid range of [-90..90] are ignored/skipped.
        final boolean lenient = true;
        final GPX gpx = GPX.read(file, lenient);

//        одной командой вывести весь трэк
//        gpx.tracks().flatMap(Track::segments).flatMap(TrackSegment::points).forEach(System.out::println);

//        вывести итеративно, включая поле времени
        ZonedDateTime zdt=null;
        long lit=0;
        List<Track> trl = gpx.getTracks();
        List<TrackSegment> trsl = trl.get(0).getSegments();
        List<WayPoint> wpl = trsl.get(0).getPoints(); // 1441 points, примерно половина точек из файла
        for (WayPoint wp : wpl) {
            System.out.print(wp.toString() + " "
                    + wp.getTime().toString().replace("Optional[", "").replace("]", "")+ " ") ;
            // PointDTO use long int field for time
            zdt = wp.getTime().get();
            lit = zdt.toInstant().toEpochMilli();
            System.out.println(lit);
        }
    }
}
