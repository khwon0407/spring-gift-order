package gift.dto.api.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TextTemplate {
    @JsonProperty("object_type")
    private String objectType;
    
    @JsonProperty("text")
    private String text;
    
    @JsonProperty("link")
    private Link link;
    
    @JsonProperty("button_title")
    private String buttonTitle;
    
    public TextTemplate(String text) {
        this.objectType = "text";
        this.text = text;
        this.link = new Link("https://your-site.com", "https://your-site.com");
        this.buttonTitle = "확인";
    }
    
    public String getObjectType() {
        return objectType;
    }
    
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Link getLink() {
        return link;
    }
    
    public void setLink(Link link) {
        this.link = link;
    }
    
    public String getButtonTitle() {
        return buttonTitle;
    }
    
    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }
}
