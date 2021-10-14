package com.example.valuepaljava.Yahoo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="application.api")
public class ApiConfig {

    private String yahooKey;
    private String yahooHost;
    private String yahooSummaryUrl = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-summary?region=BR";
    private String yahooNewsFeedUrl = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/news/v2/list?region=US";

    public String getYahooNewsFeedUrl() {
        return yahooNewsFeedUrl;
    }

    public String getYahooSummaryUrl() {
        return yahooSummaryUrl;
    }

    public void setYahooSummaryUrl(String yahooSummaryUrl) {
        this.yahooSummaryUrl = yahooSummaryUrl;
    }

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
