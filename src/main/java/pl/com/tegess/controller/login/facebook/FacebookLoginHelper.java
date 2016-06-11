package pl.com.tegess.controller.login.facebook;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.com.tegess.controller.login.request.facebook.FacebookApplicationTokenResponse;
import pl.com.tegess.controller.login.request.facebook.FacebookTokenResponse;
import pl.com.tegess.controller.login.request.facebook.FacebookValidateTokenResponse;
import pl.com.tegess.domain.application.Application;

import java.io.IOException;

@Component
public class FacebookLoginHelper {

    private RestTemplate restTemplate = new RestTemplate();

    public String getFacebookApplicationToken(Application application) throws IOException {

        String requestURI = FacebookUtils.prepareApplicationTokenRequest(application);

        FacebookApplicationTokenResponse facebookApplicationTokenResponse =
                restTemplate.getForObject(requestURI, FacebookApplicationTokenResponse.class);

        return facebookApplicationTokenResponse.getAccess_token();
    }

    public FacebookValidateTokenResponse getFacebookValidateTokenResponse(Application application, FacebookTokenResponse facebookTokenResponse, String applicationToken) throws Exception {
        String requestURI = FacebookUtils.prepareValidateTokenRequest(facebookTokenResponse, applicationToken);

        FacebookValidateTokenResponse facebookValidateTokenResponse =
                restTemplate.getForObject(requestURI, FacebookValidateTokenResponse.class);

        if(!facebookValidateTokenResponse.getData().getApp_id().equals(application.getFacebookAppId())){
            throw new Exception("Something went wrong!!!");
        }
        return facebookValidateTokenResponse;
    }

    public FacebookTokenResponse getFacebookTokenResponse(String code, Application application) throws IOException {
        String requestURI = FacebookUtils.prepareTokenRequest(application, code);

        return restTemplate.getForObject(requestURI, FacebookTokenResponse.class);
    }
}
