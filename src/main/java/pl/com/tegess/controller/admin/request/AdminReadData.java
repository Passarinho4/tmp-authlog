package pl.com.tegess.controller.admin.request;

public class AdminReadData {

    private String username;

    public AdminReadData() {
    }

    public AdminReadData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
