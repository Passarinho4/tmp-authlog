package pl.com.tegess.config;


import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.tegess.controller.login.TokenManager;
import pl.com.tegess.controller.login.facebook.FacebookLoginHelper;
import pl.com.tegess.domain.user.service.UserService;

import java.util.Collections;

@Configuration
public class Config {

    @Bean
    public MongoClient mongoClient() {
        MongoCredential credential = MongoCredential.createCredential(
                "Passarinho", "admin", "Passarinho123".toCharArray());
        ServerAddress serverAddress = new ServerAddress("51.255.48.55", 27017);
        MongoClient mongoClient = new MongoClient(serverAddress, Collections.singletonList(credential));
        return mongoClient;
    }

    @Bean
    public Morphia morphia() {
        Morphia morphia = new Morphia();
        morphia.getMapper().getOptions().setStoreEmpties(true);
        morphia.mapPackage("pl.com.tegess.domain");
        return morphia;
    }

    @Bean
    public Datastore datastore() {
        Datastore authlog = morphia().createDatastore(mongoClient(), "Authlog");
        authlog.ensureIndexes();
        return authlog;
    }

    @Bean
    public TokenManager tokenManager() {
        return new TokenManager();
    }

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public FacebookLoginHelper facebookLoginHelper() {
        return new FacebookLoginHelper();
    }
}
