package com.matdongsan.api.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationTimeGetDto {

    @Schema(description = "매물 ID", example = "101")
    private Long propertyId;

    @Schema(description = "예약 일자", example = "2025-06-22")
    private LocalDate reservedDate;
}
