package cisneros.jesus.GamesStop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadDir = Paths.get("uploads").toAbsolutePath().toUri().toString();
        // Asocia la URL "/uploads/**" a la carpeta física local "uploads/"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/", "file:./uploads/");
    }
}
