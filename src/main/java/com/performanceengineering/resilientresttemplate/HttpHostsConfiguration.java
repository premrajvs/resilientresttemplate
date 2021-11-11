package com.performanceengineering.resilientresttemplate;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "httpconnpool")
public class HttpHostsConfiguration {

    private Integer maxTotal;
    private Integer defaultMaxPerRoute;
    private List<HttpHostConfiguration> maxPerRoutes;

    public Integer getMaxTotal() {
        return this.maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getDefaultMaxPerRoute() {
        return this.defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public List<HttpHostConfiguration> getMaxPerRoutes() {
        return this.maxPerRoutes;
    }

    public void setMaxPerRoutes(List<HttpHostConfiguration> maxPerRoutes) {
        this.maxPerRoutes = maxPerRoutes;
    }

    public static class HttpHostConfiguration {

        private String scheme;
        private String host;
        private Integer port;
        private Integer maxPerRoute;

        public String getScheme() {
            return this.scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getHost() {
            return this.host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return this.port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public Integer getMaxPerRoute() {
            return maxPerRoute;
        }

        public void setMaxPerRoute(Integer maxPerRoute) {
            this.maxPerRoute = maxPerRoute;
        }
    }
}
