package gift.controller.wishlist;

import gift.config.annotation.CurrentUser;
import gift.config.annotation.ValidHeader;
import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import gift.entity.Member;
import gift.service.wishlist.WishlistService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    
    private final WishlistService wishlistService;
    
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }
    
    @GetMapping
    @ValidHeader
    public ResponseEntity<List<WishlistResponseDto>> findMyWishlist(
        @CurrentUser Member user,
        @RequestParam(required = false, value = "page", defaultValue = "0") int pageNo,
        @RequestParam(required = false, value = "size", defaultValue = "10") int pageSize,
        @RequestParam(required = false, value = "criteria", defaultValue = "id") String criteria,
        @RequestParam(required = false, value = "order", defaultValue = "ASC") String order
    ) {
        List<WishlistResponseDto> myWishlist = wishlistService.findMyWishlist(user, pageNo, pageSize, criteria, order);
        return new ResponseEntity<>(myWishlist, HttpStatus.OK);
    }
    
    @PostMapping
    @ValidHeader
    public ResponseEntity<WishlistResponseDto> addToMyWishlist(
        @CurrentUser Member user,
        @RequestBody WishlistRequestDto requestDto
    ) {
        WishlistResponseDto responseDto = wishlistService.addToMyWishlist(user, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{productId}")
    @ValidHeader
    public ResponseEntity<Void> deleteToMyWishlist(
        @CurrentUser Member user,
        @PathVariable(name = "productId") Long id
    ) {
        wishlistService.deleteFromMyWishlist(user, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    @PatchMapping
    @ValidHeader
    public ResponseEntity<?> modifyProductCntFromMyWishlist(
        @CurrentUser Member user,
        @RequestBody WishlistRequestDto requestDto
    ) {
        WishlistResponseDto responseDto = wishlistService.modifyProductCntFromMyWishlist(user, requestDto);
        if(responseDto == null) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
