package pl.com.tegess.controller.user.writecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tegess.controller.user.request.UserData;
import pl.com.tegess.domain.user.User;
import pl.com.tegess.domain.user.UserRepository;

@RestController
@Component
public class UserWriteController {

    @Autowired
    UserRepository repository;


    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public void addUser(@RequestBody UserData userData) {

        repository.save(new User(userData.getUsername(), userData.getPassword()));

        System.out.println(userData.getUsername());
        System.out.println(userData.getPassword());

    }


}
