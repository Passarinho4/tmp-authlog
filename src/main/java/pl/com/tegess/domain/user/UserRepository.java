package pl.com.tegess.domain.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    List<User> findByUsername(String username);

    @Query(value = "{'username' : ?0, 'appId' : ?1}")
    User findByUsernameAndAppId(String username, String appId);
}
