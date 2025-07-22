package gift.entity;

import gift.exception.badrequest.WrongProductCntException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "wishlist", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member_id", "product_id"})
})
public class WishlistInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_wishlist_member"))
    private Member member;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_wishlist_product"))
    private Product product;
    
    @Column(name = "product_cnt", nullable = false)
    private Long productCnt;
    
    @PrePersist
    @PreUpdate
    private void validateProductCnt() {
        if (productCnt == null || productCnt <= 0) {
            throw new WrongProductCntException();
        }
    }
    
    public WishlistInfo(Long id, Member member, Product product, Long productCnt) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.productCnt = productCnt;
    }
    
    protected WishlistInfo() {
    
    }
    
    public Member getMember() {
        return member;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public Long getProductCnt() {
        return productCnt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void changeProductCnt(Long productCnt) {
        this.productCnt = productCnt;
    }
}
