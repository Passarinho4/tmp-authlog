package pl.com.tegess.controller.application.readcontroller;

import org.apache.commons.lang3.tuple.Pair;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.com.tegess.controller.application.request.ApplicationData;
import pl.com.tegess.controller.application.request.LoginNumber;
import pl.com.tegess.controller.application.request.MinuteStats;
import pl.com.tegess.domain.application.ApplicationRepository;
import pl.com.tegess.domain.events.LoginService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Component
public class ApplicationReadController {

    @Autowired
    ApplicationRepository repository;
    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/api/applications", method = RequestMethod.GET)
    public List<ApplicationData> getApplications() {
        return repository.find()
                .asList()
                .stream()
                .map(application -> new ApplicationData(
                        application.getAppId().toString(),
                        application.getFacebookAppId(),
                        application.getSecret(),
                        application.getRedirectURI()))
                .collect(Collectors.toList());

    }

    @RequestMapping(value = "/api/applications/{appId}/loginNumber", method = RequestMethod.GET)
    public LoginNumber getLoginNumber(@PathVariable String appId, @RequestParam Long from, @RequestParam Long to) {
        return new LoginNumber(loginService.countLoginForAppInPeriod(new ObjectId(appId),
                new Date(from), new Date(to)));
    }

    @RequestMapping(value = "/api/applications/{appId}/hourLoginStats", method = RequestMethod.GET)
    public List<MinuteStats> getHourLoginStats(@PathVariable String appId) {
        return loginService.getHourLoginStats(new ObjectId(appId)).stream()
                .map(pair -> new MinuteStats(pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
    }
}
