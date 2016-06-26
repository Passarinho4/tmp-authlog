package pl.com.tegess.domain.events;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class LoginEventRepository extends BasicDAO<LoginEvent, ObjectId> {

    protected LoginEventRepository(Datastore ds) {
        super(ds);
    }
}
