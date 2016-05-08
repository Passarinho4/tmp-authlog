package pl.com.tegess.controller.application.writecontroller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tegess.controller.application.request.ApplicationData;
import pl.com.tegess.domain.application.Application;
import pl.com.tegess.domain.application.ApplicationRepository;

@RestController
@Component
public class ApplicationWriteController {


    @Autowired
    ApplicationRepository repository;

    @RequestMapping(value = "api/applications", method = RequestMethod.POST)
    public void createApplication(@RequestBody ApplicationData applicationData) {

        Application application = new Application(new ObjectId(),
                applicationData.getFacebookAppId(), applicationData.getRedirectURI());

        repository.insert(application);
    }
}
