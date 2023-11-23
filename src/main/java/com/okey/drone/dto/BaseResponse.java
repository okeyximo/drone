package com.okey.drone.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
public class BaseResponse <T> {
    private T data;
    private String message;
    private int status;

    public BaseResponse(T data, String message, int status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }


}
