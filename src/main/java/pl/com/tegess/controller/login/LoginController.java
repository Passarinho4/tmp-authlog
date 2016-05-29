package pl.com.tegess.controller.login;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import pl.com.tegess.controller.login.request.FacebookApplicationTokenResponse;
import pl.com.tegess.controller.login.request.FacebookTokenResponse;
import pl.com.tegess.controller.login.request.FacebookUserData;
import pl.com.tegess.controller.login.request.FacebookValidateTokenResponse;
import pl.com.tegess.domain.application.Application;
import pl.com.tegess.domain.application.ApplicationRepository;
import pl.com.tegess.domain.user.User;
import pl.com.tegess.domain.user.UserRepository;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

@RestController
@Component
public class LoginController {

    @Autowired
    ApplicationRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenManager tokenManager;

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

        //Get user data
        String userInfoRequestURI =
                FacebookUtils.prepareUserInfoRequest(facebookValidateTokenResponse.getData().getUser_id());


        HttpEntity<Object> objectHttpEntity = FacebookUtils.prepareHeadersForFacebookAuthorization(facebookTokenResponse);

        ResponseEntity<FacebookUserData> responseEntity =
                restTemplate.exchange(userInfoRequestURI, HttpMethod.GET, objectHttpEntity, FacebookUserData.class);

        //Create user and add it to db
        User user = createUser(appId, responseEntity.getBody());

        //Generate JWTToken and redirect to Client App.
        String token = tokenManager.generateJWTTokenForUser(user, application);

        String url = UriComponentsBuilder.fromUriString(application.getRedirectURI())
                .queryParam("token", token)
                .build()
                .toUriString();

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);

        return redirectView;
    }

    private User createUser(String appId, FacebookUserData userData) {
        User user = new User(new ObjectId(),
                userData.getName(),
                userData.getPicture().getData().getUrl(),
                userData.getEmail(),
                userData.getGender(),
                new Locale(userData.getLocale()),
                new ObjectId(appId));

        userRepository.insert(user);

        return user;
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
