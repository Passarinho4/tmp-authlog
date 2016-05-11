package pl.com.tegess.controller.login;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import pl.com.tegess.controller.login.request.FacebookApplicationTokenResponse;
import pl.com.tegess.controller.login.request.FacebookTokenResponse;
import pl.com.tegess.controller.login.request.FacebookUserData;
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
    private RestTemplate restTemplate = new RestTemplate();

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

        Application application = repository.findOne(new ObjectId(appId));

        //Firstly we need obtain token from FB.
        FacebookTokenResponse facebookTokenResponse = getFacebookTokenResponse(code, application);

        //Next, we need get our application token
        String applicationToken = getFacebookApplicationToken(application);

        //Next, we need validate this token
        FacebookValidateTokenResponse facebookValidateTokenResponse =
                getFacebookValidateTokenResponse(application, facebookTokenResponse, applicationToken);

        System.out.println("User id = " + facebookValidateTokenResponse.getData().getUser_id());

        //Test connection
        String userInfoRequestURI =
                FacebookUtils.prepareUserInfoRequest(facebookValidateTokenResponse.getData().getUser_id());


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + facebookTokenResponse.getAccess_token());
        HttpEntity<Object> objectHttpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<FacebookUserData> responseEntity =
                restTemplate.exchange(userInfoRequestURI, HttpMethod.GET, objectHttpEntity, FacebookUserData.class);

        System.out.println(responseEntity.getBody());

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.createRequest(URI.create(userInfoRequestURI), HttpMethod.GET);



        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://google.com");

        return redirectView;
    }

    private String getFacebookApplicationToken(Application application) throws IOException {

        String requestURI = FacebookUtils.prepareApplicationTokenRequest(application);

        FacebookApplicationTokenResponse facebookApplicationTokenResponse =
                restTemplate.getForObject(requestURI, FacebookApplicationTokenResponse.class);

        return facebookApplicationTokenResponse.getAccess_token();
    }

    private FacebookValidateTokenResponse getFacebookValidateTokenResponse(Application application, FacebookTokenResponse facebookTokenResponse, String applicationToken) throws Exception {
        String requestURI = FacebookUtils.prepareValidateTokenRequest(facebookTokenResponse, applicationToken);

        FacebookValidateTokenResponse facebookValidateTokenResponse =
                restTemplate.getForObject(requestURI, FacebookValidateTokenResponse.class);

        if(!facebookValidateTokenResponse.getData().getApp_id().equals(application.getFacebookAppId())){
            throw new Exception("Something went wrong!!!");
        }
        return facebookValidateTokenResponse;
    }

    private FacebookTokenResponse getFacebookTokenResponse(String code, Application application) throws IOException {
        String requestURI = FacebookUtils.prepareTokenRequest(application, code);

        return restTemplate.getForObject(requestURI, FacebookTokenResponse.class);
    }
}
