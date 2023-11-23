package com.okey.drone.controller;

import com.okey.drone.dto.BaseResponse;
import com.okey.drone.dto.DroneRequest;
import com.okey.drone.dto.DroneResponse;
import com.okey.drone.dto.MedicationRequest;
import com.okey.drone.dto.MedicationResponse;
import com.okey.drone.service.DroneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @PostMapping
    public ResponseEntity<BaseResponse<DroneResponse>> registerDrone(@Valid @RequestBody DroneRequest droneRequest) {
        return new ResponseEntity<>(droneService.registerDrone(droneRequest), HttpStatus.CREATED);
    }

    @PostMapping("/{droneSerialNumber}/load")
    public ResponseEntity<?> loadMedications(@PathVariable String droneSerialNumber, @Valid @RequestBody List<MedicationRequest> medicationsToBeLoaded) {
        return new ResponseEntity<>(droneService.loadMedications(droneSerialNumber, medicationsToBeLoaded), HttpStatus.OK);
    }

    @GetMapping("/{droneSerialNumber}/medications")
    public ResponseEntity<List<MedicationResponse>> getLoadedMedications(@PathVariable String droneSerialNumber) {
        return new ResponseEntity<>(droneService.getLoadedMedications(droneSerialNumber), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableDronesForLoading() {
        return new ResponseEntity<>(droneService.getAvailableDronesForLoading(), HttpStatus.OK);
    }

    @GetMapping("/{droneSerialNumber}/battery")
    public ResponseEntity<BaseResponse<Integer>> getDroneBatteryLevel(@PathVariable String droneSerialNumber) {
        return new ResponseEntity<>(droneService.getDroneBatteryLevel(droneSerialNumber), HttpStatus.OK);
    }

}
