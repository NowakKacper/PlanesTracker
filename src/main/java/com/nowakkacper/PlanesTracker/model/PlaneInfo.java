package com.nowakkacper.PlanesTracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaneInfo {

    private String icao24;
    private String callsign;
    private String originCountry;
    private LocalDateTime timePosition;
    private Double x;
    private Double y;
    private Double baroAltitude;
    private Double velocity;
    private StartingPoint startingPoint;

}
