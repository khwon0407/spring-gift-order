package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "option_id", nullable = false)
    private Long optionId;
    
    @Column(name = "quantity", nullable = false)
    private Long quantity;
    
    @Column(name = "order_date_time", nullable = false)
    private LocalDateTime orderDateTime;
    
    @Column(name = "message", nullable = false)
    private String message;
    
    protected Order() {}
    
    public Order(Long optionId, Long quantity, LocalDateTime orderDateTime,
        String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }
    
    public Order(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime,
        String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }
    
    public Long getId() {
        return id;
    }
    
    public Long getOptionId() {
        return optionId;
    }
    
    public Long getQuantity() {
        return quantity;
    }
    
    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }
    
    public String getMessage() {
        return message;
    }
}
