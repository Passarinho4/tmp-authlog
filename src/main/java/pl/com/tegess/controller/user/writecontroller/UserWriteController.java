package pl.com.tegess.controller.user.writecontroller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.com.tegess.domain.user.UserRepository;

@RestController
@Component
public class UserWriteController {

    @Autowired
    UserRepository repository;


    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String id) {
        repository.delete(new ObjectId(id));
    }
}
