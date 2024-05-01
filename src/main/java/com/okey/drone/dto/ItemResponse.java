package com.okey.drone.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponse {
    private String name;
    private Double weight;
    private String code;
    private String image;
}
