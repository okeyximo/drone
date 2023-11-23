package com.okey.drone.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String serialNumber;
    private String model;
    private double weightLimit;
    private int batteryCapacity;
    @Enumerated(EnumType.STRING)
    private DroneState state;

    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL)
    private List<Medication> loadedMedications;


    public void setLoadedMedications(List<Medication> droneMedications) {
        if (this.loadedMedications == null) {
            this.loadedMedications = droneMedications;
        } else {
            this.loadedMedications.addAll(droneMedications);
        }
    }

    public void setState(DroneState droneState) {
        this.state = droneState;
    }
}

