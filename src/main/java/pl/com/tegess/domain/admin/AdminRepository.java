package pl.com.tegess.domain.admin;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, ObjectId> {
}
