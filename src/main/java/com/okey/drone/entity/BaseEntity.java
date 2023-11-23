package com.okey.drone.entity;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

public abstract class BaseEntity {
    @CreatedDate
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
