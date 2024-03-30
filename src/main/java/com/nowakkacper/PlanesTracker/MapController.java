package com.nowakkacper.PlanesTracker;

import com.nowakkacper.PlanesTracker.model.PlaneInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MapController {

    private final PlaneService planeService;

    public MapController(PlaneService planeService) {
        this.planeService = planeService;
    }

    @GetMapping
    public String getMap(Model model) {
        List<PlaneInfo> planeInfos = planeService.getAllPlanesInfo();
        model.addAttribute("tracks", planeInfos);
        return "map";
    }

    @GetMapping("/update")
    public ResponseEntity<List<PlaneInfo>> getUpdatedData() {
        return ResponseEntity.ok(planeService.getAllPlanesInfo());
    }
}