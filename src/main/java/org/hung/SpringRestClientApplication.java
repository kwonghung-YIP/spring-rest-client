package org.hung;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringRestClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestClientApplication.class, args);
	}

	@Bean
	CommandLineRunner runRestClient(
			RestTemplateBuilder builder
	) {
		return (args) -> {
			log.info("Run RestTemplate client");
			
			RestTemplate restTemplate = builder
			.additionalCustomizers((template) -> {
				SSLContext sslContext = null;
				try {

					File trustStore = new File("C:/Users/kwong/Documents/certs/selfsigned/ca/trust.jks");
					sslContext = SSLContextBuilder.create()
						.loadTrustMaterial(trustStore,"abcd1234".toCharArray())
						//.loadTrustMaterial(new TrustAllStrategy())
						//.loadTrustMaterial(new TrustSelfSignedStrategy())	
						.build();
				} catch ( KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException  e) {
					log.error("{}",e);
				}
				
				//SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,NoopHostnameVerifier.INSTANCE);
				SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,null); //Use default hostname verifier
				
				HttpClient httpClient = HttpClientBuilder.create()
						.setSSLSocketFactory(socketFactory)
						//.useSystemProperties()
						.build();
				
				ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
				
				template.setRequestFactory(requestFactory);
			})
			.build();
			
			
			String result = restTemplate.getForObject("https://localhost:8080/echo", String.class);
			
			log.info("result: {}",result);
		};
	}
}
