package pl.com.tegess.controller.admin.request;

public class AdminWriteData {

    private String username;
    private String password;

    public AdminWriteData() {
    }

    public AdminWriteData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
