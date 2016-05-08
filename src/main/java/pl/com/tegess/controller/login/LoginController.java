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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.com.tegess.domain.application.Application;
import pl.com.tegess.domain.application.ApplicationRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
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

        String tokenRequest = FacebookUtils.prepareTokenRequest(application, code);

        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        ClientHttpRequest request = httpRequestFactory.createRequest(URI.create(tokenRequest), HttpMethod.GET);
        ClientHttpResponse response = request.execute();

        ObjectMapper mapper = new ObjectMapper();
        FacebookTokenResponse tokenResponse = mapper.readValue(response.getBody(), FacebookTokenResponse.class);

        System.out.println(tokenResponse.toString());

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://google.com");

        return redirectView;
    }
}
