package com.example.valuepaljava.Yahoo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix="application.api")
public class ApiConfig {

    private String yahooKey;
    private String yahooHost;

    public String getYahooKey() {
        return yahooKey;
    }

    public void setYahooKey(String yahooKey) {
        this.yahooKey = yahooKey;
    }

    public String getYahooHost() {
        return yahooHost;
    }

    public void setYahooHost(String yahooHost) {
        this.yahooHost = yahooHost;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }
}
