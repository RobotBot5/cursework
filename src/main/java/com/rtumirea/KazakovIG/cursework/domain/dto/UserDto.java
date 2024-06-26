package com.rtumirea.KazakovIG.cursework.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String phoneNumber;

    private String name;

    private String password;

    private String roles;

    private Integer ordersNum;

}
