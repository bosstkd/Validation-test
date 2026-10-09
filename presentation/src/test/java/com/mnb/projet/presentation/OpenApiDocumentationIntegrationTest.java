package com.mnb.projet.presentation;

import com.mnb.projet.spring.ApplicationConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Test d'intégration de la documentation OpenAPI (Swagger) exposée par la couche présentation.
 */
@SpringBootTest(
        classes = ApplicationConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class OpenApiDocumentationIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void apiDocs_exposeLesInformationsDeLApi() {
        webTestClient.get().uri("/v3/api-docs")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.info.title").isEqualTo("Mnb API")
                .jsonPath("$.tags[0].name").isEqualTo("Mnb");
    }

    @Test
    void apiDocs_documenteTousLesEndpoints() {
        webTestClient.get().uri("/v3/api-docs")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.paths['/mnb/hello'].get.summary").isEqualTo("Hello")
                .jsonPath("$.paths['/mnb/badRequest'].get.responses['400']").exists()
                .jsonPath("$.paths['/mnb/serverError'].get.responses['500']").exists()
                .jsonPath("$.paths['/mnb/notFound'].get.responses['404']").exists()
                .jsonPath("$.paths['/mnb/create'].post.summary").isEqualTo("Créer un mnb")
                .jsonPath("$.paths['/mnb/find'].get.summary").isEqualTo("Rechercher un mnb");
    }

    @Test
    void apiDocs_documenteLeContratDeCreation() {
        webTestClient.get().uri("/v3/api-docs")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.paths['/mnb/create'].post.requestBody.content['application/json'].schema.$ref")
                .isEqualTo("#/components/schemas/MnbModelDTO")
                .jsonPath("$.paths['/mnb/create'].post.responses['200']").exists()
                .jsonPath("$.paths['/mnb/create'].post.responses['400'].content['application/json'].schema.$ref")
                .isEqualTo("#/components/schemas/ErrorMessage")
                .jsonPath("$.components.schemas.MnbModelDTO.properties.adresse.$ref")
                .isEqualTo("#/components/schemas/AdresseExampleDTO");
    }

    @Test
    void apiDocs_documenteLeContratDeRecherche() {
        webTestClient.get().uri("/v3/api-docs")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.paths['/mnb/find'].get.parameters[0].name").isEqualTo("email")
                .jsonPath("$.paths['/mnb/find'].get.parameters[0].required").isEqualTo(true)
                .jsonPath("$.paths['/mnb/find'].get.responses['200']").exists()
                .jsonPath("$.paths['/mnb/find'].get.responses['400']").exists()
                .jsonPath("$.paths['/mnb/find'].get.responses['404']").exists();
    }

    @Test
    void swaggerUi_estAccessible() {
        webTestClient.get().uri("/swagger-ui/index.html")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML);
    }
}
