package com.matdongsan.api.dto.reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationTimeGetDto {
    private Long propertyId;
    private LocalDate reservedDate;
}
