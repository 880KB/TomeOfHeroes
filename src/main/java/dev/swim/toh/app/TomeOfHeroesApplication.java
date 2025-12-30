package dev.swim.toh.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(proxyBeanMethods = false, scanBasePackages = "dev.swim.toh")
@ConfigurationPropertiesScan
public class TomeOfHeroesApplication {
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = new SpringApplicationBuilder(TomeOfHeroesApplication.class).run(args);
        javafx.application.Application.launch(TomeOfHeroesApp.class, args);
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}
