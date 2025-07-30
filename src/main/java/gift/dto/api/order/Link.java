package gift.dto.api.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Link {
    @JsonProperty("web_url")
    private String webUrl;
    
    @JsonProperty("mobile_web_url")
    private String mobileWebUrl;

    public Link(String webUrl, String mobileWebUrl) {
        this.webUrl = webUrl;
        this.mobileWebUrl = mobileWebUrl;
    }
    
    public String getWebUrl() {
        return webUrl;
    }
    
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
    
    public String getMobileWebUrl() {
        return mobileWebUrl;
    }
    
    public void setMobileWebUrl(String mobileWebUrl) {
        this.mobileWebUrl = mobileWebUrl;
    }
}