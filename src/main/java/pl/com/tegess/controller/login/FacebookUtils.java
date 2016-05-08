package pl.com.tegess.controller.login;

import pl.com.tegess.domain.application.Application;

public class FacebookUtils {


    public static String prepareCodeRequest(Application application) {
        return "https://facebook.com/dialog/oauth?" +
                "  client_id=" + application.getFacebookAppId() +
                "  &redirect_uri=http://51.255.48.55:8085/api/logged?appId="+application.getAppId();
    }

}
