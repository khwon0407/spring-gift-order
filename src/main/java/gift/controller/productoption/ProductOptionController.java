package gift.controller.productoption;

import gift.config.annotation.ValidHeader;
import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.OptionResponseDto;
import gift.entity.Role;
import gift.service.productoption.ProductOptionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class ProductOptionController {
    private final ProductOptionService productOptionService;
    
    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }
    
    @GetMapping
    public ResponseEntity<List<OptionResponseDto>> findProductOptionsById(
        @PathVariable(name = "productId") Long id
    ) {
        List<OptionResponseDto> productOptions = productOptionService.findProductOptionsById(id);
        return new ResponseEntity<>(productOptions, HttpStatus.OK);
    }
    
    @PostMapping
    @ValidHeader(role = Role.ADMIN)
    public ResponseEntity<OptionResponseDto> addOptionsToProduct(
        @PathVariable(name = "productId") Long id,
        @RequestBody @Valid OptionRequestDto optionRequestDto
    ) {
        OptionResponseDto responseDto = productOptionService.addOptionsToProduct(id, optionRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{optionId}")
    @ValidHeader(role = Role.ADMIN)
    public ResponseEntity<Void> deleteOptionsToProduct(
        @PathVariable(name = "productId") Long productId,
        @PathVariable(name = "optionId") Long optionId
    ) {
        productOptionService.deleteOptionToProduct(productId, optionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping("/{optionId}")
    @ValidHeader(role = Role.ADMIN)
    public ResponseEntity<OptionResponseDto> modifyOptionsToProduct(
        @PathVariable(name = "productId") Long productId,
        @PathVariable(name = "optionId") Long optionId,
        @RequestBody @Valid OptionRequestDto optionRequestDto
    ) {
        OptionResponseDto responseDto = productOptionService.modifyOptionsToProduct(productId, optionId, optionRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
