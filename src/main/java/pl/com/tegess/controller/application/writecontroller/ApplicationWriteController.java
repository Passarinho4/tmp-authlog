package pl.com.tegess.controller.application.writecontroller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
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
                applicationData.getFacebookAppId(), applicationData.getSecret(),
                applicationData.getFacebookRedirectURI());

        repository.insert(application);
    }

    @RequestMapping(value = "api/applications/:id", method = RequestMethod.DELETE)
    public void deleteApplication(@RequestParam String id) {
        if(ObjectId.isValid(id)){
            repository.delete(new ObjectId(id));
        }else {
            throw new IllegalArgumentException("Wrong id!");
        }
    }
}
