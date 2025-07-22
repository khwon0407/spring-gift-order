package gift.service.productoption;

import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.OptionResponseDto;
import java.util.List;

public interface ProductOptionService {
    List<OptionResponseDto> findProductOptionsById(Long id);
    OptionResponseDto addOptionsToProduct(Long id, OptionRequestDto optionRequestDto);
    
    void deleteOptionToProduct(Long productId, Long optionId);
    
    void decreaseOptionQuantity(Long optionId, Long quantity);
    
    OptionResponseDto modifyOptionsToProduct(Long productId, Long optionId, OptionRequestDto optionRequestDto);
}
