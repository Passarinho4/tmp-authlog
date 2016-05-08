package pl.com.tegess.controller.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
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
import pl.com.tegess.controller.login.request.FacebookApplicationTokenResponse;
import pl.com.tegess.controller.login.request.FacebookTokenResponse;
import pl.com.tegess.controller.login.request.FacebookValidateTokenResponse;
import pl.com.tegess.domain.application.Application;
import pl.com.tegess.domain.application.ApplicationRepository;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

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

        Application application = repository.findOne(new ObjectId(appId));

        //Firstly we need obtain token from FB.
        FacebookTokenResponse facebookTokenResponse = getFacebookTokenResponse(code, application);

        //Next, we need get our application token
        String applicationToken = getFacebookApplicationToken(application);

        //Next, we need validate this token
        FacebookValidateTokenResponse facebookValidateTokenResponse =
                getFacebookValidateTokenResponse(application, facebookTokenResponse, applicationToken);

        System.out.println("User id = " + facebookValidateTokenResponse.getData().getUserId());


        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://google.com");

        return redirectView;
    }

    private String getFacebookApplicationToken(Application application) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String requestURI = FacebookUtils.prepareApplicationTokenRequest(application);

        ClientHttpRequest request = httpRequestFactory.createRequest(URI.create(requestURI), HttpMethod.GET);

        ClientHttpResponse response = request.execute();

        FacebookApplicationTokenResponse facebookApplicationTokenResponse =
                mapper.readValue(response.getBody(), FacebookApplicationTokenResponse.class);

        return facebookApplicationTokenResponse.getAccess_token();
    }

    private FacebookValidateTokenResponse getFacebookValidateTokenResponse(Application application, FacebookTokenResponse facebookTokenResponse, String applicationToken) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String requestURI = FacebookUtils.prepareValidateTokenRequest(facebookTokenResponse, applicationToken);

        ClientHttpRequest request = httpRequestFactory.createRequest(URI.create(requestURI), HttpMethod.GET);

        ClientHttpResponse response = request.execute();

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String s = IOUtils.toString(response.getBody(), Charset.defaultCharset());
        System.out.println(s);

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        FacebookValidateTokenResponse facebookValidateTokenResponse =
                mapper.readValue(response.getBody(), FacebookValidateTokenResponse.class);

        if(!facebookValidateTokenResponse.getData().getAppId().equals(application.getAppId().toString())){
            throw new Exception("Something went wrong!!!");
        }
        return facebookValidateTokenResponse;
    }

    private FacebookTokenResponse getFacebookTokenResponse(String code, Application application) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String requestURI = FacebookUtils.prepareTokenRequest(application, code);

        ClientHttpRequest request = httpRequestFactory.createRequest(URI.create(requestURI), HttpMethod.GET);
        ClientHttpResponse response = request.execute();

        FacebookTokenResponse facebookTokenResponse = mapper.readValue(response.getBody(), FacebookTokenResponse.class);

        System.out.println(response.toString());
        return facebookTokenResponse;
    }
}
