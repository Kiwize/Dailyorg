package fr.nexa.dailyorg_java.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataSourceLogger {
	
	@Value("${DB_URL}")
	private String DB_URL;
    
    @Value("${DB_NAME}")
	private String DB_NAME;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    
    

    @PostConstruct
    public void logUrl() {
        System.out.println("========> Connecting to database with JDBC URL: " + jdbcUrl);
    }
}
