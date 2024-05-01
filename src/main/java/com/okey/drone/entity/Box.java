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

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String txRef;
    private String model;
    private double weightLimit;
    private int batteryCapacity;
    @Enumerated(EnumType.STRING)
    private BoxState state;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL)
    private List<Item> loadedItems;


    public void setLoadedItems(List<Item> droneItems) {
        if (this.loadedItems == null) {
            this.loadedItems = droneItems;
        } else {
            this.loadedItems.addAll(droneItems);
        }
    }

    public void setState(BoxState boxState) {
        this.state = boxState;
    }
}

