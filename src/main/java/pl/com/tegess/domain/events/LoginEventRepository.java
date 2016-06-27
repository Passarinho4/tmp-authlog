package pl.com.tegess.domain.events;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class LoginEventRepository extends BasicDAO<LoginEvent, ObjectId> {

    @Autowired
    protected LoginEventRepository(Datastore ds) {
        super(ds);
    }

    long countLoginForAppInPeriod(ObjectId appId, Date from, Date to){
        return createQuery().field("appId").equal(appId)
                .field("time").greaterThanOrEq(from)
                .field("time").lessThan(to)
                .countAll();
    }

    List<LoginEvent> getAllForAppInPeriod(ObjectId appId, Date from, Date to) {
        return createQuery().field("appId").equal(appId)
                .field("time").greaterThanOrEq(from)
                .field("time").lessThan(to)
                .asList();
    }

    long countLoginForAppInTime(ObjectId appId, Date time) {
        return createQuery().field("appId").equal(appId)
                .field("time").equal(time).countAll();
    }

}
