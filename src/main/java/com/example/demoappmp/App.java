package com.example.demoappmp;
import com.mercadopago.MercadoPagoConfig;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
	@Value("${mercado.pago.access.token}")
	private  String accessToken;

	@PostConstruct
	public void init(){
		MercadoPagoConfig.setAccessToken(accessToken);
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
