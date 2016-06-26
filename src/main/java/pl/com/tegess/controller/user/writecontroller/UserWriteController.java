package pl.com.tegess.controller.user.writecontroller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.com.tegess.controller.user.request.NewUserRequest;
import pl.com.tegess.domain.user.User;
import pl.com.tegess.domain.user.UserRepository;

import java.util.List;

@RestController
@Component
public class UserWriteController {

    @Autowired
    UserRepository repository;


    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String id) {
        repository.deleteById(new ObjectId(id));
    }

    @RequestMapping(value = "/api/users/{id}/privileges", method = RequestMethod.POST)
    public void addPrivilege(@PathVariable String id, @RequestBody List<String> privileges) {
        User user = repository.findOneById(new ObjectId(id));
        privileges.forEach(user::addPrivilege);
        repository.save(user);
    }

    @RequestMapping(value = "/api/users/{id}/privileges/{privilege}", method = RequestMethod.DELETE)
    public void deletePrivilege(@PathVariable String id, @PathVariable String privilege) {
        User user = repository.findOneById(new ObjectId(id));
        user.deletePrivilege(privilege);
        repository.save(user);
    }

    @RequestMapping(value = "/api/applications/{appId}/users", method = RequestMethod.POST)
    public void addUser(@RequestBody NewUserRequest userRequest, @PathVariable String appId) {
        User user = new User(new ObjectId(), userRequest.getUsername(), userRequest.getPassword(), new ObjectId(appId));
        repository.save(user);
    }

    @RequestMapping(value = "/api/applications/{appId}/users/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String appId, @PathVariable String userId) {
        repository.deleteById(new ObjectId(userId));
    }
}
