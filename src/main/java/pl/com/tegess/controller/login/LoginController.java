package pl.com.tegess.controller.login;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.com.tegess.domain.application.Application;
import pl.com.tegess.domain.application.ApplicationRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

@RestController
@Component
public class LoginController {

    @Autowired
    ApplicationRepository repository;

    @RequestMapping(value = "api/login", method = RequestMethod.GET)
    public RedirectView login(@RequestParam String appId, @RequestParam String type) {

        Application application = repository.findOne(new ObjectId(appId));

        RedirectView view = new RedirectView();
        view.setUrl("https://facebook.com/dialog/oauth?" +
                "  client_id=" + application.getFacebookAppId() +
                "  &redirect_uri=http://51.255.48.55:8085/api/logged?appId="+appId+"&type=FB");

        return view;
    }

    @RequestMapping(value = "api/logged", method = RequestMethod.GET)
    public RedirectView logged(
            @RequestParam String appId,
            @RequestParam String type,
            HttpServletRequest facebookResponse) throws IOException {

        String string = IOUtils.toString(facebookResponse.getInputStream(), Charset.defaultCharset());

        System.out.println("Handling response from FB for applicationId = " + appId);
        System.out.println(string);

        String redirectURI = repository.findOne(new ObjectId(appId)).getRedirectURI();
        System.out.println("Redirecting to " + redirectURI);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectURI);
        return redirectView;
    }
}
