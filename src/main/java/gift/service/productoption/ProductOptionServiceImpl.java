package gift.service.productoption;

import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.OptionResponseDto;
import gift.entity.Product;
import gift.entity.ProductOption;
import gift.exception.badrequest.LessQuantityException;
import gift.exception.badrequest.WrongProductOptionException;
import gift.exception.notfound.NoOptionInfoException;
import gift.exception.notfound.NoProductInfoException;
import gift.repository.product.ProductRepository;
import gift.repository.productoption.ProductOptionRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductOptionServiceImpl implements ProductOptionService {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    
    public ProductOptionServiceImpl(ProductRepository productRepository,
        ProductOptionRepository productOptionRepository) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
    }
    
    @Override
    public List<OptionResponseDto> findProductOptionsById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(NoProductInfoException::new);
        return product.getOptions().stream().map(option -> new OptionResponseDto(
            option.getId(),
            option.getName(),
            option.getQuantity()
        )).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public OptionResponseDto addOptionsToProduct(Long id, OptionRequestDto optionRequestDto) {
        Product product = productRepository.findById(id).orElseThrow(NoProductInfoException::new);
        ProductOption option = new ProductOption(null, optionRequestDto.name(), optionRequestDto.quantity(), product);
        product.addOption(option);
        productRepository.save(product);
        
        ProductOption savedOption = product.lastOption();
        
        return new OptionResponseDto(
            savedOption.getId(),
            savedOption.getName(),
            savedOption.getQuantity()
        );
    }
    
    @Override
    @Transactional
    public void deleteOptionToProduct(Long productId, Long optionId) {
        Product product = productRepository.findById(productId).orElseThrow(NoProductInfoException::new);
        ProductOption option = productOptionRepository.findById(optionId).orElseThrow(NoOptionInfoException::new);
        
        if(!product.hasOption(option) || !option.isNotForProduct(product)) {
            throw new WrongProductOptionException();
        }
        
        product.removeOption(option);
        
        productOptionRepository.deleteById(optionId);
    }
    
    @Override
    @Transactional
    public OptionResponseDto modifyOptionsToProduct(Long productId, Long optionId,
        OptionRequestDto optionRequestDto) {
        Product product = productRepository.findById(productId).orElseThrow(NoProductInfoException::new);
        ProductOption option = productOptionRepository.findById(optionId).orElseThrow(NoOptionInfoException::new);
        
        if(!product.hasOption(option) || !option.isNotForProduct(product)) {
            throw new WrongProductOptionException();
        }
        
        option.changeInfo(optionRequestDto.name(), optionRequestDto.quantity());
        ProductOption saved = productOptionRepository.save(option);
        
        return new OptionResponseDto(
            saved.getId(),
            saved.getName(),
            saved.getQuantity()
        );
    }
    
    @Override
    @Transactional
    public void decreaseOptionQuantity(Long optionId, Long quantity) {
        ProductOption option = productOptionRepository.findById(optionId)
            .orElseThrow(NoOptionInfoException::new);
        
        Long currentQuantity = option.getQuantity();
        if (currentQuantity == null || currentQuantity < quantity) {
            throw new LessQuantityException();
        }
        
        option.decreaseQuantity(quantity);
        
        productOptionRepository.save(option);
    }
}
