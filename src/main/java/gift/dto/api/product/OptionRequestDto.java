package gift.dto.api.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OptionRequestDto(
    @NotNull(message = "옵션명은 필수입니다.")
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]+$",
        message = "옵션명은 한글, 영문, 숫자, 공백, (), [], +, -, &, /, _ 만 포함할 수 있습니다."
    )
    String name,
    @NotNull(message = "수량은 필수 항목입니다.")
    @Min(value = 1, message = "옵션은 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션은 1억개 미만이어야 합니다.")
    Long quantity) {
    
}
