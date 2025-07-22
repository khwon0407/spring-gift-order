package gift.service.product;

import gift.dto.api.product.AddProductRequestDto;
import gift.dto.api.product.ModifyProductRequestDto;
import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.ProductResponseDto;
import gift.entity.ProductOption;
import gift.entity.Product;
import gift.exception.badrequest.CheckMdOkException;
import gift.exception.badrequest.FillAllInfoException;
import gift.exception.badrequest.FillSomeInfoException;
import gift.exception.badrequest.WrongCriteriaException;
import gift.exception.badrequest.WrongOrderException;
import gift.exception.notfound.NoProductInfoException;
import gift.repository.product.ProductRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
    
    private static final List<String> VALID_CRITERIA = List.of("id", "price", "name");
    
    private final ProductRepository productRepository;
    
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    //상품 추가 Service
    @Override
    @Transactional
    public ProductResponseDto addProduct(AddProductRequestDto requestDto) {
        
        if(!requestDto.goodName()) {
            throw new CheckMdOkException();
        }
        
        Product product = new Product(
            null,
            requestDto.name(),
            requestDto.price(),
            requestDto.imageUrl()
        );
        
        for (OptionRequestDto optionDto : requestDto.options()) {
            ProductOption option = new ProductOption(
                null,
                optionDto.name(),
                optionDto.quantity(),
                product // 아직 저장 전이지만 연관관계 설정 가능
            );
            product.addOption(option); // addOptions에서 product 세팅도 해줄 거니까
        }
        
        Product saved = productRepository.save(product);
        
        return new ProductResponseDto(saved);
    }
    
    //상품 전체 조회
    @Override
    public Page<ProductResponseDto> findAllProducts(int pageNo, int pageSize, String criteria, String order) {
        
        if (!VALID_CRITERIA.contains(criteria)) {
            throw new WrongCriteriaException();
        }
        
        if(!order.equals("ASC") && !order.equals("DESC")) {
            throw new WrongOrderException();
        }
        
        var orderValue = order.equals("ASC") ? Direction.ASC : Direction.DESC;
        
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orderValue, criteria));
        
        return productRepository.findAll(pageable).map(ProductResponseDto::new);
    }
    
    //상품 단건 조회
    @Override
    public ProductResponseDto findProductWithId(Long id) {
        Product product = productRepository.findById(id).orElseThrow(NoProductInfoException::new);
        return new ProductResponseDto(product);
    }
    
    //상품 수정 (상품 자체가 다른 것으로 바뀜)
    @Override
    @Transactional
    public ProductResponseDto modifyProductWithId(Long id,
        ModifyProductRequestDto requestDto) {
        
        if (requestDto.isNotValidForModify()) {
            throw new FillAllInfoException();
        }
        
        if(!requestDto.goodName()) {
            throw new CheckMdOkException();
        }
        
        Product product = productRepository.findById(id).orElseThrow(NoProductInfoException::new);
        
        product.changeProductInfo(requestDto.name(), requestDto.price(), requestDto.imageUrl());
        
        return new ProductResponseDto(productRepository.save(product));
    }
    
    //상품 단건 삭제
    @Override
    @Transactional
    public void deleteProductWithId(Long id) {
        productRepository.deleteById(id);
    }
    
    //상품 수정 (일부 내용이 바뀜)
    @Override
    @Transactional
    public ProductResponseDto modifyProductInfoWithId(Long id,
        ModifyProductRequestDto requestDto) {
        
        if (requestDto.isNotValidForModifyInfo()) {
            throw new FillSomeInfoException();
        }
        
        if(!requestDto.goodName()) {
            throw new CheckMdOkException();
        }
        
        Product product = productRepository.findById(id).orElseThrow(NoProductInfoException::new);
        
        if(requestDto.name() != null) {
            product.changeName(requestDto.name());
        }
        
        if(requestDto.price() != null) {
            product.changePrice(requestDto.price());
        }
        
        if(requestDto.imageUrl() != null) {
            product.changeImageUrl(requestDto.imageUrl());
        }
        
        return new ProductResponseDto(productRepository.save(product));
    }
}
