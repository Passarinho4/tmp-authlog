package pl.com.tegess.controller.user.writecontroller;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.com.tegess.controller.user.request.User;

@RestController
@Component
public class UserWriteController {

    @Autowired
    MongoClient mongoClient;


    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public void addUser(@RequestBody User user) {

        System.out.println(user.getUsername());
        System.out.println(user.getPassword());

    }


}
