package pl.com.tegess.controller.user.readcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tegess.controller.user.request.UserData;
import pl.com.tegess.domain.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Component
public class UserReadController {

    @Autowired
    UserRepository repository;

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public List<UserData> getUsersData() {

        return repository.findAll()
                .stream()
                .map(user -> new UserData(user.getUsername(), user.getPassword()))
                .collect(Collectors.toList());

    }

 }
