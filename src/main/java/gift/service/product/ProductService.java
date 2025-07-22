package gift.service.product;

import gift.dto.api.product.AddProductRequestDto;
import gift.dto.api.product.ModifyProductRequestDto;
import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.OptionResponseDto;
import gift.dto.api.product.ProductResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductService {
    
    ProductResponseDto addProduct(AddProductRequestDto requestDto);
    
    Page<ProductResponseDto> findAllProducts(int pageNo, int pageSize, String criteria, String order);
    
    ProductResponseDto findProductWithId(Long id);
    
    ProductResponseDto modifyProductWithId(Long id, ModifyProductRequestDto requestDto);
    
    void deleteProductWithId(Long id);
    
    ProductResponseDto modifyProductInfoWithId(Long id, ModifyProductRequestDto requestDto);
}
