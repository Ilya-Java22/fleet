package ru.skillsmart.fleet.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "trips_track_points")
public class TrackPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

//    @Column(columnDefinition = "geometry(Point,4326)")
//    @Type(type = "org.hibernate.spatial.dialect.postgis.PostgisPG95Dialect")
    private Point point;

//    @Type(type = "org.hibernate.spatial.dialect.postgis.PostgisPG95Dialect")
    //не компилится
//    @Type(type = "org.hibernate.spatial.JTSGeometryType")
    //не компилится
//    @Type(type = "org.hibernate.spatial.geom.Point")

}