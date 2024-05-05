package com.rtumirea.KazakovIG.cursework.domain.entities;

import com.rtumirea.KazakovIG.cursework.domain.dto.UserDto;
import com.rtumirea.KazakovIG.cursework.domain.enums.AudiBodyType;
import com.rtumirea.KazakovIG.cursework.domain.enums.BmwBodyType;
import com.rtumirea.KazakovIG.cursework.domain.enums.CarMake;
import com.rtumirea.KazakovIG.cursework.domain.enums.MercedesBodyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

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

    @Column(unique = true)
    private String stateNumber;

    @Enumerated(EnumType.STRING)
    private CarMake make;

    private String bodyType;

    private Integer year;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @AssertTrue(message = "Invalid body type for the specified make")
    public boolean isBodyTypeValidForMake() {
        if (make == CarMake.BMW) {
            return Arrays.stream(BmwBodyType.values()).anyMatch(b -> Objects.equals(b.toString(), bodyType));
        } else if (make == CarMake.MERCEDES) {
            return Arrays.stream(MercedesBodyType.values()).anyMatch(b -> Objects.equals(b.toString(), bodyType));
        } else if (make == CarMake.AUDI) {
            return Arrays.stream(AudiBodyType.values()).anyMatch(b -> Objects.equals(b.toString(), bodyType));
        }
        return false;
    }
}
