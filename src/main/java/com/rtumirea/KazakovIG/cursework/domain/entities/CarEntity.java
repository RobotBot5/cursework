package com.rtumirea.KazakovIG.cursework.domain.entities;

import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cars")
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;

    private String model;

    private Integer year;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
