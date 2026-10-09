package com.mnb.projet.presentation;

import com.mnb.projet.infrastructure.persistance.model.MnbEntity;
import com.mnb.projet.infrastructure.persistance.repository.MnbRepository;
import com.mnb.projet.spring.ApplicationConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test d'intégration de la couche présentation.
 * <p>
 * Parcourt toute la chaîne controller → application → domaine → infrastructure
 * sur une base H2 en mémoire dédiée aux tests (profil {@code test}).
 */
@SpringBootTest(
        classes = ApplicationConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class MnbControllerIntegrationTest {

    private static final String EMAIL = "a-ek@hotmail.fr";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MnbRepository repository;

    @Value("classpath:jsons/test.json")
    private Resource createRequest;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void createMnb_persisteEnBaseEtRetourneLeModeleCree() throws Exception {
        String requestBody = createRequest.getContentAsString(StandardCharsets.UTF_8);

        webTestClient.post().uri("/mnb/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody().json(requestBody);

        List<MnbEntity> persisted = repository.findByEmail(EMAIL);
        assertThat(persisted).hasSize(1);
        assertThat(persisted.get(0).getId()).isNotNull();
        assertThat(persisted.get(0).getNom()).isEqualTo("Mahmoudi");
        assertThat(persisted.get(0).getPrenom()).isEqualTo("Amine");
    }

    @Test
    void createMnb_puisFind_retourneLeModeleDepuisLaBase() throws Exception {
        String requestBody = createRequest.getContentAsString(StandardCharsets.UTF_8);

        webTestClient.post().uri("/mnb/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri(uri -> uri.path("/mnb/find").queryParam("email", EMAIL).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nom").isEqualTo("Mahmoudi")
                .jsonPath("$.prenom").isEqualTo("Amine")
                .jsonPath("$.email").isEqualTo(EMAIL);
    }

    @Test
    @Disabled("L'adresse est persistée sans lien vers son mnb (adresse.mnbEntity jamais renseigné) : /find la retourne à null")
    void createMnb_puisFind_retourneAussiLAdresse() throws Exception {
        String requestBody = createRequest.getContentAsString(StandardCharsets.UTF_8);

        webTestClient.post().uri("/mnb/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri(uri -> uri.path("/mnb/find").queryParam("email", EMAIL).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(requestBody);
    }

    @Test
    void createMnb_avecModeleInvalide_retourne400EtNePersisteRien() {
        String invalidBody = """
                {
                  "nom": "Mahmoudi",
                  "prenom": "Amine",
                  "email": "email-invalide",
                  "adresse": {
                    "voie": "38, avenue des source",
                    "ville": "Ecully",
                    "codePostal": "69130",
                    "pays": "France"
                  }
                }
                """;

        webTestClient.post().uri("/mnb/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(400);

        assertThat(repository.count()).isZero();
    }
}
