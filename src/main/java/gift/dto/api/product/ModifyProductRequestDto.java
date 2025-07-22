package gift.dto.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ModifyProductRequestDto(
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,15}$",
        message = "이름은 한글, 영문, 숫자, 공백, (), [], +, -, &, /, _ 만 포함할 수 있으며 최대 15자까지 입력 가능합니다."
    )
    String name,
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    Long price,
    String imageUrl,
    @NotNull(
        message = "MD 협의 여부는 필수입니다."
    )
    Boolean mdOk
) {
    
    public ModifyProductRequestDto(String name, Long price, String imageUrl, Boolean mdOk) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdOk = mdOk;
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    @JsonProperty("mdOk")
    public Boolean mdOk() {
        return mdOk;
    }
    
    
    public Boolean isNotValidForModify() {
        return (name == null || price == null || imageUrl == null);
    }
    
    public Boolean isNotValidForModifyInfo() {
        return (name == null && price == null && imageUrl == null);
    }
    
    public Boolean goodName() {
        if (name.contains("카카오")) {
            return mdOk;
        }
        return true;
    }
}
