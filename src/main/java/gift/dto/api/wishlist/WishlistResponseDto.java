package gift.dto.api.wishlist;

import gift.entity.WishlistInfo;

public class WishlistResponseDto {
    
    private Long productId;
    private String productName;
    private Long productCnt;
    
    public WishlistResponseDto(Long productId, String productName, Long productCnt) {
        this.productId = productId;
        this.productName = productName;
        this.productCnt = productCnt;
    }
    
    public WishlistResponseDto() {
        this(null, null, null);
    }
    
    public WishlistResponseDto(WishlistInfo wishlistInfo) {
        this(wishlistInfo.getProduct().getId(), wishlistInfo.getProduct().getName(),
            wishlistInfo.getProductCnt());
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public Long getProductCnt() {
        return productCnt;
    }
}
