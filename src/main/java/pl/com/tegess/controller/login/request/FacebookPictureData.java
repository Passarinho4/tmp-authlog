package pl.com.tegess.controller.login.request;

public class FacebookPictureData {

    private boolean is_silhouette;
    private String url;

    public FacebookPictureData() {
    }

    public FacebookPictureData(boolean is_silhouette, String url) {
        this.is_silhouette = is_silhouette;
        this.url = url;
    }

    public boolean is_silhouette() {
        return is_silhouette;
    }

    public void setIs_silhouette(boolean is_silhouette) {
        this.is_silhouette = is_silhouette;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FacebookPictureData{" +
                "is_silhouette=" + is_silhouette +
                ", url='" + url + '\'' +
                '}';
    }
}
