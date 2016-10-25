package ru.mail.park;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Zac on 01/10/16.
 */

@SpringBootApplication
public class Application {

    @Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource mainDataSource() throws URISyntaxException {
		URI jdbUri = new URI(System.getenv("JAWSDB_URL"));
		String port = String.valueOf(jdbUri.getPort());
		return DataSourceBuilder.create()
				.username(jdbUri.getUserInfo().split(":")[0])
				.password(jdbUri.getUserInfo().split(":")[1])
				.url("jdbc:mysql://" + jdbUri.getHost() + ":" + port + jdbUri.getPath())
				.build();
	}

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
