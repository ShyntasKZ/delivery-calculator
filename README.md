# Delivery Calculator

REST API для расчёта стоимости доставки груза. Принимает параметры маршрута и груза, возвращает разбивку стоимости по составляющим.

## Стек

- **Java 21**
- **Spring Boot 4.0.5** (Web, Validation)
- **springdoc-openapi 3.0.2** — Swagger UI
- **JUnit 5** — юнит-тесты

## Формула расчёта

```
basePrice         = distanceKm × weightTon × 8  (KZT)
urgentSurcharge   = basePrice × 0.20  (если isUrgent = true)
cargoTypeSurcharge = basePrice × коэффициент типа груза
totalPrice        = basePrice + urgentSurcharge + cargoTypeSurcharge
```

### Коэффициенты типа груза

| Тип груза  | Надбавка |
|------------|----------|
| `STANDARD` | 0%       |
| `FRAGILE`  | 10%      |
| `OVERSIZED`| 25%      |

## Быстрый старт

**Требования:** JDK 21, Maven 3.9+

```bash
# Клонировать репозиторий
git clone <repo-url>
cd delivery-calculator

# Запустить приложение
./mvnw spring-boot:run
```

Сервер поднимается на `http://localhost:8080`.

## API

### `POST /api/delivery/calculate`

**Тело запроса:**

```json
{
  "distanceKm": 450,
  "weightTon": 12.5,
  "cargoType": "FRAGILE",
  "isUrgent": true
}
```

| Поле          | Тип     | Ограничения          | Описание                    |
|---------------|---------|----------------------|-----------------------------|
| `distanceKm`  | integer | 1 – 5000             | Расстояние в километрах     |
| `weightTon`   | double  | 0.1 – 120            | Вес груза в тоннах          |
| `cargoType`   | string  | STANDARD/FRAGILE/OVERSIZED | Тип груза             |
| `isUrgent`    | boolean | —                    | Признак срочной доставки    |

**Ответ `200 OK`:**

```json
{
  "basePrice": 45000,
  "urgentSurcharge": 9000,
  "cargoTypeSurcharge": 4500,
  "totalPrice": 58500,
  "currency": "KZT"
}
```

**Ответ `400 Bad Request`** — при нарушении ограничений валидации.

## Swagger UI

После запуска документация доступна по адресу:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Запуск тестов

```bash
./mvnw test
```

Тесты покрывают:
- эталонный пример из ТЗ (450 км, 12.5 т, FRAGILE, срочно → 58 500 KZT)
- надбавки по каждому типу груза
- надбавку за срочность
- граничные значения (минимум, максимум, все надбавки одновременно)

## Структура проекта

```
src/
├── main/java/kz/delivery/calculator/
│   ├── DeliveryCalculatorApplication.java  # точка входа
│   ├── OpenApiConfig.java                  # настройка Swagger
│   ├── controller/
│   │   └── DeliveryController.java         # POST /api/delivery/calculate
│   ├── service/
│   │   └── DeliveryService.java            # логика расчёта
│   ├── dto/
│   │   ├── DeliveryRequest.java            # входные данные
│   │   ├── DeliveryResponse.java           # результат расчёта
│   │   └── CargoType.java                  # enum типов груза
│   └── exception/
│       └── GlobalExceptionHandler.java     # обработка ошибок валидации
└── test/java/kz/delivery/calculator/
    └── DeliveryServiceTest.java
```

## Возможные доработки

Описание архитектурных изменений для хранения тарифов в БД и управления ими через админ-панель — в файле [ARCHITECTURE_ANSWER.md](ARCHITECTURE_ANSWER.md).
