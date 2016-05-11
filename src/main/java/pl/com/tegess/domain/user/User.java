package pl.com.tegess.domain.user;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Locale;
import java.util.Optional;

public class User {

    @Id
    private final ObjectId id;
    private final String username;
    private final String password;
    private final String pictureURL;
    private final String mail;
    private final String gender;
    private final Locale locale;

    public User(ObjectId id, String username, String pictureURL, String mail, String gender, Locale locale) {
        this.id = id;
        this.username = username;
        this.password = null;
        this.pictureURL = pictureURL;
        this.mail = mail;
        this.gender = gender;
        this.locale = locale;
    }

    public User(ObjectId id,
                String username,
                String password,
                String pictureURL,
                String mail,
                String gender,
                Locale locale) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.pictureURL = pictureURL;
        this.mail = mail;
        this.gender = gender;
        this.locale = locale;
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
}
