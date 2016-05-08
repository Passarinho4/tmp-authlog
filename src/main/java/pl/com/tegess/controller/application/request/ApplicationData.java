package pl.com.tegess.controller.application.request;

public class ApplicationData {

    private String appId;
    private String facebookAppId;
    private String facebookRedirectURI;

    public ApplicationData() {

    }

    public ApplicationData(String appId, String facebookAppId, String facebookRedirectURI) {
        this.appId = appId;
        this.facebookAppId = facebookAppId;
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
}
