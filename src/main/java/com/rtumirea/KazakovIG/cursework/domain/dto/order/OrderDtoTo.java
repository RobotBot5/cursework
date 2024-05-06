package com.rtumirea.KazakovIG.cursework.domain.dto.order;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.ServiceEntity;
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
public class OrderDtoTo {

    private Long id;

    private CarEntity carEntity;

    private Set<ServiceEntity> serviceEntity = new HashSet<>();

    private UserEntity userEntity;

    private OrderStatus orderStatus;

}
