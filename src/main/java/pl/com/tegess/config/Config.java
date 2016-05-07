package pl.com.tegess.config;


import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Collections;

@Configuration
public class Config {


    @Bean
    public MongoClient mongoClient() {
        MongoCredential credential = MongoCredential.createCredential(
                "Passarinho", "admin", "Passarinho123".toCharArray());
        ServerAddress serverAddress = new ServerAddress("51.255.48.55", 27019);
        MongoClient mongoClient = new MongoClient(serverAddress, Collections.singletonList(credential));
        return mongoClient;
    }


}
