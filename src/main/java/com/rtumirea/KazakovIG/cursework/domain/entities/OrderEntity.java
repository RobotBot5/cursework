package com.rtumirea.KazakovIG.cursework.domain.entities;

import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import jakarta.persistence.*;
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
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "car_id")
    private CarEntity carEntity;

    @ManyToMany
    @JoinTable(
            name = "order_service",
            joinColumns = { @JoinColumn(name = "order_id") },
            inverseJoinColumns = { @JoinColumn(name = "service_id") }
    )
    private Set<ServiceEntity> serviceEntity = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "automech_id")
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status")
    private OrderStatus orderStatus;

    @JoinColumn(name = "working_time")
    private Integer workingTime;

    @JoinColumn(name = "details_waiting")
    private Integer detailsWaiting;
}
