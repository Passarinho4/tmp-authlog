package pl.com.tegess.controller.login.request;

public class FacebookValidateTokenDateResponse {

    private String appId;
    private String userId;

    public FacebookValidateTokenDateResponse() {

    }

    public FacebookValidateTokenDateResponse(String appId, String userId) {
        this.appId = appId;
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
