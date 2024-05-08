package com.rtumirea.KazakovIG.cursework.domain.dto;

import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {

    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ScheduleStatus status;

    private OrderEntity orderEntity;

}
