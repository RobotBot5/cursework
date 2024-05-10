package com.rtumirea.KazakovIG.cursework.domain.entities;

import com.rtumirea.KazakovIG.cursework.domain.enums.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "services1")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "service_type")
    private ServiceType serviceType;

    @Column(unique = true)
    private String name;

    private Integer price;

    private Boolean deletable = false;
}
