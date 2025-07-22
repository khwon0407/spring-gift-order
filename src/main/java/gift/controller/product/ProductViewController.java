package gift.controller.product;

import gift.dto.api.product.AddProductRequestDto;
import gift.dto.api.product.ModifyProductRequestDto;
import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.ProductResponseDto;
import gift.dto.htmlform.AddProductForm;
import gift.dto.htmlform.ModifyProductForm;
import gift.service.product.ProductService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/products")
public class ProductViewController {
    
    private final ProductService productService;
    private final String PRODUCTS_LIST_PATH = "/admin/products";
    
    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }
    
    //main 화면, 상품 목록
    @GetMapping
    public String showListView(
        @RequestParam(name = "page", required = false, defaultValue = "0") int pageNo,
        @RequestParam(name = "size", required = false, defaultValue = "2") int pageSize,
        @RequestParam(name = "criteria", required = false, defaultValue = "id") String criteria,
        @RequestParam(name = "order", required = false, defaultValue = "ASC") String order,
        Model model
    ) {
        Page<ProductResponseDto> productPage = productService.findAllProducts(pageNo, pageSize, criteria, order);
        
        model.addAttribute("products", productPage.getContent()); // 목록
        model.addAttribute("page", productPage); // 페이지 정보 (for UI)
        model.addAttribute("criteria", criteria);
        return "product-list";
    }
    
    //특정 상품 상세 조회
    @GetMapping("/{productId}")
    public String showProductView(
        @PathVariable(name="productId") Long id,
        Model model
    ) {
        ProductResponseDto product = productService.findProductWithId(id);
        ModifyProductForm modifyForm = new ModifyProductForm(product.getName(),
            product.getPrice(), product.getImageUrl(), product.containKakao());
        
        model.addAttribute("product", product);
        model.addAttribute("modifyForm", modifyForm);
        
        return "product-detail";
    }
    
    //상품 추가 화면, 이름 / 가격 / 이미지 링크 입력 받도록 구성
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("productForm", new AddProductForm());
        
        return "product-add";
    }
    
    //상품 추가 화면에서 제출 버튼 누르면 동작
    @PostMapping("/add")
    public String addProduct(@ModelAttribute @Valid AddProductForm productForm) {
        List<OptionRequestDto> options = new ArrayList<>();
        options.add(new OptionRequestDto("기본 옵션", 1L));
        AddProductRequestDto requestDto = new AddProductRequestDto(
            productForm.getName(),
            productForm.getPrice(),
            productForm.getImageUrl(),
            productForm.getMdOk(),
            options
        );
        
        productService.addProduct(requestDto);
        
        return "redirect:" + PRODUCTS_LIST_PATH;
    }
    
    //상품 목록에서 수정 버튼 누를 시 수정화면 불러옴, 구성은 추가와 유사
    @GetMapping("/edit/{productId}")
    public String showModifyForm(@PathVariable(name="productId") Long id, Model model) {
        model.addAttribute("productId", id);
        model.addAttribute("productForm", new ModifyProductForm());
        
        return "product-edit";
    }
    
    //수정 화면에서 수정 누를 시 동작
    @PutMapping("/edit/{productId}")
    public String modifyProduct(@PathVariable(name="productId") Long id,
        @ModelAttribute ModifyProductForm productForm) {
        ModifyProductRequestDto requestDto = new ModifyProductRequestDto(
            productForm.getName(),
            productForm.getPrice(),
            productForm.getImageUrl(),
            productForm.getMdOk()
        );
        
        productService.modifyProductWithId(id, requestDto);
        
        return "redirect:" + PRODUCTS_LIST_PATH;
    }
    
    //조회 화면에서 수정 누를 시 동작
    @PatchMapping("/edit/{productId}")
    public String modifyInfoProduct(@PathVariable(name="productId") Long id,
        @ModelAttribute ModifyProductForm modifyForm) {
        ModifyProductRequestDto requestDto = new ModifyProductRequestDto(
            modifyForm.getName(),
            modifyForm.getPrice(),
            modifyForm.getImageUrl(),
            modifyForm.getMdOk()
        );
        
        productService.modifyProductInfoWithId(id, requestDto);
        
        return "redirect:" + PRODUCTS_LIST_PATH;
    }
    
    //삭제 버튼 누를 시 동작
    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable(name="productId") Long id) {
        productService.deleteProductWithId(id);
        
        return "redirect:" + PRODUCTS_LIST_PATH;
    }
}
