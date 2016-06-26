package pl.com.tegess.domain.admin;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepository extends BasicDAO<Admin, String> {

    @Autowired
    protected AdminRepository(Datastore ds) {
        super(ds);
    }
}

