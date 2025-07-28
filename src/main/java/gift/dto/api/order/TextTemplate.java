package gift.dto.api.order;

public class TextTemplate {
    private String object_type;
    private String text;
    private Link link;
    private String button_title;

    public TextTemplate(String text) {
        this.object_type = "text";
        this.text = text;
        this.link = new Link("https://your-site.com", "https://your-site.com");
        this.button_title = "확인";
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
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

    public String getButton_title() {
        return button_title;
    }

    public void setButton_title(String button_title) {
        this.button_title = button_title;
    }
}
