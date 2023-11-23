package com.okey.drone.repository;


import com.okey.drone.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findBySerialNumber(String droneSerialNumber);

    @Query("SELECT d FROM Drone d WHERE d.state = 'IDLE' OR d.state = 'LOADING' AND d.batteryCapacity >= ?1")
    List<Drone> findDroneByStateAndBatteryCapacityGreaterThanEqual(int i);
}
