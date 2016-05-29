package pl.com.tegess.controller.user.request;

import java.util.List;

public class UserData {

    private String id;
    private String username;
    private String mail;
    private String pictureURL;
    private List<String> privileges;

    public UserData() {
    }

    public UserData(String id, String username, String mail, String pictureURL, List<String> privileges) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.pictureURL = pictureURL;
        this.privileges = privileges;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }
}
