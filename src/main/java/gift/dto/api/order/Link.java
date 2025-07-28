package gift.dto.api.order;

public class Link {
    private String web_url;
    private String mobile_web_url;

    public Link(String webUrl, String mobileWebUrl) {
        this.web_url = webUrl;
        this.mobile_web_url = mobileWebUrl;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getMobile_web_url() {
        return mobile_web_url;
    }

    public void setMobile_web_url(String mobile_web_url) {
        this.mobile_web_url = mobile_web_url;
    }
}