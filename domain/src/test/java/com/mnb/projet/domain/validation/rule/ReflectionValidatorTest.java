package com.mnb.projet.domain.validation.rule;

import com.mnb.projet.domain.validation.exception.ValidationException;
import com.mnb.projet.domain.validation.exception.ValidationError;
import com.mnb.projet.domain.validation.rule.annotation.*;
import com.mnb.projet.domain.validation.rule.group.Default;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class ReflectionValidatorTest {

    // -------------------------------------------------------------------------
    // Minimal model stubs used only in this test class
    // -------------------------------------------------------------------------

    static class ModelNotNull {
        @NotNull(message = "champ obligatoire")
        String value;
    }

    static class ModelNotEmpty {
        @NotEmpty(message = "champ vide")
        String value;
    }

    static class ModelNotBlank {
        @NotBlank(message = "champ blanc")
        String value;
    }

    static class ModelPattern {
        @Pattern(regexp = "\\d+", message = "doit être numérique")
        String value;
    }

    static class ModelNotEmptyAndPattern {
        @NotEmpty(message = "champ vide")
        @Pattern(regexp = "\\d+", message = "doit être numérique")
        String value;
    }

    static class Nested1 {
        @NotBlank(message = "nom obligatoire")
        String nom;
    }

    static class ModelValid {
        @NotNull(message = "nested null")
        @Valid
        Nested1 nested;
    }

    static class ModelAssertTrue {
        int age;

        @AssertTrue(message = "âge doit être positif")
        public boolean isAgePositif() {
            return age > 0;
        }
    }

    static class ModelMultipleViolations {
        @NotEmpty(message = "nom vide")
        String nom;

        @NotEmpty(message = "prenom vide")
        String prenom;
    }

    interface OnCreate {}

    interface OnUpdate {}

    interface OnCreateStrict extends OnCreate {}

    static class NestedWithGroups {
        @NotBlank(message = "nested default")
        String defaut;

        @NotBlank(message = "nested update", groups = OnUpdate.class)
        String update;
    }

    static class ModelGroups {
        @NotNull(message = "sans groupe")
        String sansGroupe = "ok";

        @NotNull(message = "id obligatoire en update", groups = OnUpdate.class)
        String id = "ok";

        @NotEmpty(message = "nom obligatoire en create", groups = OnCreate.class)
        String nom = "ok";

        @NotBlank(message = "code obligatoire en create et update", groups = {OnCreate.class, OnUpdate.class})
        String code = "ok";

        @Pattern(regexp = "\\d+", message = "ref numérique en create", groups = OnCreate.class)
        String ref = "1";

        @Valid
        NestedWithGroups nested;

        boolean coherent = true;

        @AssertTrue(message = "incohérent en update", groups = OnUpdate.class)
        public boolean isCoherent() {
            return coherent;
        }
    }

    // -------------------------------------------------------------------------
    // @NotNull
    // -------------------------------------------------------------------------

    @Nested
    class NotNullTests {

        @Test
        void passes_when_value_is_not_null() {
            ModelNotNull model = new ModelNotNull();
            model.value = "hello";
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }

        @Test
        void fails_when_value_is_null() {
            ModelNotNull model = new ModelNotNull();
            model.value = null;

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(ex).isNotNull();
            assertThat(messagesOf(ex)).containsExactly("champ obligatoire");
        }
    }

    // -------------------------------------------------------------------------
    // @NotEmpty
    // -------------------------------------------------------------------------

    @Nested
    class NotEmptyTests {

        @Test
        void passes_when_value_is_not_empty() {
            ModelNotEmpty model = new ModelNotEmpty();
            model.value = "ok";
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }

        @Test
        void fails_when_value_is_null() {
            ModelNotEmpty model = new ModelNotEmpty();
            model.value = null;

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactly("champ vide");
        }

        @Test
        void fails_when_value_is_empty_string() {
            ModelNotEmpty model = new ModelNotEmpty();
            model.value = "";

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactly("champ vide");
        }

        @Test
        void passes_when_value_is_blank_string() {
            // @NotEmpty does NOT reject whitespace-only — that's @NotBlank
            ModelNotEmpty model = new ModelNotEmpty();
            model.value = "   ";
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }
    }

    // -------------------------------------------------------------------------
    // @NotBlank
    // -------------------------------------------------------------------------

    @Nested
    class NotBlankTests {

        @Test
        void passes_when_value_has_content() {
            ModelNotBlank model = new ModelNotBlank();
            model.value = "ok";
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }

        @Test
        void fails_when_value_is_null() {
            ModelNotBlank model = new ModelNotBlank();
            assertThat(catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class))
                    .isNotNull();
        }

        @Test
        void fails_when_value_is_empty() {
            ModelNotBlank model = new ModelNotBlank();
            model.value = "";
            assertThat(catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class))
                    .isNotNull();
        }

        @Test
        void fails_when_value_is_whitespace_only() {
            ModelNotBlank model = new ModelNotBlank();
            model.value = "   ";
            assertThat(catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class))
                    .isNotNull();
        }
    }

    // -------------------------------------------------------------------------
    // @Pattern
    // -------------------------------------------------------------------------

    @Nested
    class PatternTests {

        @Test
        void passes_when_value_matches_regexp() {
            ModelPattern model = new ModelPattern();
            model.value = "12345";
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }

        @Test
        void fails_when_value_does_not_match_regexp() {
            ModelPattern model = new ModelPattern();
            model.value = "abc";

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactly("doit être numérique");
        }

        @Test
        void passes_when_value_is_null() {
            // null is considered valid for @Pattern — mirrors jakarta behavior
            ModelPattern model = new ModelPattern();
            model.value = null;
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }
    }

    // -------------------------------------------------------------------------
    // @NotEmpty + @Pattern interaction
    // -------------------------------------------------------------------------

    @Nested
    class NotEmptyAndPatternInteractionTests {

        @Test
        void skips_pattern_check_when_not_empty_already_failed() {
            // if @NotEmpty fails, @Pattern must NOT produce a second violation for the same field
            ModelNotEmptyAndPattern model = new ModelNotEmptyAndPattern();
            model.value = "";

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(ex.getErreurs()).hasSize(1);
            assertThat(messagesOf(ex)).containsExactly("champ vide");
        }

        @Test
        void checks_pattern_when_not_empty_passed() {
            ModelNotEmptyAndPattern model = new ModelNotEmptyAndPattern();
            model.value = "abc"; // not empty but doesn't match \d+

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactly("doit être numérique");
        }
    }

    // -------------------------------------------------------------------------
    // @Valid (cascading)
    // -------------------------------------------------------------------------

    @Nested
    class ValidCascadeTests {

        @Test
        void fails_with_nested_violation() {
            ModelValid model = new ModelValid();
            model.nested = new Nested1(); // nom is null → @NotBlank fails

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactly("nom obligatoire");
        }

        @Test
        void fails_when_nested_object_is_null_and_not_null_annotated() {
            ModelValid model = new ModelValid();
            model.nested = null;

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactly("nested null");
        }

        @Test
        void passes_when_nested_object_is_valid() {
            ModelValid model = new ModelValid();
            model.nested = new Nested1();
            model.nested.nom = "Jean";
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }
    }

    // -------------------------------------------------------------------------
    // @AssertTrue
    // -------------------------------------------------------------------------

    @Nested
    class AssertTrueTests {

        @Test
        void passes_when_method_returns_true() {
            ModelAssertTrue model = new ModelAssertTrue();
            model.age = 25;
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
        }

        @Test
        void fails_when_method_returns_false() {
            ModelAssertTrue model = new ModelAssertTrue();
            model.age = -1;

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactly("âge doit être positif");
        }
    }

    // -------------------------------------------------------------------------
    // All violations collected (no short-circuit)
    // -------------------------------------------------------------------------

    @Nested
    class AllViolationsCollectedTests {

        @Test
        void collects_all_violations_before_throwing() {
            ModelMultipleViolations model = new ModelMultipleViolations();
            // both nom and prenom are null

            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);

            assertThat(ex.getErreurs()).hasSize(2);
            assertThat(messagesOf(ex)).containsExactlyInAnyOrder("nom vide", "prenom vide");
        }
    }

    // -------------------------------------------------------------------------
    // Groupes
    // -------------------------------------------------------------------------

    @Nested
    class GroupsTests {

        private ModelGroups invalidEverywhere() {
            ModelGroups model = new ModelGroups();
            model.sansGroupe = null;
            model.id = null;
            model.nom = "";
            model.code = " ";
            model.ref = "abc";
            model.coherent = false;
            return model;
        }

        @Test
        void without_group_only_default_constraints_are_checked() {
            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(invalidEverywhere()), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactly("sans groupe");
        }

        @Test
        void explicit_default_group_behaves_like_no_group() {
            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(invalidEverywhere(), Default.class), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactly("sans groupe");
        }

        @Test
        void only_constraints_of_requested_group_are_checked() {
            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(invalidEverywhere(), OnCreate.class), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactlyInAnyOrder(
                    "nom obligatoire en create",
                    "code obligatoire en create et update",
                    "ref numérique en create");
        }

        @Test
        void assert_true_respects_groups() {
            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(invalidEverywhere(), OnUpdate.class), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactlyInAnyOrder(
                    "id obligatoire en update",
                    "code obligatoire en create et update",
                    "incohérent en update");
        }

        @Test
        void several_groups_can_be_requested_at_once() {
            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(invalidEverywhere(), Default.class, OnCreate.class, OnUpdate.class),
                    ValidationException.class);

            assertThat(ex.getErreurs()).hasSize(6);
        }

        @Test
        void child_group_also_checks_parent_group_constraints() {
            ValidationException ex = catchThrowableOfType(
                    () -> ReflectionValidator.validate(invalidEverywhere(), OnCreateStrict.class), ValidationException.class);

            assertThat(messagesOf(ex)).containsExactlyInAnyOrder(
                    "nom obligatoire en create",
                    "code obligatoire en create et update",
                    "ref numérique en create");
        }

        @Test
        void passes_when_violations_belong_to_another_group() {
            ModelGroups model = new ModelGroups();
            model.id = null; // vérifié uniquement pour OnUpdate

            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model));
            assertThatNoException().isThrownBy(() -> ReflectionValidator.validate(model, OnCreate.class));
        }

        @Test
        void valid_cascades_the_requested_groups() {
            ModelGroups model = new ModelGroups();
            model.nested = new NestedWithGroups(); // les deux champs sont null

            ValidationException exDefault = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model), ValidationException.class);
            ValidationException exUpdate = catchThrowableOfType(
                    () -> ReflectionValidator.validate(model, OnUpdate.class), ValidationException.class);

            assertThat(messagesOf(exDefault)).containsExactly("nested default");
            assertThat(messagesOf(exUpdate)).containsExactly("nested update");
        }
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private static Set<String> messagesOf(ValidationException ex) {
        return ex.getErreurs().stream()
                .map(ValidationError::message)
                .collect(java.util.stream.Collectors.toSet());
    }
}