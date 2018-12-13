package gemeente.nlakbonline;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class NlAkbOnlineApplication implements WebMvcConfigurer {

	@Value("${content.resources.directory}")
	private String contentResourcesDirectory;

	public static void main(String[] args) {
		SpringApplication.run(NlAkbOnlineApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		final SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("nl", "NL"));
		return slr;
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/external/**").addResourceLocations("file://" + contentResourcesDirectory);
	}
}
