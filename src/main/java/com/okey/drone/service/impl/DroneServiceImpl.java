package com.okey.drone.service.impl;

import com.okey.drone.dto.BaseResponse;
import com.okey.drone.dto.DroneRequest;
import com.okey.drone.dto.DroneResponse;
import com.okey.drone.dto.MedicationRequest;
import com.okey.drone.dto.MedicationResponse;
import com.okey.drone.entity.Drone;
import com.okey.drone.entity.DroneState;
import com.okey.drone.entity.Medication;
import com.okey.drone.exception.InvalidRequestException;
import com.okey.drone.exception.ResourceNotFoundException;
import com.okey.drone.repository.DroneRepository;
import com.okey.drone.repository.MedicationRepository;
import com.okey.drone.service.DroneService;
import com.okey.drone.utils.DroneMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final DroneMapper droneMapper;
    @Override
    public BaseResponse<DroneResponse> registerDrone(DroneRequest droneRequest) {
        Drone drone = droneMapper.mapToDrone(droneRequest);
        droneRepository.save(drone);
        return BaseResponse.<DroneResponse>builder()
                .status(201)
                .message("Drone registered successfully")
                .data(droneMapper.mapToDroneResponse(drone))
                .build();
    }

    @Override
    @Transactional
    public BaseResponse<DroneResponse> loadMedications(String droneSerialNumber, List<MedicationRequest> medicationsToBeLoaded) {
        var drone = droneRepository.findBySerialNumber(droneSerialNumber).orElseThrow(()-> new ResourceNotFoundException("Drone not found"));
        if (drone.getBatteryCapacity() < 25) {
            log.error("Drone with serial number {} battery is low", droneSerialNumber);
            throw new InvalidRequestException("Drone battery is low");
        }
        var totalMedicationWeight = calculateTotalWeight(medicationsToBeLoaded, drone.getLoadedMedications());

        if (totalMedicationWeight > drone.getWeightLimit()) {
            log.error("Medication weight is more than drone weight limit");
            throw new InvalidRequestException("Medication weight is more than drone weight limit");
        }

        List<Medication> droneMedications = droneMapper.mapToMedicationList(medicationsToBeLoaded, drone);
        medicationRepository.saveAll(droneMedications);
        drone.setState(DroneState.LOADED);
        drone.setLoadedMedications(droneMedications);
        drone  = droneRepository.save(drone);

        log.info("Medications loaded successfully");
        return BaseResponse.<DroneResponse>builder()
                .status(201)
                .message("Medications loaded successfully")
                .data(droneMapper.mapToDroneResponse(drone))
                .build();
    }



    @Override
    @Transactional
    public List<MedicationResponse> getLoadedMedications(String droneSerialNumber) {
        var drone = droneRepository.findBySerialNumber(droneSerialNumber).orElseThrow(()-> new ResourceNotFoundException("Drone not found"));
        List<Medication> droneMedications = drone.getLoadedMedications();
        return droneMapper.mapToMedicationResponseList(droneMedications);

    }

    @Override
    public List<DroneResponse> getAvailableDronesForLoading() {
        var availableDrone = droneRepository.findDroneByStateAndBatteryCapacityGreaterThanEqual( 25);
        return droneMapper.mapToDroneResponseList(availableDrone);
    }

    @Override
    public BaseResponse<Integer> getDroneBatteryLevel(String droneSerialNumber) {
        var drone = droneRepository.findBySerialNumber(droneSerialNumber).orElseThrow(()-> new ResourceNotFoundException("Drone not found"));
        return BaseResponse.<Integer>builder()
                .status(200)
                .message("Drone battery level")
                .data(drone.getBatteryCapacity())
                .build();
    }

    private Double calculateTotalWeight(List<MedicationRequest> medicationRequests, List<Medication> loadedMedications) {
        double totalWeightMedicationRequests = medicationRequests.stream()
                .mapToDouble(MedicationRequest::getWeight)
                .sum();

        double totalWeightDroneMedications = loadedMedications.stream()
                .mapToDouble(Medication::getWeight)
                .sum();

        return totalWeightMedicationRequests + totalWeightDroneMedications;
    }
}
