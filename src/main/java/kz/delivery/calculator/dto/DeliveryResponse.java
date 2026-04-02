package kz.delivery.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Результат расчёта стоимости доставки")
public record DeliveryResponse(

        @Schema(description = "Базовая стоимость (distanceKm × weightTon × 8)", example = "45000")
        long basePrice,

        @Schema(description = "Надбавка за срочность (+20% от базовой)", example = "9000")
        long urgentSurcharge,

        @Schema(description = "Надбавка за тип груза", example = "4500")
        long cargoTypeSurcharge,

        @Schema(description = "Итоговая стоимость", example = "58500")
        long totalPrice,

        @Schema(description = "Валюта", example = "KZT")
        String currency
) {
}
