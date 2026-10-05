package com.mnb.projet.domain.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marque une classe comme service de domaine au sens DDD.
 * <p>
 * Annotation Java pure — aucune dépendance à Spring.
 * Spring enregistre les beans annotés avec {@code @DomainService} via
 * un filtre d'inclusion {@code @ComponentScan} dans {@code ApplicationConfiguration}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DomainService {
}
