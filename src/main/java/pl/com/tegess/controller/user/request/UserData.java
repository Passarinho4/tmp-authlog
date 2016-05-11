package pl.com.tegess.controller.user.request;

public class UserData {

    private String username;
    private String mail;
    private String pictureURL;

    public UserData() {
    }

    public UserData(String username, String mail, String pictureURL) {
        this.username = username;
        this.mail = mail;
        this.pictureURL = pictureURL;
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
}
