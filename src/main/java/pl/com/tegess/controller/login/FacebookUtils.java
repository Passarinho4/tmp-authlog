package pl.com.tegess.controller.login;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import pl.com.tegess.controller.login.request.FacebookApplicationTokenResponse;
import pl.com.tegess.controller.login.request.FacebookTokenResponse;
import pl.com.tegess.domain.application.Application;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FacebookUtils {


    public static String prepareCodeRequest(Application application) {
        return "https://facebook.com/dialog/oauth?" +
                "client_id=" + application.getFacebookAppId() +
                "&redirect_uri=http://51.255.48.55:8085/api/logged?appId="+application.getAppId() +
                "&scope=public_profile,email";
    }

    public static String prepareTokenRequest(Application application, String code) {
        return "https://graph.facebook.com/v2.6/oauth/access_token?" +
                "client_id=" + application.getFacebookAppId() +
                "&redirect_uri=http://51.255.48.55:8085/api/logged?appId=" + application.getAppId() +
                "&client_secret=" + application.getSecret() +
                "&code=" + code;
    }

    public static String prepareApplicationTokenRequest(Application application) {
        return "https://graph.facebook.com/v2.6/oauth/access_token?" +
                "client_id=" + application.getFacebookAppId() +
                "&client_secret=" + application.getSecret() +
                "&grant_type=client_credentials";
    }

    public static String prepareValidateTokenRequest(FacebookTokenResponse tokenResponse, String applicationToken)
            throws UnsupportedEncodingException {
        return "https://graph.facebook.com/debug_token?" +
                "input_token=" + tokenResponse.getAccess_token() +
                "&access_token="+ URLEncoder.encode(applicationToken, "UTF-8");
    }

    public static String prepareUserInfoRequest(String user_id) {
        return "https://graph.facebook.com/v2.6/"+user_id +
                "?fields=id,name,email,gender,locale,picture";
    }

    public static HttpEntity<Object> prepareHeadersForFacebookAuthorization(FacebookTokenResponse facebookTokenResponse) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + facebookTokenResponse.getAccess_token());
        HttpEntity<Object> objectHttpEntity = new HttpEntity<>(httpHeaders);
        return objectHttpEntity;
    }
}
