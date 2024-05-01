package com.okey.drone.dto;

import com.okey.drone.entity.BoxState;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoxRequest {

    @NotBlank(message = "Serial number is required")
    @Size(max = 10, message = "TxRef cannot exceed 10 characters")
    private String txRef;

    @NotBlank(message = "Model is required")
    @Pattern(regexp = "Lightweight|Middleweight|Cruiserweight|Heavyweight", message = "Invalid model. Must be one of: Lightweight, Middleweight, Cruiserweight, Heavyweight")
    private String model;

    @Positive(message = "Weight limit must be a positive value")
    @Max(value = 500, message = "Weight limit cannot exceed 500 grams")
    private double weightLimit;
    @Positive(message = "Battery capacity must be a positive value")
    @Max(value = 100, message = "Battery capacity cannot exceed 100%")
    private int batteryCapacity;

    private BoxState state;

}
