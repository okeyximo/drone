package com.okey.drone.service.impl;

import com.okey.drone.dto.BaseResponse;
import com.okey.drone.dto.DroneRequest;
import com.okey.drone.dto.DroneResponse;
import com.okey.drone.dto.MedicationRequest;
import com.okey.drone.dto.MedicationResponse;
import com.okey.drone.entity.Drone;
import com.okey.drone.entity.DroneState;
import com.okey.drone.entity.Medication;
import com.okey.drone.exception.ResourceNotFoundException;
import com.okey.drone.repository.DroneRepository;
import com.okey.drone.repository.MedicationRepository;
import com.okey.drone.utils.DroneMapper;
import com.okey.drone.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DroneServiceImplTest {

    @Mock
    private DroneMapper droneMapper;

    @Mock
    private DroneRepository droneRepository;

    @Mock
    MedicationRepository medicationRepository;

    @InjectMocks
    private DroneServiceImpl droneService;
    @Test
    public void testRegisterDrone_Success() {
        // Arrange
        DroneRequest droneRequest = TestData.getDroneRequest();
        Drone drone = TestData.getDrone();


        when(droneMapper.mapToDrone(droneRequest)).thenReturn(drone);
        when(droneMapper.mapToDroneResponse(drone)).thenReturn(TestData.getDroneResponse(drone));


        // Act
        BaseResponse<DroneResponse> response = droneService.registerDrone(droneRequest);

        // Assert
        verify(droneRepository, times(1)).save(drone);
        assertEquals(201, response.getStatus());
        assertEquals("Drone registered successfully", response.getMessage());
        assertEquals(drone.getSerialNumber(), response.getData().getSerialNumber());
    }

    @Test
    public void loadMedications_whenDroneBatteryIsSufficientAndMedicationWeightIsLessThanDroneWeightLimit_loadsMedicationsSuccessfully() {
        // Given
        var droneSerialNumber = UUID.randomUUID().toString();
        var drone = Drone.builder()
                .serialNumber(droneSerialNumber)
                .batteryCapacity(100)
                .weightLimit(100)
                .state(DroneState.IDLE)
                .loadedMedications(new ArrayList<>())
                .build();
        droneRepository.save(drone);

        // When
        var medicationsToBeLoaded = List.of(
                MedicationRequest.builder()
                        .name("Medication 1")
                        .weight(10.0)
                        .build(),
                MedicationRequest.builder()
                        .name("Medication 2")
                        .weight(10.0)
                        .build()
        );

        when(droneRepository.findBySerialNumber(droneSerialNumber)).thenReturn(java.util.Optional.of(drone));
        when(droneMapper.mapToMedicationList(medicationsToBeLoaded, drone)).thenReturn(List.of());
        when(droneMapper.mapToDroneResponse(drone)).thenReturn(TestData.getDroneResponse(drone));
        when(droneRepository.save(drone)).thenReturn(drone);
        when(droneRepository.save(drone)).thenReturn(drone);

        // Then
        var response = droneService.loadMedications(droneSerialNumber, medicationsToBeLoaded);

        assertEquals(201, response.getStatus());
        assertEquals("Medications loaded successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(DroneState.LOADED, drone.getState());

        verify(droneRepository, times(1)).findBySerialNumber(droneSerialNumber);
        verify(droneMapper, times(1)).mapToMedicationList(medicationsToBeLoaded, drone);
        verify(droneMapper, times(1)).mapToDroneResponse(drone);


    }

    @Test
    public void testGetLoadedMedications_WithValidDroneSerialNumber_ShouldReturnMedicationResponseList() {
        String droneSerialNumber = UUID.randomUUID().toString();
        Drone drone = new Drone();
        drone.setSerialNumber(droneSerialNumber);
        Medication medication1 = new Medication();
        medication1.setId(1L);
        Medication medication2 = new Medication();
        medication2.setId(2L);
        List<Medication> droneMedications = new ArrayList<>();
        droneMedications.add(medication1);
        droneMedications.add(medication2);
        drone.setLoadedMedications(droneMedications);
        List<MedicationResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(TestData.getMedicationResponse(1L));
        expectedResponse.add(TestData.getMedicationResponse(2L));

        when(droneRepository.findBySerialNumber(droneSerialNumber)).thenReturn(Optional.of(drone));
        when(droneMapper.mapToMedicationResponseList(droneMedications)).thenReturn(expectedResponse);

        List<MedicationResponse> actualResponse = droneService.getLoadedMedications(droneSerialNumber);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetDroneBatteryLevel() {
        String droneSerialNumber = UUID.randomUUID().toString();
        int batteryCapacity = 80;

        Drone drone = new Drone();
        drone.setSerialNumber(droneSerialNumber);
        drone.setBatteryCapacity(batteryCapacity);

        when(droneRepository.findBySerialNumber(droneSerialNumber)).thenReturn(Optional.of(drone));

        BaseResponse<Integer> response = droneService.getDroneBatteryLevel(droneSerialNumber);

        assertEquals(200, response.getStatus());
        assertEquals("Drone battery level", response.getMessage());
        assertEquals(batteryCapacity, response.getData());
    }

    @Test
    public void testGetDroneBatteryLevel_DroneNotFound() {
        String droneSerialNumber = UUID.randomUUID().toString();

        when(droneRepository.findBySerialNumber(droneSerialNumber)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> {
            droneService.getDroneBatteryLevel(droneSerialNumber);
        });
    }
}