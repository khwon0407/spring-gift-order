package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "product_options", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "name"})
})
public class ProductOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "quantity", nullable = false)
    private Long quantity;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_option_product"))
    private Product product;
    
    protected ProductOption() {
    
    }
    
    public ProductOption(Long id, String name, Long quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Long getQuantity() {
        return quantity;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void belongToProduct(Product product) {
        this.product = product;
    }
    
    public void decreaseQuantity(Long quantity) {
        this.quantity -= quantity;
    }
    
    public void changeInfo(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    
    public boolean isNotForProduct(Product product) {
        return this.product.equals(product);
    }
}
