package pl.com.tegess.domain.events;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.tegess.domain.application.Application;
import pl.com.tegess.domain.user.User;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Pair<Date, Long>> getHourLoginStats(ObjectId appId) {
        Date now = DateUtils.truncate(new Date(), Calendar.MINUTE);

        List<LoginEvent> loginEventList = repository.getAllForAppInPeriod(appId, DateUtils.addMinutes(now, -60), now);

        return loginEventList.stream()
                .map(loginEvent -> loginEvent.setTime(DateUtils.truncate(loginEvent.getTime(), Calendar.MINUTE)))
                .collect(Collectors.groupingBy(LoginEvent::getTime))
                .entrySet().stream()
                .map(entry -> new ImmutablePair<>(entry.getKey(), (long) entry.getValue().size()))
                .collect(Collectors.toList());
    }
}
