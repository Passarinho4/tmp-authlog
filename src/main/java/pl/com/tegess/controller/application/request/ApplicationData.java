package pl.com.tegess.controller.application.request;

public class ApplicationData {

    private String appId;
    private String facebookAppId;

    public ApplicationData() {

    }

    public ApplicationData(String appId, String facebookAppId) {
        this.appId = appId;
        this.facebookAppId = facebookAppId;
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
}
