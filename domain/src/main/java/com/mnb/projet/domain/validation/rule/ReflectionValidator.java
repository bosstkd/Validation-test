package com.mnb.projet.domain.validation.rule;

import com.mnb.projet.domain.validation.exception.ValidationException;
import com.mnb.projet.domain.validation.exception.ValidationError;
import com.mnb.projet.domain.validation.rule.annotation.*;
import com.mnb.projet.domain.validation.rule.group.Default;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Validateur par réflexion qui traite nos annotations de contraintes personnalisées
 * (@NotNull, @NotEmpty, @NotBlank, @Pattern, @Valid, @AssertTrue) : on applique la JSR-380 sans
 * aucune dépendance à jakarta.validation.
 *
 * <p>Utilisation : {@code ReflectionValidator.validate(myObject);}
 * ou {@code ReflectionValidator.validate(myObject, OnCreate.class);} pour cibler des groupes de validation
 * myObject doit porter nos annotations pour être analysé, sinon il est ignoré
 *
 * <p>Le comportement est similaire à celui de jakarta.validation :
 * <ul>
 *   <li>@NotNull  — échoue si la valeur est null ; le null ne se propage PAS à @Pattern</li>
 *   <li>@NotEmpty — échoue si null ou vide (String/Collection/Map/tableau)</li>
 *   <li>@NotBlank — échoue si null, vide ou composé uniquement d'espaces</li>
 *   <li>@Pattern  — ignoré quand la valeur est null (null est considéré comme valide)</li>
 *   <li>@Valid    — valide en cascade l'objet imbriqué, récursivement</li>
 *   <li>@AssertTrue — la méthode doit retourner true ; une exception levée par la méthode est propagée</li>
 *   <li>groups — une contrainte n'est vérifiée que si l'un de ses groupes est demandé (ou est parent d'un
 *       groupe demandé) ; sans groupe, c'est {@link Default}, des deux côtés. @Valid propage les groupes demandés</li>
 * </ul>
 */
public class ReflectionValidator {

    private static final Class<?>[] DEFAULT_GROUPS = {Default.class};

    private ReflectionValidator() {}

    public static void validate(Object target, Class<?>... groups) {
        Set<ValidationError> errors = new LinkedHashSet<>();
        collectErrors(target, groups == null || groups.length == 0 ? DEFAULT_GROUPS : groups, errors);
        if (!errors.isEmpty()) {
            throw new ValidationException(ValidationException.DONNEES_INCORRECTES_EXCEPTION, errors);
        }
    }

    // Error collector engine
    private static void collectErrors(Object target, Class<?>[] groups, Set<ValidationError> errors) {
        if (target == null) return;

        validateFields(target, groups, errors);
        validateAssertTrueMethods(target, groups, errors);
    }

    // Field-level checkers
    private static void validateFields(Object target, Class<?>[] groups, Set<ValidationError> errors) {
        for (Field field : getAllFields(target.getClass())) {
            field.setAccessible(true);
            Object value = getFieldValue(field, target);
            String champ = buildChamp(target.getClass(), field.getName());

            boolean failedRequiredCheck = applyNotNull(field, value, champ, groups, errors) ||
                applyNotEmpty(field, value, champ, groups, errors) ||
                applyNotBlank(field, value, champ, groups, errors);

            if (!failedRequiredCheck) {
                applyPattern(field, value, champ, groups, errors);
                applyValid(field, value, groups, errors);
            }
        }
    }

    private static boolean applyNotNull(Field field, Object value, String champ, Class<?>[] groups, Set<ValidationError> errors) {
        if (!field.isAnnotationPresent(NotNull.class)) return false;
        if (!inGroups(field.getAnnotation(NotNull.class).groups(), groups)) return false;
        if (value != null) return false;
        errors.add(new ValidationError(field.getAnnotation(NotNull.class).message(), champ));
        return true;
    }

    private static boolean applyNotEmpty(Field field, Object value, String champ, Class<?>[] groups, Set<ValidationError> errors) {
        if (!field.isAnnotationPresent(NotEmpty.class)) return false;
        if (!inGroups(field.getAnnotation(NotEmpty.class).groups(), groups)) return false;
        if (!isEmpty(value)) return false;
        errors.add(new ValidationError(field.getAnnotation(NotEmpty.class).message(), champ));
        return true;
    }

    private static boolean applyNotBlank(Field field, Object value, String champ, Class<?>[] groups, Set<ValidationError> errors) {
        if (!field.isAnnotationPresent(NotBlank.class)) return false;
        if (!inGroups(field.getAnnotation(NotBlank.class).groups(), groups)) return false;
        if (!isBlank(value)) return false;
        errors.add(new ValidationError(field.getAnnotation(NotBlank.class).message(), champ));
        return true;
    }

    private static void applyPattern(Field field, Object value, String champ, Class<?>[] groups, Set<ValidationError> errors) {
        if (!field.isAnnotationPresent(Pattern.class)) return;
        if (!inGroups(field.getAnnotation(Pattern.class).groups(), groups)) return;
        if (value == null) return; // null is valid for @Pattern, mirrors jakarta behavior
        Pattern pattern = field.getAnnotation(Pattern.class);
        if (!value.toString().matches(pattern.regexp())) {
            errors.add(new ValidationError(pattern.message(), champ));
        }
    }

    // Used for the recursive call (@Valid)
    private static void applyValid(Field field, Object value, Class<?>[] groups, Set<ValidationError> errors) {
        if (!field.isAnnotationPresent(Valid.class)) return;
        if (value == null) return;
        collectErrors(value, groups, errors);
    }

    // Method-level checker (@AssertTrue)
    private static void validateAssertTrueMethods(Object target, Class<?>[] groups, Set<ValidationError> errors) {
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(AssertTrue.class)) continue;
            if (!inGroups(method.getAnnotation(AssertTrue.class).groups(), groups)) continue;
            if (method.getParameterCount() != 0) continue;
            if (!isBooleanReturn(method)) continue;

            method.setAccessible(true);

          try {
            if (Boolean.FALSE.equals(method.invoke(target))) {
              String champ = buildChamp(target.getClass(), method.getName());
              errors.add(new ValidationError(method.getAnnotation(AssertTrue.class).message(), champ));
            }
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
          }


        }
    }

    // Helpers methods
    private static boolean inGroups(Class<?>[] constraintGroups, Class<?>[] requestedGroups) {
        Class<?>[] declared = constraintGroups.length == 0 ? DEFAULT_GROUPS : constraintGroups;
        for (Class<?> requested : requestedGroups) {
            for (Class<?> group : declared) {
                // isAssignableFrom apporte l'héritage de groupes : demander un groupe enfant vérifie aussi ses parents
                if (requested != null && group.isAssignableFrom(requested)) return true;
            }
        }
        return false;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    private static Object getFieldValue(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private static String buildChamp(Class<?> clazz, String member) {
        return clazz.getName() + "." + member;
    }

  private static boolean isEmpty(Object value) {
    return value == null
        || value instanceof CharSequence cs && cs.isEmpty()
        || value instanceof Collection<?> c && c.isEmpty()
        || value instanceof Map<?, ?> m && m.isEmpty()
        || value.getClass().isArray() && java.lang.reflect.Array.getLength(value) == 0;
  }

  private static boolean isBlank(Object value) {
    return value == null
        || (value instanceof CharSequence cs && cs.toString().isBlank())
        || isEmpty(value);
  }

    private static boolean isBooleanReturn(Method method) {
        return method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class;
    }
}