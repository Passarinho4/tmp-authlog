package pl.com.tegess.domain.events;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.tegess.domain.application.Application;
import pl.com.tegess.domain.user.User;

import java.util.Date;

@Service
public class LoginService {

    @Autowired LoginEventRepository repository;

    public void logLogin(Application application, User user) {
        LoginEvent event = new LoginEvent(application.getAppId(), user.getId());
        repository.save(event);
    }

    public long countLoginForAppInPeriod(ObjectId appId, Date from, Date to) {
        return repository.countLoginForAppInPeriod(appId, from, to);
    }

}
