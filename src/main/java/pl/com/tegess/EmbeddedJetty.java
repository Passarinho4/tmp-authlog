package pl.com.tegess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan("pl.com.tegess")
@Configuration
@EnableAutoConfiguration
public class EmbeddedJetty {

    public static void main(String[] args) {
        SpringApplication.run(EmbeddedJetty.class, args);
    }

}