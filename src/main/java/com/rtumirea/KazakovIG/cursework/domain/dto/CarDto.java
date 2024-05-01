package com.rtumirea.KazakovIG.cursework.domain.dto;

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

    private String make;

    private String model;

    private Integer year;

    private UserDto userDto;

}
