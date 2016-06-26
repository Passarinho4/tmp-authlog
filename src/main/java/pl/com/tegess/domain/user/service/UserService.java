package pl.com.tegess.domain.user.service;

import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.tegess.controller.login.request.facebook.FacebookUserData;
import pl.com.tegess.domain.user.User;
import pl.com.tegess.domain.user.UserRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(String appId, FacebookUserData userData) {

        List<User> users = userRepository.find(
                userRepository.createQuery()
                        .field("username").equal(userData.getName())
        ).asList();

        if(users.size() == 0) {
            User user = new User(new ObjectId(),
                    userData.getName(),
                    userData.getPicture().getData().getUrl(),
                    userData.getEmail(),
                    userData.getGender(),
                    new Locale(userData.getLocale()),
                    new ObjectId(appId));

            userRepository.save(user);
            return user;
        }else if(users.size() == 1) {
            return users.get(0);
        }else{
            throw new IllegalStateException("Too many users with the same username!");
        }

    }

    public Optional<User> findUserInApplication(String appId, String username) {
        Query<User> query = userRepository.createQuery()
                .field("username").equal(username)
                .field("appId").equal(new ObjectId(appId));


        User user = userRepository.findOne(query);
        return Optional.ofNullable(user);
    }

}
