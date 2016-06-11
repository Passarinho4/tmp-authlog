package pl.com.tegess.controller.login.request.facebook;

public class FacebookUserData {

    private String id;
    private String name;
    private String email;
    private String gender;
    private String locale;
    private FacebookPicture picture;

    public FacebookUserData() {
    }

    public FacebookUserData(String id, String name, String email, String gender, String locale, FacebookPicture picture) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.locale = locale;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public FacebookPicture getPicture() {
        return picture;
    }

    public void setPicture(FacebookPicture picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "FacebookUserData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", locale='" + locale + '\'' +
                ", picture=" + picture +
                '}';
    }
}
