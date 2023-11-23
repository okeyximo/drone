package com.okey.drone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicationRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    @Pattern(regexp = "[a-zA-Z0-9_-]+", message = "Name can only contain letters, numbers, '-', and '_'")
    private String name;

    @NotNull(message = "Weight is required")
    private Double weight;

    @NotBlank(message = "Code is required")
    @Pattern(regexp = "[A-Z0-9_]+", message = "Code can only contain upper case letters, numbers, and '_'")
    private String code;

    private String image;
}
