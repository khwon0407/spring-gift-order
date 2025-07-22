package gift.controller.product;

import gift.config.annotation.ValidHeader;
import gift.dto.api.product.AddProductRequestDto;
import gift.dto.api.product.ModifyProductRequestDto;
import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.OptionResponseDto;
import gift.dto.api.product.ProductResponseDto;
import gift.entity.Role;
import gift.service.product.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @PostMapping
    @ValidHeader(role = Role.ADMIN)
    public ResponseEntity<ProductResponseDto> addProduct(
        @RequestBody @Valid AddProductRequestDto requestDto
    ) {
        ProductResponseDto responseDto = productService.addProduct(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAllProducts(
        @RequestParam(required = false, value = "page", defaultValue = "0") int pageNo,
        @RequestParam(required = false, value = "size", defaultValue = "10") int pageSize,
        @RequestParam(required = false, value = "criteria", defaultValue = "id") String criteria,
        @RequestParam(required = false, value = "order", defaultValue = "ASC") String order
    ) {
        List<ProductResponseDto> responseDtoList = productService
            .findAllProducts(pageNo, pageSize, criteria, order)
            .getContent();
        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> findProductWithId(
        @PathVariable(name="productId") Long id
    ) {
        ProductResponseDto responseDto = productService.findProductWithId(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    
    @PutMapping("/{productId}")
    @ValidHeader(role = Role.ADMIN)
    public ResponseEntity<ProductResponseDto> modifyProductWithId(
        @PathVariable(name="productId") Long id,
        @RequestBody @Valid ModifyProductRequestDto requestDto
    ) {
        ProductResponseDto responseDto = productService.modifyProductWithId(id, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    
    @PatchMapping("/{productId}")
    @ValidHeader(role = Role.ADMIN)
    public ResponseEntity<ProductResponseDto> modifyProductInfoWithId(
        @PathVariable(name="productId") Long id,
        @RequestBody @Valid ModifyProductRequestDto requestDto
    ) {
        ProductResponseDto responseDto = productService.modifyProductInfoWithId(id,
            requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    
    @DeleteMapping("/{productId}")
    @ValidHeader(role = Role.ADMIN)
    public ResponseEntity<Void> deleteProductWithId(
        @PathVariable(name="productId") Long id
    ) {
        productService.deleteProductWithId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
}
