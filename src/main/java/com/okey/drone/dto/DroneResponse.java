package com.okey.drone.dto;

import com.okey.drone.entity.DroneState;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DroneResponse {
    private String serialNumber;
    private String model;
    private double weightLimit;
    private int batteryCapacity;
    private DroneState state;
    private List<MedicationResponse> loadedMedications;
}
