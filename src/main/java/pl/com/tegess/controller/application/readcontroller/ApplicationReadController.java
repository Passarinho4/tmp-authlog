package pl.com.tegess.controller.application.readcontroller;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.com.tegess.controller.application.request.ApplicationData;
import pl.com.tegess.controller.application.request.LoginNumber;
import pl.com.tegess.domain.application.ApplicationRepository;
import pl.com.tegess.domain.events.LoginService;

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
                new DateTime(from), new DateTime(to)));
    }
}
