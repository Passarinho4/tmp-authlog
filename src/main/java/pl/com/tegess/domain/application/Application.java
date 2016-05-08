package pl.com.tegess.domain.application;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Application {

    @Id
    private final ObjectId appId;
    private final String facebookAppId;

    public Application(ObjectId appId, String facebookAppId) {
        this.appId = appId;
        this.facebookAppId = facebookAppId;
    }

    public ObjectId getAppId() {
        return appId;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

}
