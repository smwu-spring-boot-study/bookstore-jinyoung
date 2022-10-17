package SpringAPIStudy.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing //자동 createAt, updatedAt 활성화
@EnableWebSecurity
public class BookstoreApplication {

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

}
