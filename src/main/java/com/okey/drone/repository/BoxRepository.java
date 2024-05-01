package com.okey.drone.repository;


import com.okey.drone.entity.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {
    Optional<Box> findBytxRef(String txRef);

    @Query("SELECT d FROM Box d WHERE d.state = 'IDLE' OR d.state = 'LOADING' AND d.batteryCapacity >= ?1")
    List<Box> findBoxByStateAndBatteryCapacityGreaterThanEqual(int i);
}
