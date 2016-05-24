package pl.com.tegess.controller.user.readcontroller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
                .map(user -> new UserData(user.getId().toString(), user.getUsername(), user.getMail(), user.getPictureURL()))
                .collect(Collectors.toList());

    }

    @RequestMapping(value = "/api/applications/{appId}/users", method = RequestMethod.GET)
    public List<UserData> getUserDataForApplication(@RequestParam String appId) {
        return repository.findAll()
                .stream()
                .filter(user -> user.getAppId().equals(new ObjectId(appId)))
                .map(user -> new UserData(
                        user.getId().toString(),
                        user.getUsername(),
                        user.getMail(),
                        user.getPictureURL()))
                .collect(Collectors.toList());
    }

 }
