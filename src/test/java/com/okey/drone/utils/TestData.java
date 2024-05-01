package com.okey.drone.utils;

import com.okey.drone.dto.BoxRequest;
import com.okey.drone.dto.BoxResponse;
import com.okey.drone.dto.ItemResponse;
import com.okey.drone.entity.Box;
import com.okey.drone.entity.BoxState;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TestData {

    public BoxRequest getDroneRequest() {
        return BoxRequest.builder()
                .model("Test Drone")
                .weightLimit(100)
                .batteryCapacity(100)
                .state(BoxState.IDLE)
                .build();
    }

    public Box getDrone() {
        return Box.builder()
                .model("Test Drone")
                .txRef(UUID.randomUUID().toString())
                .weightLimit(100)
                .batteryCapacity(100)
                .state(BoxState.IDLE)
                .build();
    }

    public BoxResponse getDroneResponse(Box box) {
        return BoxResponse.builder()
                .txRef(box.getTxRef())
                .weightLimit(box.getWeightLimit())
                .state(box.getState())
                .batteryCapacity(box.getBatteryCapacity())
                .build();
    }

    public static ItemResponse getMedicationResponse(long l) {
        return ItemResponse.builder()
                .name("Medication " + l)
                .weight(10.0)
                .build();
    }
}
