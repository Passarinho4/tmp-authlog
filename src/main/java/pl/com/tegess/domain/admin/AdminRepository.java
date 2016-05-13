package pl.com.tegess.domain.admin;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AdminRepository extends MongoRepository<Admin, String> {

}

