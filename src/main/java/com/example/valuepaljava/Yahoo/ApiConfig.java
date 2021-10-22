package com.example.valuepaljava.Yahoo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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

}
