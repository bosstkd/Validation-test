package com.mnb.projet.spring;

import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ProjetApplication {

    private ProjetApplication() {

    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApplicationConfiguration.class)
                .bannerMode(Banner.Mode.OFF)
                .application()
                .run(args);
    }

}
