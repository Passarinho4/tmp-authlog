package pl.com.tegess.controller.login.request;

public class FacebookPicture {

    private FacebookPictureData data;

    public FacebookPicture() {
    }

    public FacebookPicture(FacebookPictureData data) {
        this.data = data;
    }

    public FacebookPictureData getData() {
        return data;
    }

    public void setData(FacebookPictureData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FacebookPicture{" +
                "data=" + data +
                '}';
    }
}
