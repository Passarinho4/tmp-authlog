package pl.com.tegess.domain.events;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LoginEventRepository extends BasicDAO<LoginEvent, ObjectId> {

    @Autowired
    protected LoginEventRepository(Datastore ds) {
        super(ds);
    }

    public long countLoginForAppInPeriod(ObjectId appId, DateTime from, DateTime to){
        return createQuery().field("appId").equal(appId)
                .field("time").greaterThanOrEq(from)
                .field("time").lessThan(to)
                .countAll();
    }
}
