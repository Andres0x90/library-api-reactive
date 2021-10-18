package co.com.sofka.libraryapireactive.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
@EnableWebFlux
public class ApiRestConfig implements WebFluxConfigurer{
    @Bean
    public WebFluxConfigurer corsConfigure() {
        return new WebFluxConfigurerComposite() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }

    @Bean
    public ModelMapper getModelMapper()
    {
        return new ModelMapper();
    }

    @Bean
    public DateTimeFormatter getDateTimeFormatter()
    {
        return DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
    }
}
