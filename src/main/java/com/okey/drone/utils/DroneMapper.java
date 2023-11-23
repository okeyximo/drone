package com.okey.drone.utils;

import com.okey.drone.dto.DroneRequest;
import com.okey.drone.dto.DroneResponse;
import com.okey.drone.dto.MedicationRequest;
import com.okey.drone.dto.MedicationResponse;
import com.okey.drone.entity.Drone;
import com.okey.drone.entity.Medication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DroneMapper {

    public Drone mapToDrone(DroneRequest droneRequest) {
        return Drone.builder()
                .model(droneRequest.getModel())
                .serialNumber(droneRequest.getSerialNumber())
                .weightLimit(droneRequest.getWeightLimit())
                .state(droneRequest.getState())
                .batteryCapacity(droneRequest.getBatteryCapacity())
                .build();

    }

    public List<MedicationResponse> mapToMedicationResponseList(List<Medication> droneMedications) {
        return droneMedications.stream()
                .map(this::mapToMedicationResponse)
                .collect(Collectors.toList());
    }

    public List<Medication> mapToMedicationList(List<MedicationRequest> medicationsToBeLoaded, Drone drone) {
        return medicationsToBeLoaded.stream()
                .map(medicationRequest -> mapToMedication(medicationRequest, drone))
                .collect(Collectors.toList());
    }

    private Medication mapToMedication(MedicationRequest medicationRequest, Drone drone) {
        return Medication.builder()
                .name(medicationRequest.getName())
                .weight(medicationRequest.getWeight())
                .code(medicationRequest.getCode())
                .image(medicationRequest.getImage())
                .drone(drone)
                .build();
    }

    private MedicationResponse mapToMedicationResponse(Medication medication) {
        return MedicationResponse.builder()
                .name(medication.getName())
                .weight(medication.getWeight())
                .code(medication.getCode())
                .image(medication.getImage())
                .build();
    }


    public List<DroneResponse> mapToDroneResponseList(List<Drone> availableDrone) {
        return availableDrone.stream()
                .map(this::mapToDroneResponse)
                .collect(Collectors.toList());
    }

    public DroneResponse mapToDroneResponse(Drone drone) {
        return DroneResponse.builder()
                .batteryCapacity(drone.getBatteryCapacity())
                .model(drone.getModel())
                .serialNumber(drone.getSerialNumber())
                .state(drone.getState())
                .weightLimit(drone.getWeightLimit())
                .loadedMedications(mapToMedicationResponseList(drone.getLoadedMedications()))
                .build();
    }
}
