package com.nowakkacper.PlanesTracker;

import com.fasterxml.jackson.databind.JsonNode;
import com.nowakkacper.PlanesTracker.model.PlaneInfo;
import com.nowakkacper.PlanesTracker.model.StartingPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaneService {

    public List<PlaneInfo> getAllPlanesInfo(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://opensky-network.org/api/states/all?lamin=47.115&lomin=10.975&lamax=50&lomax=15";
        JsonNode data = restTemplate.getForObject(url, JsonNode.class).get("states");

        List<PlaneInfo> planeInfos = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            System.out.println("PlaneInfo");
            JsonNode state = data.get(i);
            PlaneInfo planeInfo1 = createState(state);
            planeInfo1.setStartingPoint(addStartingPoint(planeInfo1.getIcao24(), planeInfo1.getX(), planeInfo1.getY()));
            planeInfos.add(planeInfo1);
        }

        return planeInfos;
    }

    private StartingPoint addStartingPoint(String icao24, Double x, Double y) {
        System.out.println("Starting");
        RestTemplate restTemplate = new RestTemplate();
        StartingPoint startingPoint = new StartingPoint();
        try{
            String url = "https://opensky-network.org/api/tracks/all?icao24=" + icao24 + "&time=0";
            JsonNode data = restTemplate.getForObject(url, JsonNode.class).get("path").get(0);

            if(data.get(1) != null) startingPoint.setY(data.get(1).asDouble());
            else startingPoint.setY(y);

            if(data.get(2) != null) startingPoint.setX(data.get(2).asDouble());
            else startingPoint.setX(x);

            return startingPoint;
        }
        catch (RestClientException e) {
            startingPoint.setX(x);
            startingPoint.setY(y);
            return startingPoint;
        }
    }

    private static PlaneInfo createState(JsonNode state) {
        PlaneInfo planeInfo1 = new PlaneInfo();

        planeInfo1.setIcao24(state.get(0).asText());

        if(state.get(1) == null) planeInfo1.setCallsign("-");
        else planeInfo1.setCallsign(state.get(1).asText());

        planeInfo1.setOriginCountry(state.get(2).asText());

        int secondsFromEpoch;
        if(state.get(3) == null) secondsFromEpoch=0;
        else secondsFromEpoch = state.get(3).asInt();
        Instant instant = Instant.ofEpochSecond(secondsFromEpoch);
        planeInfo1.setTimePosition(LocalDateTime.ofInstant(instant, ZoneOffset.UTC));

        if (state.get(5) != null) planeInfo1.setX(state.get(5).asDouble());
        else planeInfo1.setX(360.0);

        if (state.get(6) != null) planeInfo1.setY(state.get(6).asDouble());
        else planeInfo1.setY(360.0);

        if (state.get(7) != null) planeInfo1.setBaroAltitude(state.get(7).asDouble());
        else planeInfo1.setBaroAltitude(0.0);

        if (state.get(9) != null) planeInfo1.setVelocity(state.get(9).asDouble());
        else planeInfo1.setVelocity(0.0);

        return planeInfo1;
    }
}