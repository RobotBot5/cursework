package com.rtumirea.KazakovIG.cursework.domain.dto.order;

import com.rtumirea.KazakovIG.cursework.domain.entities.UserEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDtoFrom {

    private Long id;

    private Long carId;

    private Set<Long> serviceIds = new HashSet<>();

    private UserEntity userEntity;

    private OrderStatus orderStatus;
}
