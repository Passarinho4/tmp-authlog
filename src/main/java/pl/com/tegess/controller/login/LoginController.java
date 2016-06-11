package pl.com.tegess.controller.login;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import pl.com.tegess.controller.login.facebook.FacebookLoginHelper;
import pl.com.tegess.controller.login.facebook.FacebookUtils;
import pl.com.tegess.controller.login.request.facebook.FacebookTokenResponse;
import pl.com.tegess.controller.login.request.facebook.FacebookUserData;
import pl.com.tegess.controller.login.request.facebook.FacebookValidateTokenResponse;
import pl.com.tegess.domain.application.Application;
import pl.com.tegess.domain.application.ApplicationRepository;
import pl.com.tegess.domain.user.User;
import pl.com.tegess.domain.user.service.UserService;

@RestController
@Component
public class LoginController {

    @Autowired
    ApplicationRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private FacebookLoginHelper facebookLoginHelper;

    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "api/login/facebook", method = RequestMethod.GET)
    public RedirectView loginByFacebook(@RequestParam String appId) {

        Application application = repository.findOne(new ObjectId(appId));

        RedirectView view = new RedirectView();
        view.setUrl(FacebookUtils.prepareCodeRequest(application));

        return view;
    }

    @RequestMapping(value = "api/logged/facebook", method = RequestMethod.GET)
    public RedirectView loggedByFacebook(
            @RequestParam String appId,
            @RequestParam String code) throws Exception {

        Application application = repository.findOne(new ObjectId(appId));

        //Firstly we need obtain token from FB.
        FacebookTokenResponse facebookTokenResponse = facebookLoginHelper.getFacebookTokenResponse(code, application);

        //Next, we need get our application token
        String applicationToken = facebookLoginHelper.getFacebookApplicationToken(application);

        //Next, we need validate this token
        FacebookValidateTokenResponse facebookValidateTokenResponse =
                facebookLoginHelper.getFacebookValidateTokenResponse(application, facebookTokenResponse, applicationToken);

        System.out.println("User id = " + facebookValidateTokenResponse.getData().getUser_id());

        //Get user data
        String userInfoRequestURI =
                FacebookUtils.prepareUserInfoRequest(facebookValidateTokenResponse.getData().getUser_id());


        HttpEntity<Object> objectHttpEntity = FacebookUtils.prepareHeadersForFacebookAuthorization(facebookTokenResponse);

        ResponseEntity<FacebookUserData> responseEntity =
                restTemplate.exchange(userInfoRequestURI, HttpMethod.GET, objectHttpEntity, FacebookUserData.class);

        //Create user and add it to db
        User user = userService.createUser(appId, responseEntity.getBody());

        //Generate JWTToken and redirect to Client App.
        String token = tokenManager.generateJWTTokenForUser(user, application);

        String url = application.getRedirectURI() + "?token=" + token;

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);

        return redirectView;
    }
}
