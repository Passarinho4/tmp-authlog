package pl.com.tegess.controller.login.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookValidateTokenResponse {

    private FacebookValidateTokenDateResponse data;

    public FacebookValidateTokenResponse() {
    }

    public FacebookValidateTokenResponse(FacebookValidateTokenDateResponse data) {
        this.data = data;
    }

    public FacebookValidateTokenDateResponse getData() {
        return data;
    }

    public void setData(FacebookValidateTokenDateResponse data) {
        this.data = data;
    }
}
