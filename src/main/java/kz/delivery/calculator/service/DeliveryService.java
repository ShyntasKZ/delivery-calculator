package kz.delivery.calculator.service;

import kz.delivery.calculator.dto.DeliveryRequest;
import kz.delivery.calculator.dto.DeliveryResponse;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    private static final double RATE_PER_KM_TON = 8.0;
    private static final double URGENT_SURCHARGE_RATE = 0.20;
    private static final String CURRENCY = "KZT";

    public DeliveryResponse calculate(DeliveryRequest request) {
        double basePrice = request.distanceKm() * request.weightTon() * RATE_PER_KM_TON;

        double urgentSurcharge = request.isUrgent() ? basePrice * URGENT_SURCHARGE_RATE : 0.0;
        double cargoTypeSurcharge = basePrice * request.cargoType().getSurchargeRate();

        double totalPrice = basePrice + urgentSurcharge + cargoTypeSurcharge;

        return new DeliveryResponse(
                Math.round(basePrice),
                Math.round(urgentSurcharge),
                Math.round(cargoTypeSurcharge),
                Math.round(totalPrice),
                CURRENCY
        );
    }
}
