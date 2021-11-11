package com.performanceengineering.resilientresttemplate;

import javax.net.ssl.SSLContext;

import com.performanceengineering.resilientresttemplate.HttpHostsConfiguration.HttpHostConfiguration;
//import com.performanceengineering.resttemplateexample2.SampleHTTPSClient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
//import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

@SpringBootApplication(scanBasePackages = { "com.performanceengineering.resilientresttemplate" })
public class ResilientresttemplateApplication {

	@Autowired
	private HttpHostsConfiguration httpHostConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(ResilientresttemplateApplication.class, args);
	}

	// Pooling Connection manager set with max of 20 connections
	// Since defaultMaxPerRoute or maxPerRoute is not specified,
	// PoolingHttpClientConnectionManager uses a max of 4 connections per host route
	// if not specified
	@Bean
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
		// result.setMaxTotal(20);
		// Change 1 : Setting this value from httpHostConfiguration
		result.setMaxTotal(this.httpHostConfiguration.getMaxTotal());
		// Change 2 : Setting Default Max Per Route
		result.setDefaultMaxPerRoute(this.httpHostConfiguration.getDefaultMaxPerRoute());

		// Change 1 and 2 will increase the concurrent connections count from default 4
		// to 20

		if (CollectionUtils.isNotEmpty(this.httpHostConfiguration.getMaxPerRoutes())) {
			for (HttpHostConfiguration httpHostConfig : this.httpHostConfiguration.getMaxPerRoutes()) {
				HttpHost host = new HttpHost(httpHostConfig.getHost(), httpHostConfig.getPort(),
						httpHostConfig.getScheme());
				// Max per route for a specific host route
				result.setMaxPerRoute(new HttpRoute(host), httpHostConfig.getMaxPerRoute());
			}
		}

		return result;
	}

	ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
		@Override
		public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
			HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
			while (it.hasNext()) {
				HeaderElement he = it.nextElement();
				String param = he.getName();
				String value = he.getValue();
				if (value != null && param.equalsIgnoreCase("timeout")) {
					System.out.println("TIME OUT ***************************************************************");
					return Long.parseLong(value) * 1000;
				}
			}
			return 5 * 1000;
		}
	};

	@Bean
	public RequestConfig requestConfig() {
		RequestConfig result = RequestConfig.custom().setConnectionRequestTimeout(2000).setConnectTimeout(2000)
				.setSocketTimeout(2000).build();
		return result;
	}

	@Bean
	public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
			RequestConfig requestConfig) {
		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new SampleHTTPSClient.DefaultTrustManager() },
					new SecureRandom());
			SSLContext.setDefault(ctx);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		CloseableHttpClient result = HttpClientBuilder.create().setConnectionManager(poolingHttpClientConnectionManager)
				.setDefaultRequestConfig(requestConfig).setKeepAliveStrategy(myStrategy)
				// Change 3 to modify the keep alive strategy
				// .setSslcontext(ctx)
				.build();
		return result;
	}

	// Configuring a restTemplate bean with clientHTTPRequestFactory set to
	// HttpComponentsClientHttpRequestFactory
	// This is to replace the default implementation based on the JDK
	@Bean
	public RestTemplate restTemplate(HttpClient httpClient) {
		System.out.println("Rest Template Built Successfully !!!!!!!!!!!!!!!!!!!!!!!!!!");
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(false); // To avoid buffering the request body internally
		requestFactory.setHttpClient(httpClient);
		return new RestTemplate(requestFactory);
	}
}
