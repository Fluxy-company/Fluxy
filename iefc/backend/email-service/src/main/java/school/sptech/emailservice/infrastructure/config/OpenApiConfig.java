package school.sptech.emailservice.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String ESQUEMA_API_KEY = "ApiKeyAuth";

    @Bean
    public OpenAPI emailServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Email Service API")
                        .version("v1")
                        .description("Microservico responsavel pelo envio assincrono de e-mails "
                                + " para o ecossistema IEFC."))
                .components(new Components().addSecuritySchemes(ESQUEMA_API_KEY,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-API-KEY")))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_API_KEY));
    }
}
