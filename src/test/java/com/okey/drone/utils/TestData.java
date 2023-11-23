package com.okey.drone.utils;

import com.okey.drone.dto.DroneRequest;
import com.okey.drone.dto.DroneResponse;
import com.okey.drone.dto.MedicationResponse;
import com.okey.drone.entity.Drone;
import com.okey.drone.entity.DroneState;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TestData {

    public DroneRequest getDroneRequest() {
        return DroneRequest.builder()
                .model("Test Drone")
                .weightLimit(100)
                .batteryCapacity(100)
                .state(DroneState.IDLE)
                .build();
    }

    public Drone getDrone() {
        return Drone.builder()
                .model("Test Drone")
                .serialNumber(UUID.randomUUID().toString())
                .weightLimit(100)
                .batteryCapacity(100)
                .state(DroneState.IDLE)
                .build();
    }

    public DroneResponse getDroneResponse(Drone drone) {
        return DroneResponse.builder()
                .serialNumber(drone.getSerialNumber())
                .weightLimit(drone.getWeightLimit())
                .state(drone.getState())
                .batteryCapacity(drone.getBatteryCapacity())
                .build();
    }

    public static MedicationResponse getMedicationResponse(long l) {
        return MedicationResponse.builder()
                .name("Medication " + l)
                .weight(10.0)
                .build();
    }
}
