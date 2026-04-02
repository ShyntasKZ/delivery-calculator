package kz.delivery.calculator;

import kz.delivery.calculator.dto.CargoType;
import kz.delivery.calculator.dto.DeliveryRequest;
import kz.delivery.calculator.dto.DeliveryResponse;
import kz.delivery.calculator.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryServiceTest {

    private DeliveryService service;

    @BeforeEach
    void setUp() {
        service = new DeliveryService();
    }

    // ───────────── Основной сценарий из ТЗ ─────────────

    @Test
    @DisplayName("Пример из ТЗ: 450 км, 12.5 т, FRAGILE, urgent → 58500 KZT")
    void shouldMatchSpecExample() {
        var request = new DeliveryRequest(450, 12.5, CargoType.FRAGILE, true);
        DeliveryResponse response = service.calculate(request);

        assertEquals(45_000L, response.basePrice());
        assertEquals(9_000L, response.urgentSurcharge());
        assertEquals(4_500L, response.cargoTypeSurcharge());
        assertEquals(58_500L, response.totalPrice());
        assertEquals("KZT", response.currency());
    }

    // ───────────── Типы груза ─────────────

    @Nested
    @DisplayName("Надбавки по типу груза")
    class CargoTypeSurchargeTests {

        @Test
        @DisplayName("STANDARD — надбавка 0%")
        void standardNoSurcharge() {
            var request = new DeliveryRequest(100, 10.0, CargoType.STANDARD, false);
            DeliveryResponse response = service.calculate(request);

            // basePrice = 100 * 10 * 8 = 8000
            assertEquals(8_000L, response.basePrice());
            assertEquals(0L, response.cargoTypeSurcharge());
            assertEquals(0L, response.urgentSurcharge());
            assertEquals(8_000L, response.totalPrice());
        }

        @Test
        @DisplayName("FRAGILE — надбавка 10%")
        void fragileSurcharge() {
            var request = new DeliveryRequest(100, 10.0, CargoType.FRAGILE, false);
            DeliveryResponse response = service.calculate(request);

            assertEquals(8_000L, response.basePrice());
            assertEquals(800L, response.cargoTypeSurcharge());
            assertEquals(8_800L, response.totalPrice());
        }

        @Test
        @DisplayName("OVERSIZED — надбавка 25%")
        void oversizedSurcharge() {
            var request = new DeliveryRequest(100, 10.0, CargoType.OVERSIZED, false);
            DeliveryResponse response = service.calculate(request);

            assertEquals(8_000L, response.basePrice());
            assertEquals(2_000L, response.cargoTypeSurcharge());
            assertEquals(10_000L, response.totalPrice());
        }
    }

    // ───────────── Срочность ─────────────

    @Nested
    @DisplayName("Надбавка за срочность")
    class UrgencyTests {

        @Test
        @DisplayName("Срочная доставка — +20% от базовой")
        void urgentAdds20Percent() {
            var request = new DeliveryRequest(200, 5.0, CargoType.STANDARD, true);
            DeliveryResponse response = service.calculate(request);

            // basePrice = 200 * 5 * 8 = 8000
            assertEquals(8_000L, response.basePrice());
            assertEquals(1_600L, response.urgentSurcharge());
            assertEquals(9_600L, response.totalPrice());
        }

        @Test
        @DisplayName("Не срочная — надбавка 0")
        void notUrgentNoSurcharge() {
            var request = new DeliveryRequest(200, 5.0, CargoType.STANDARD, false);
            DeliveryResponse response = service.calculate(request);

            assertEquals(0L, response.urgentSurcharge());
        }
    }

    // ───────────── Граничные значения ─────────────

    @Nested
    @DisplayName("Граничные значения")
    class BoundaryTests {

        @Test
        @DisplayName("Минимальные значения: 1 км, 0.1 т")
        void minimumValues() {
            var request = new DeliveryRequest(1, 0.1, CargoType.STANDARD, false);
            DeliveryResponse response = service.calculate(request);

            // basePrice = 1 * 0.1 * 8 = 0.8 → округление до 1
            assertEquals(1L, response.basePrice());
            assertEquals(1L, response.totalPrice());
        }

        @Test
        @DisplayName("Максимальные значения: 5000 км, 120 т")
        void maximumValues() {
            var request = new DeliveryRequest(5000, 120.0, CargoType.STANDARD, false);
            DeliveryResponse response = service.calculate(request);

            // basePrice = 5000 * 120 * 8 = 4_800_000
            assertEquals(4_800_000L, response.basePrice());
            assertEquals(4_800_000L, response.totalPrice());
        }

        @Test
        @DisplayName("Все надбавки вместе: OVERSIZED + urgent")
        void allSurchargesCombined() {
            var request = new DeliveryRequest(1000, 20.0, CargoType.OVERSIZED, true);
            DeliveryResponse response = service.calculate(request);

            // basePrice = 1000 * 20 * 8 = 160_000
            // urgent   = 160_000 * 0.20 = 32_000
            // oversized = 160_000 * 0.25 = 40_000
            // total = 160_000 + 32_000 + 40_000 = 232_000
            assertEquals(160_000L, response.basePrice());
            assertEquals(32_000L, response.urgentSurcharge());
            assertEquals(40_000L, response.cargoTypeSurcharge());
            assertEquals(232_000L, response.totalPrice());
        }
    }
}
