package com.mnb.projet.domain.validation.rule;

import com.mnb.projet.domain.validation.exception.ValidationException;
import com.mnb.projet.domain.validation.exception.ValidationError;
import com.mnb.projet.domain.validation.rule.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Reflection-based validator that processes our custom constraint annotations
 * (@NotNull, @NotEmpty, @NotBlank, @Pattern, @Valid, @AssertTrue) without
 * any dependency on jakarta.validation.
 *
 * <p>Usage: {@code ReflectionValidator.validate(myModel);}
 *
 * <p>Behavior mirrors jakarta.validation:
 * <ul>
 *   <li>@NotNull  — fails if value is null; null does NOT propagate to @Pattern</li>
 *   <li>@NotEmpty — fails if null or empty (String/Collection/Map/array)</li>
 *   <li>@NotBlank — fails if null, empty, or whitespace-only</li>
 *   <li>@Pattern  — skipped when value is null (null is considered valid)</li>
 *   <li>@Valid    — cascades into the nested object recursively</li>
 *   <li>@AssertTrue — method must return true; skipped if method throws</li>
 * </ul>
 * All violations are collected before throwing, not short-circuited.
 */
public class ReflectionValidator {

    private ReflectionValidator() {}

    public static void validate(Object target) {
        Set<ValidationError> errors = new LinkedHashSet<>();
        collectErrors(target, errors);
        if (!errors.isEmpty()) {
            throw new ValidationException(ValidationException.DONNEES_INCORRECTES_EXCEPTION, errors);
        }
    }

    // -------------------------------------------------------------------------
    // Core recursive engine
    // -------------------------------------------------------------------------

    private static void collectErrors(Object target, Set<ValidationError> errors) {
        if (target == null) return;

        validateFields(target, errors);
        validateAssertTrueMethods(target, errors);
    }

    // -------------------------------------------------------------------------
    // Field-level processing
    // -------------------------------------------------------------------------

    private static void validateFields(Object target, Set<ValidationError> errors) {
        for (Field field : getAllFields(target.getClass())) {
            field.setAccessible(true);
            Object value = getFieldValue(field, target);
            String champ = buildChamp(target.getClass(), field.getName());

            boolean failedRequiredCheck = applyNotNull(field, value, champ, errors)
                    || applyNotEmpty(field, value, champ, errors)
                    || applyNotBlank(field, value, champ, errors);

            if (!failedRequiredCheck) {
                applyPattern(field, value, champ, errors);
                applyValid(field, value, errors);
            }
        }
    }

    private static boolean applyNotNull(Field field, Object value, String champ, Set<ValidationError> errors) {
        if (!field.isAnnotationPresent(NotNull.class)) return false;
        if (value != null) return false;
        errors.add(new ValidationError(field.getAnnotation(NotNull.class).message(), champ));
        return true;
    }

    private static boolean applyNotEmpty(Field field, Object value, String champ, Set<ValidationError> errors) {
        if (!field.isAnnotationPresent(NotEmpty.class)) return false;
        if (!isEmpty(value)) return false;
        errors.add(new ValidationError(field.getAnnotation(NotEmpty.class).message(), champ));
        return true;
    }

    private static boolean applyNotBlank(Field field, Object value, String champ, Set<ValidationError> errors) {
        if (!field.isAnnotationPresent(NotBlank.class)) return false;
        if (!isBlank(value)) return false;
        errors.add(new ValidationError(field.getAnnotation(NotBlank.class).message(), champ));
        return true;
    }

    private static void applyPattern(Field field, Object value, String champ, Set<ValidationError> errors) {
        if (!field.isAnnotationPresent(Pattern.class)) return;
        if (value == null) return; // null is valid for @Pattern, mirrors jakarta behavior
        Pattern pattern = field.getAnnotation(Pattern.class);
        if (!value.toString().matches(pattern.regexp())) {
            errors.add(new ValidationError(pattern.message(), champ));
        }
    }

    private static void applyValid(Field field, Object value, Set<ValidationError> errors) {
        if (!field.isAnnotationPresent(Valid.class)) return;
        if (value == null) return;
        collectErrors(value, errors);
    }

    // -------------------------------------------------------------------------
    // Method-level processing (@AssertTrue)
    // -------------------------------------------------------------------------

    private static void validateAssertTrueMethods(Object target, Set<ValidationError> errors) {
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(AssertTrue.class)) continue;
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

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

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