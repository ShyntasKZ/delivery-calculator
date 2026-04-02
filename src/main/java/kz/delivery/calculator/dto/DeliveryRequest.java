package kz.delivery.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на расчёт стоимости доставки")
public record DeliveryRequest(

        @Schema(description = "Дистанция в километрах", example = "450", minimum = "1", maximum = "5000")
        @NotNull(message = "distanceKm is required")
        @Min(value = 1, message = "distanceKm must be at least 1")
        @Max(value = 5000, message = "distanceKm must be at most 5000")
        Integer distanceKm,

        @Schema(description = "Вес груза в тоннах", example = "12.5", minimum = "0.1", maximum = "120")
        @NotNull(message = "weightTon is required")
        @DecimalMin(value = "0.1", message = "weightTon must be at least 0.1")
        @DecimalMax(value = "120", message = "weightTon must be at most 120")
        Double weightTon,

        @Schema(description = "Тип груза: STANDARD, FRAGILE, OVERSIZED", example = "FRAGILE")
        @NotNull(message = "cargoType is required")
        CargoType cargoType,

        @Schema(description = "Срочная доставка", example = "true")
        @NotNull(message = "isUrgent is required")
        Boolean isUrgent
) {
}
