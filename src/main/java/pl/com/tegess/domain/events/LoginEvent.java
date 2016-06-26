package pl.com.tegess.domain.events;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

@Entity
public class LoginEvent {

    @Id
    private ObjectId id;
    private ObjectId appId;
    private ObjectId userId;
    private Date time;

    public LoginEvent(){
    }

    public LoginEvent(ObjectId appId, ObjectId userId) {
        this.appId = appId;
        this.userId = userId;
        id = new ObjectId();
        time = new Date();
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public Date getTime() {
        return time;
    }

    public ObjectId getAppId() {
        return appId;
    }
}
