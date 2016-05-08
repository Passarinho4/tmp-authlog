package pl.com.tegess.controller.login;

import pl.com.tegess.controller.login.request.FacebookTokenResponse;
import pl.com.tegess.domain.application.Application;

public class FacebookUtils {


    public static String prepareCodeRequest(Application application) {
        return "https://facebook.com/dialog/oauth?" +
                "client_id=" + application.getFacebookAppId() +
                "&redirect_uri=http://51.255.48.55:8085/api/logged?appId="+application.getAppId();
    }

    public static String prepareTokenRequest(Application application, String code) {
        return "https://graph.facebook.com/v2.6/oauth/access_token?" +
                "client_id=" + application.getFacebookAppId() +
                "&redirect_uri=http://51.255.48.55:8085/api/logged?appId=" + application.getAppId() +
                "&client_secret=" + application.getSecret() +
                "&code=" + code;
    }

    public static String prepareValidateTokenRequest(FacebookTokenResponse tokenResponse, String applicationToken) {
        return "https://graph.facebook.com/debug_token?" +
                "input_token=" + tokenResponse.getAccess_token() +
                "&access_token="+applicationToken;
    }

    public static String prepareApplicationTokenRequest(Application application) {
        return "/oauth/access_token?" +
                "client_id=" + application.getAppId() +
                "&client_secret=" + application.getSecret() +
                "&grant_type=client_credentials";
    }
}
