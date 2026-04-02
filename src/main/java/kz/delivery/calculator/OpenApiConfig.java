package kz.delivery.calculator;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI deliveryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery Calculator API")
                        .description("API для расчёта стоимости доставки грузов")
                        .version("1.0.0"));
    }
}
