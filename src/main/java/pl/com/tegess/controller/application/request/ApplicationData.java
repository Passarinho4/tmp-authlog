package pl.com.tegess.controller.application.request;

public class ApplicationData {

    private String appId;
    private String facebookAppId;
    private String redirectURI;

    public ApplicationData() {

    }

    public ApplicationData(String appId, String facebookAppId, String redirectURI) {
        this.appId = appId;
        this.facebookAppId = facebookAppId;
        this.redirectURI = redirectURI;
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

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }
}
