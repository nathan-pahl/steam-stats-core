package org.nathan.pahl.steamstatscore;

import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import de.SweetCode.SteamAPI.SteamAPI;

@SpringBootApplication
public class SteamStatsCoreApplication {

	private static String steamApiKey;

	public static void main(String[] args) {
		steamApiKey = args[0];
		SpringApplication.run(SteamStatsCoreApplication.class, args);
	}

	@Bean
	public SteamWebApiClient getSteamWebApiClient() {
		return new SteamWebApiClient.SteamWebApiClientBuilder(steamApiKey).build();
	}

	@Bean
	public SteamAPI getSteamApi() {
		return new SteamAPI(steamApiKey);
	}

}
