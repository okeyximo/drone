package com.okey.drone.dto;

import com.okey.drone.entity.BoxState;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoxResponse {
    private String txRef;
    private String model;
    private double weightLimit;
    private int batteryCapacity;
    private BoxState state;
    private List<ItemResponse> loadedItems;
}
