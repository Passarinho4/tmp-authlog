package pl.com.tegess.controller.application.request;

public class ApplicationData {

    private String appId;
    private String facebookAppId;
    private String facebookRedirectURI;
    private String secret;

    public ApplicationData() {

    }

    public ApplicationData(String appId, String facebookAppId, String secret, String facebookRedirectURI) {
        this.appId = appId;
        this.facebookAppId = facebookAppId;
        this.secret = secret;
        this.facebookRedirectURI = facebookRedirectURI;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public void setFacebookAppId(String facebookAppId) {
        this.facebookAppId = facebookAppId;
    }

    public String getFacebookRedirectURI() {
        return facebookRedirectURI;
    }

    public void setFacebookRedirectURI(String facebookRedirectURI) {
        this.facebookRedirectURI = facebookRedirectURI;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
