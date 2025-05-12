package br.com.mounit.test.order_service;

import br.com.mounit.test.order_service.infra.SwaggerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class InfraTest {

    @Autowired
    private SwaggerConfig swaggerConfig;

    @Test
    void shouldCreateDocketBean() {
        // when
        Docket docket = swaggerConfig.api();

        // then
        assertNotNull(docket, "Docket should not be null");

        // Verifica se o tipo de documentação está correto
        assertEquals("Document type should be Swagger 2",
                DocumentationType.SWAGGER_2, docket.getDocumentationType()
                );

        // Verifica se o seletor de APIs está configurado
        assertNotNull(docket.select().build(),
                "API selector should be configured");


    }

}
