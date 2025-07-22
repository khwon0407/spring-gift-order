package gift.entity;

import gift.exception.badrequest.NoRemoveOptionException;
import gift.exception.badrequest.WrongPriceException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "price", nullable = false)
    private Long price;
    
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();
    
    @PrePersist
    @PreUpdate
    private void validatePrice() {
        if (price == null || price < 0) {
            throw new WrongPriceException();
        }
    }
    
    public Product(Long id, String name, Long price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    
    protected Product() {
    
    }
    
    public Long getId() {
        return id;
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
    
    public List<ProductOption> getOptions() {
        return options;
    }
    
    public void changeProductInfo(String name, Long price, String imageUrl) {
        changeName(name);
        changePrice(price);
        changeImageUrl(imageUrl);
    }
    
    public void changeName(String name) {
        this.name = name;
    }
    
    public void changePrice(Long price) {
        this.price = price;
    }
    
    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public void addOption(ProductOption option) {
        this.options.add(option);
        option.belongToProduct(this);
    }
    
    public void removeOption(ProductOption option) {
        if(this.options.size() <= 1) {
            throw new NoRemoveOptionException();
        }
        this.options.remove(option);
        option.belongToProduct(null);
    }
    
    public ProductOption lastOption() {
        return this.options.get(this.options.size() - 1);
    }
    
    public boolean hasOption(ProductOption option) {
        return this.options.contains(option);
    }
}
