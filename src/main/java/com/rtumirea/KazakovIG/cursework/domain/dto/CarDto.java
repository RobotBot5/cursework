package com.rtumirea.KazakovIG.cursework.domain.dto;

import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.CarMake;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDto {

    private Long id;

    private String stateNumber;

    private CarMake make;

    private String bodyType;

    private Integer year;

    private UserEntity userEntity;

    private Boolean carReady = false;
}
