package ro.sebastianrapa.atmapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.sebastianrapa.atmapp.security.CardAuthentication;

@Configuration
public class CardAuthenticationConfig {

    @Bean
    public CardAuthentication getCardAuthentication() {
        return new CardAuthentication();
    }
}
