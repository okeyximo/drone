package com.okey.drone.service;

import com.okey.drone.dto.BaseResponse;
import com.okey.drone.dto.DroneRequest;
import com.okey.drone.dto.DroneResponse;
import com.okey.drone.dto.MedicationRequest;
import com.okey.drone.dto.MedicationResponse;

import java.util.List;
import java.util.UUID;

public interface DroneService {
    BaseResponse<DroneResponse> registerDrone(DroneRequest drone);

    BaseResponse<DroneResponse> loadMedications(String droneSerialNumber, List<MedicationRequest> medications);

    List<MedicationResponse> getLoadedMedications(String droneSerialNumber);

    List<DroneResponse> getAvailableDronesForLoading();

    BaseResponse<Integer> getDroneBatteryLevel(String droneSerialNumber);
}
