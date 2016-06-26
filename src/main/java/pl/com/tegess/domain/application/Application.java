package pl.com.tegess.domain.application;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Application {

    @Id
    private final ObjectId appId;
    private final String facebookAppId;
    private final String secret;
    private final String redirectURI;

    public Application(ObjectId appId, String facebookAppId, String secret, String redirectURI) {
        this.appId = appId;
        this.facebookAppId = facebookAppId;
        this.secret = secret;
        this.redirectURI = redirectURI;
    }

    public ObjectId getAppId() {
        return appId;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public String getSecret() {
        return secret;
    }
}
