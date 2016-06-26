package pl.com.tegess.domain.events;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class LoginEvent {

    @Id
    private ObjectId id;
    private ObjectId appId;
    private ObjectId userId;
    private DateTime time;

    public LoginEvent(){
    }

    public LoginEvent(ObjectId appId, ObjectId userId) {
        this.appId = appId;
        this.userId = userId;
        id = new ObjectId();
        time = new DateTime();
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public DateTime getTime() {
        return time;
    }

    public ObjectId getAppId() {
        return appId;
    }
}
