package gift.dto.htmlform;

public class ModifyProductForm {
    
    private String name;
    private Long price;
    private String imageUrl;
    private Boolean mdOk;
    
    public ModifyProductForm() {
        this(null, null, null, false);
    }
    
    public ModifyProductForm(String name, Long price, String imageUrl, Boolean mdOk) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdOk = mdOk;
    }
    
    public String getName() {
        return name;
    }
    
    public Long getPrice() {
        return price;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public Boolean getMdOk() {
        return mdOk;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrice(Long price) {
        this.price = price;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    @SuppressWarnings("unused")
    public void setMdOk(Boolean mdOk) {
        this.mdOk = mdOk;
    }
}
