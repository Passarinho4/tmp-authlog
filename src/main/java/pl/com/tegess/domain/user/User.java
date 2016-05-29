package pl.com.tegess.domain.user;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class User {

    @Id
    private ObjectId id;
    private String username;
    private String password;
    private String pictureURL;
    private String mail;
    private String gender;
    private Locale locale;
    private ObjectId appId;
    private List<String> privileges;

    public User() {
    }

    public User(ObjectId id, String username, String pictureURL, String mail, String gender, Locale locale, ObjectId appId) {
        this(id, username, null, pictureURL, mail, gender, locale, appId, new ArrayList<>());
    }

    public User(ObjectId id,
                String username,
                String password,
                String pictureURL,
                String mail,
                String gender,
                Locale locale,
                ObjectId appId,
                List<String> privileges) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.pictureURL = pictureURL;
        this.mail = mail;
        this.gender = gender;
        this.locale = locale;
        this.appId = appId;
        this.privileges = privileges;
    }

    public ObjectId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getMail() {
        return mail;
    }

    public String getGender() {
        return gender;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ObjectId getAppId() {
        return appId;
    }

    public List<String> getPrivileges() {
        return privileges;
    }

    public void addPrivilege(String privilege) {
        this.privileges.add(privilege);
    }

    public void deletePrivilege(String privilege) {
        this.privileges.remove(privilege);
    }
}
