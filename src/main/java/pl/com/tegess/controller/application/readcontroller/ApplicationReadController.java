package pl.com.tegess.controller.application.readcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tegess.controller.application.request.ApplicationData;
import pl.com.tegess.domain.application.ApplicationRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Component
public class ApplicationReadController {

    @Autowired
    ApplicationRepository repository;

    @RequestMapping(value = "/api/applications", method = RequestMethod.GET)
    public List<ApplicationData> getApplications() {
        return repository.findAll()
                .stream()
                .map(application -> new ApplicationData(
                        application.getAppId().toString(),
                        application.getFacebookAppId(),
                        application.getRedirectURI()))
                .collect(Collectors.toList());

    }
}
