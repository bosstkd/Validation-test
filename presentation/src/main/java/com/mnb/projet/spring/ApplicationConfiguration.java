package com.mnb.projet.spring;

import com.mnb.projet.domain.common.DomainService;
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
