package kz.delivery.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.delivery.calculator.dto.DeliveryRequest;
import kz.delivery.calculator.dto.DeliveryResponse;
import kz.delivery.calculator.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
@Tag(name = "Delivery", description = "Расчёт стоимости доставки")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/calculate")
    @Operation(
            summary = "Рассчитать стоимость доставки",
            description = "Формула: basePrice = distanceKm × weightTon × 8. "
                    + "Срочность +20%, FRAGILE +10%, OVERSIZED +25%, STANDARD 0% (надбавки от базовой).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = DeliveryRequest.class),
                            examples = @ExampleObject(
                                    name = "Пример",
                                    value = "{\"distanceKm\": 450, \"weightTon\": 12.5, \"cargoType\": \"FRAGILE\", \"isUrgent\": true}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный расчёт",
                            content = @Content(schema = @Schema(implementation = DeliveryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                            content = @Content)
            }
    )
    public ResponseEntity<DeliveryResponse> calculate(@Valid @RequestBody DeliveryRequest request) {
        return ResponseEntity.ok(deliveryService.calculate(request));
    }
}
