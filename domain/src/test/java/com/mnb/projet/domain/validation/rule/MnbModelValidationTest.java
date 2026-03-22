package com.mnb.projet.domain.validation.rule;

import com.mnb.projet.domain.model.AdresseExample;
import com.mnb.projet.domain.model.MnbModel;
import com.mnb.projet.domain.validation.exception.ValidationException;
import com.mnb.projet.domain.validation.exception.ValidationError;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class MnbModelValidationTest {

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

    private static MnbModel validModel() {
        return MnbModel.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .adresse(validAdresse())
                .build();
    }

    // -------------------------------------------------------------------------
    // Happy path
    // -------------------------------------------------------------------------

    @Test
    void passes_for_fully_valid_model() {
        assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(validModel()));
    }

    // -------------------------------------------------------------------------
    // nom
    // -------------------------------------------------------------------------

    @Nested
    class NomTests {

        @Test
        void fails_when_nom_is_null() {
            MnbModel model = validModel();
            model.setNom(null);

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).anyMatch(m -> m.contains("nom") && m.contains("obligatoire"));
        }

        @Test
        void fails_when_nom_is_empty() {
            MnbModel model = validModel();
            model.setNom("");

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).anyMatch(m -> m.contains("nom") && m.contains("obligatoire"));
        }

        @Test
        void fails_when_nom_contains_digits() {
            MnbModel model = validModel();
            model.setNom("Dupont123");

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).anyMatch(m -> m.contains("nom") && m.contains("valide"));
        }

        @Test
        void passes_when_nom_has_accents() {
            MnbModel model = validModel();
            model.setNom("Héloïse");
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }

        @Test
        void passes_when_nom_has_hyphen() {
            MnbModel model = validModel();
            model.setNom("Martin-Lefebvre");
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }
    }

    // -------------------------------------------------------------------------
    // prenom
    // -------------------------------------------------------------------------

    @Nested
    class PrenomTests {

        @Test
        void fails_when_prenom_is_null() {
            MnbModel model = validModel();
            model.setPrenom(null);

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).anyMatch(m -> m.contains("prenom") && m.contains("obligatoire"));
        }

        @Test
        void fails_when_prenom_is_empty() {
            MnbModel model = validModel();
            model.setPrenom("");

            assertThat(catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class))
                    .isNotNull();
        }

        @Test
        void fails_when_prenom_contains_digits() {
            MnbModel model = validModel();
            model.setPrenom("Jean2");

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).anyMatch(m -> m.contains("prenom") && m.contains("valide"));
        }
    }

    // -------------------------------------------------------------------------
    // email
    // -------------------------------------------------------------------------

    @Nested
    class EmailTests {

        @Test
        void fails_when_email_is_null() {
            MnbModel model = validModel();
            model.setEmail(null);

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).anyMatch(m -> m.contains("email") && m.contains("obligatoire"));
        }

        @Test
        void fails_when_email_has_no_at_sign() {
            MnbModel model = validModel();
            model.setEmail("jean.dupont");

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).anyMatch(m -> m.contains("email") && m.contains("valide"));
        }

        @Test
        void fails_when_email_has_no_domain() {
            MnbModel model = validModel();
            model.setEmail("jean@");

            assertThat(catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class))
                    .isNotNull();
        }

        @Test
        void passes_for_valid_email_formats() {
            MnbModel model = validModel();
            model.setEmail("user.name+tag@sub.domain.org");
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }
    }

    // -------------------------------------------------------------------------
    // adresse (@NotNull + @Valid cascade)
    // -------------------------------------------------------------------------

    @Nested
    class AdresseTests {

        @Test
        void fails_when_adresse_is_null() {
            MnbModel model = validModel();
            model.setAdresse(null);

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).anyMatch(m -> m.contains("adresse") && m.contains("Null"));
        }

        @Test
        void cascades_adresse_violations_when_valid_annotated() {
            MnbModel model = validModel();
            model.setAdresse(AdresseExample.builder().build()); // all fields blank

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            // voie, codePostal, pays should all be reported
            assertThat(ex.getErreurs()).hasSizeGreaterThanOrEqualTo(3);
        }
    }

    // -------------------------------------------------------------------------
    // Multiple violations at once
    // -------------------------------------------------------------------------

    @Test
    void collects_all_violations_on_completely_empty_model() {
        MnbModel model = MnbModel.builder().build(); // everything null

        ValidationException ex = catchThrowableOfType(
                () -> ReflectionValidator.validate(model), ValidationException.class);

        // nom, prenom, email, adresse must all be reported
        assertThat(ex.getErreurs()).hasSizeGreaterThanOrEqualTo(4);
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private static Set<String> messagesOf(ValidationException ex) {
        return ex.getErreurs().stream()
                .map(ValidationError::message)
                .collect(Collectors.toSet());
    }
}