package com.mnb.projet.domain.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a domain service in the DDD sense.
 * <p>
 * Pure Java annotation — no Spring dependency.
 * Spring registers beans annotated with {@code @DomainService} via
 * a {@code @ComponentScan} include filter in {@code ApplicationConfiguration}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DomainService {
}
