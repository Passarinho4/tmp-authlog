package pl.com.tegess.controller.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import pl.com.tegess.controller.login.request.FacebookTokenResponse;
import pl.com.tegess.controller.login.request.FacebookValidateTokenResponse;
import pl.com.tegess.domain.application.Application;
import pl.com.tegess.domain.application.ApplicationRepository;

import java.io.IOException;
import java.net.URI;

@RestController
@Component
public class LoginController {

    @Autowired
    ApplicationRepository repository;
    private SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();

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
            @RequestParam String code) throws Exception {

        //Firstly we need obtain token from FB.
        Application application = repository.findOne(new ObjectId(appId));

        String tokenRequestURI = FacebookUtils.prepareTokenRequest(application, code);

        ClientHttpRequest tokenRequest = httpRequestFactory.createRequest(URI.create(tokenRequestURI), HttpMethod.GET);
        ClientHttpResponse tokenResponse = tokenRequest.execute();

        ObjectMapper mapper = new ObjectMapper();
        FacebookTokenResponse facebookTokenResponse = mapper.readValue(tokenResponse.getBody(), FacebookTokenResponse.class);

        System.out.println(tokenResponse.toString());

        //Next, we need validate this token

        String validateTokenRequestURI = FacebookUtils.prepareValidateTokenRequest(application, facebookTokenResponse);

        ClientHttpRequest validateTokenRequest =
                httpRequestFactory.createRequest(URI.create(validateTokenRequestURI), HttpMethod.GET);

        ClientHttpResponse validateTokenResponse = validateTokenRequest.execute();

        FacebookValidateTokenResponse facebookValidateTokenResponse =
                mapper.readValue(validateTokenResponse.getBody(), FacebookValidateTokenResponse.class);

        if(!facebookValidateTokenResponse.getData().getAppId().equals(appId)){
            throw new Exception("Something went wrong!!!");
        }

        System.out.println("User id = " + facebookValidateTokenResponse.getData().getUserId());


        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://google.com");

        return redirectView;
    }
}
