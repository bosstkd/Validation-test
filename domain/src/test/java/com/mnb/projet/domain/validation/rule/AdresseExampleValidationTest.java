package com.mnb.projet.domain.validation.rule;

import com.mnb.projet.domain.model.AdresseExample;
import com.mnb.projet.domain.validation.exception.ValidationException;
import com.mnb.projet.domain.validation.exception.ValidationError;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class AdresseExampleValidationTest {

    // -------------------------------------------------------------------------
    // Builder helpers
    // -------------------------------------------------------------------------

    private static AdresseExample validAdresse() {
        return AdresseExample.builder()
                .voie("12 rue de la Paix")
                .codePostal("75001")
                .ville("Paris")
                .pays("France")
                .build();
    }

    // -------------------------------------------------------------------------
    // Happy path
    // -------------------------------------------------------------------------

    @Test
    void passes_for_fully_valid_adresse() {
        assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(validAdresse()));
    }

    @Test
    void passes_when_ville_is_null() {
        AdresseExample adresse = validAdresse();
        adresse.setVille(null);
        assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(adresse));
    }

    // -------------------------------------------------------------------------
    // voie
    // -------------------------------------------------------------------------

    @Nested
    class VoieTests {

        @Test
        void fails_when_voie_is_null() {
            AdresseExample adresse = validAdresse();
            adresse.setVoie(null);

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class);

            assertThat(champsOf(ex)).contains("voie");
        }

        @Test
        void fails_when_voie_is_empty() {
            AdresseExample adresse = validAdresse();
            adresse.setVoie("");

            assertThat(catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class))
                    .isNotNull();
        }

        @Test
        void fails_when_voie_is_whitespace_only() {
            AdresseExample adresse = validAdresse();
            adresse.setVoie("   ");

            assertThat(catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class))
                    .isNotNull();
        }
    }

    // -------------------------------------------------------------------------
    // codePostal
    // -------------------------------------------------------------------------

    @Nested
    class CodePostalTests {

        @Test
        void fails_when_codePostal_is_null() {
            AdresseExample adresse = validAdresse();
            adresse.setCodePostal(null);

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class);

            assertThat(champsOf(ex)).contains("codePostal");
        }

        @Test
        void fails_when_codePostal_is_blank() {
            AdresseExample adresse = validAdresse();
            adresse.setCodePostal("  ");

            assertThat(catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class))
                    .isNotNull();
        }
    }

    // -------------------------------------------------------------------------
    // pays
    // -------------------------------------------------------------------------

    @Nested
    class PaysTests {

        @Test
        void fails_when_pays_is_null() {
            AdresseExample adresse = validAdresse();
            adresse.setPays(null);

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class);

            assertThat(champsOf(ex)).contains("pays");
        }

        @Test
        void fails_when_pays_is_blank() {
            AdresseExample adresse = validAdresse();
            adresse.setPays("");

            assertThat(catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class))
                    .isNotNull();
        }
    }

    // -------------------------------------------------------------------------
    // @AssertTrue — hasCorrectVille (ville/codePostal coherence)
    // -------------------------------------------------------------------------

    @Nested
    class HasCorrectVilleTests {

        @Test
        void passes_when_69001_with_Lyon() {
            AdresseExample adresse = validAdresse();
            adresse.setCodePostal("69001");
            adresse.setVille("Lyon");
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(adresse));
        }

        @Test
        void passes_when_69001_with_Lyon_ignoring_case() {
            AdresseExample adresse = validAdresse();
            adresse.setCodePostal("69001");
            adresse.setVille("LYON");
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(adresse));
        }

        @Test
        void fails_when_69001_with_wrong_ville() {
            AdresseExample adresse = validAdresse();
            adresse.setCodePostal("69001");
            adresse.setVille("Paris");

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class);

            assertThat(messagesOf(ex)).anyMatch(m -> m.contains("code-postal") && m.contains("ville"));
        }

        @Test
        void passes_when_ville_is_blank_regardless_of_code_postal() {
            AdresseExample adresse = validAdresse();
            adresse.setCodePostal("69001");
            adresse.setVille("");
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(adresse));
        }

        @Test
        void passes_when_code_postal_is_not_69001_with_any_ville() {
            AdresseExample adresse = validAdresse();
            adresse.setCodePostal("75001");
            adresse.setVille("Marseille"); // mismatch but rule only checks 69001
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(adresse));
        }
    }

    // -------------------------------------------------------------------------
    // @AssertTrue — hasValidCodePostalPourPays (replaces AdresseInfosValid logic)
    // -------------------------------------------------------------------------

    @Nested
    class HasValidCodePostalPourPaysTests {

        @Test
        void passes_when_France_and_valid_5_digit_code() {
            AdresseExample adresse = validAdresse();
            adresse.setPays("France");
            adresse.setCodePostal("13001"); // valid 5-digit French code, not 69001 to avoid hasCorrectVille rule
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(adresse));
        }

        @Test
        void fails_when_France_and_code_postal_is_4_digits() {
            AdresseExample adresse = validAdresse();
            adresse.setPays("France");
            adresse.setCodePostal("6900");

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class);

            assertThat(messagesOf(ex)).anyMatch(m -> m.contains("validation adresse"));
        }

        @Test
        void fails_when_France_and_code_postal_contains_letters() {
            AdresseExample adresse = validAdresse();
            adresse.setPays("France");
            adresse.setCodePostal("AB123");

            assertThat(catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class))
                    .isNotNull();
        }

        @Test
        void passes_when_pays_is_not_France() {
            // non-French countries are not subject to the 5-digit format rule
            AdresseExample adresse = validAdresse();
            adresse.setPays("Belgique");
            adresse.setCodePostal("1000"); // 4-digit Belgian code — valid here
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(adresse));
        }

        @Test
        void passes_case_insensitive_for_france() {
            AdresseExample adresse = validAdresse();
            adresse.setPays("FRANCE");
            adresse.setCodePostal("75001");
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(adresse));
        }

        @Test
        void passes_when_code_postal_is_blank_regardless_of_pays() {
            AdresseExample adresse = validAdresse();
            adresse.setCodePostal(null);
            // @NotBlank on codePostal fires, but hasValidCodePostalPourPays returns true on blank
            // so no extra violation from @AssertTrue
            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(adresse), ValidationException.class);

            assertThat(ex.getErreurs()).hasSize(1);
            assertThat(champsOf(ex)).containsExactly("codePostal");
        }
    }

    // -------------------------------------------------------------------------
    // All required fields together
    // -------------------------------------------------------------------------

    @Test
    void collects_all_violations_for_empty_adresse() {
        AdresseExample adresse = AdresseExample.builder().build();

        ValidationException ex = catchThrowableOfType(
                () -> ReflectionValidator.validate(adresse), ValidationException.class);

        // voie, codePostal, pays must all be reported
        assertThat(ex.getErreurs()).hasSizeGreaterThanOrEqualTo(3);
        assertThat(champsOf(ex)).contains("voie", "codePostal", "pays");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static Set<String> messagesOf(ValidationException ex) {
        return ex.getErreurs().stream()
                .map(ValidationError::message)
                .collect(Collectors.toSet());
    }

    private static Set<String> champsOf(ValidationException ex) {
        return ex.getErreurs().stream()
                .map(ValidationError::champ)
                .map(champ -> champ != null ? champ.substring(champ.lastIndexOf('.') + 1) : "")
                .collect(Collectors.toSet());
    }
}