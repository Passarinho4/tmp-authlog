package pl.com.tegess.domain.application;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationRepository extends BasicDAO<Application, ObjectId> {

    @Autowired
    protected ApplicationRepository(Datastore ds) {
        super(ds);
    }

    public Application findOneById(ObjectId objectId) {
        Query<Application> q = createQuery().field("appId").equal(objectId);
        return findOne(q);
    }
}
