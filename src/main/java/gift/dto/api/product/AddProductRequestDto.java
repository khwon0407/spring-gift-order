package gift.dto.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record AddProductRequestDto(
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]{1,15}$",
        message = "이름은 한글, 영문, 숫자, 공백, (), [], +, -, &, /, _ 만 포함할 수 있으며 최대 15자까지 입력 가능합니다."
    )
    @NotNull(message = "이름은 필수입니다.")
    String name,
    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    Long price,
    @NotNull(message = "이미지 url은 필수입니다.")
    String imageUrl,
    @NotNull(message = "MD 협의 여부는 필수입니다.")
    Boolean mdOk,
    
    @NotNull(message = "옵션 목록은 필수입니다.")
    @Size(min = 1, message = "상품에는 옵션이 최소 1개 이상 있어야 합니다.")
    List<@Valid OptionRequestDto> options
) {
    
    public Boolean goodName() {
        if (name.contains("카카오")) {
            return mdOk;
        }
        return true;
    }
}
