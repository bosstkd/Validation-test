package com.mnb.projet.spring;

import com.mnb.projet.domain.common.DomainService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.mnb.projet.spring.ApplicationConfiguration.PERSISTANCE_PACKAGE;
import static com.mnb.projet.spring.ApplicationConfiguration.APPLICATION_PACKAGE;

@Slf4j
@Configuration
@OpenAPIDefinition(info = @Info(title = "Mnb API", version = "0.0.1", description = "Api description"))
@EnableAutoConfiguration
@EnableJpaRepositories(PERSISTANCE_PACKAGE)
@EntityScan(PERSISTANCE_PACKAGE)
@ComponentScan(
        value = { APPLICATION_PACKAGE },
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = DomainService.class
        )
)
public class ApplicationConfiguration {

    public static final String APPLICATION_PACKAGE = "com.mnb.projet";
    public static final String PERSISTANCE_PACKAGE = "com.mnb.projet.infrastructure.persistance";
}
