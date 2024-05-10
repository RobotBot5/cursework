package com.rtumirea.KazakovIG.cursework.domain.dto;

import com.rtumirea.KazakovIG.cursework.domain.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDto {

    private Long id;

    private ServiceType serviceType;

    private String name;

    private Integer price;

    private Boolean deletable = false;
}
