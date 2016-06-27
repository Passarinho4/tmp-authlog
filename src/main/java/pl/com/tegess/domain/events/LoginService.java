package pl.com.tegess.domain.events;

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
import java.util.stream.IntStream;

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

    public List<Pair<Long, Long>> getHourLoginStats(ObjectId appId) {
        Date now = DateUtils.truncate(new Date(), Calendar.MINUTE);

        return IntStream.range(0, 60)
                .mapToObj(i -> DateUtils.addMinutes(now, -i))
                .map(time -> new ImmutablePair<>(
                        time.getTime(),
                        repository.countLoginForAppInPeriod(appId, DateUtils.addMinutes(time,-1), time)))
                .collect(Collectors.toList());
    }
}
