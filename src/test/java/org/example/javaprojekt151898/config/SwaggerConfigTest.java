package org.example.javaprojekt151898.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {

    @Test
    void shouldCreateOpenAPIBean() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Job Application System API");
        assertThat(openAPI.getComponents().getSecuritySchemes()).containsKey("Bearer Authentication");
    }
} 