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
import java.util.Arrays;
import java.util.Map;

@RestController
@Component
public class LoginController {

    @Autowired
    ApplicationRepository repository;

    @RequestMapping(value = "api/login", method = RequestMethod.GET)
    public RedirectView login(@RequestParam String appId, @RequestParam String type) {

        Application application = repository.findOne(new ObjectId(appId));

        RedirectView view = new RedirectView();
        view.setUrl(FacebookUtils.prepareCodeRequest(application));

        return view;
    }

    @RequestMapping(value = "api/logged", method = RequestMethod.GET)
    public RedirectView logged(
            @RequestParam String appId,
            @RequestParam String code) throws IOException {

        Application application = repository.findOne(new ObjectId(appId));

        String redirectURI = FacebookUtils.prepareTokenRequest(application, code);

        System.out.println(redirectURI);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectURI);
        return redirectView;
    }

    @RequestMapping(value = "api/token", method = RequestMethod.GET)
    public RedirectView token(@RequestParam String appId, @RequestBody FacebookTokenResponse tokenResponse){

        System.out.println(tokenResponse.toString());

        Application application = repository.findOne(new ObjectId(appId));
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(application.getRedirectURI());
        return redirectView;
    }
}
