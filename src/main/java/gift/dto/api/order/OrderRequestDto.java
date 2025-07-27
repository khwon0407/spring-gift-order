package gift.dto.api.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(
    @NotNull(message = "옵션 id는 필수입니다.")
    Long optionId,
    @NotNull(message = "상품 수량은 필수입니다.")
    Long quantity,
    @NotBlank(message = "메시지는 필수이며, 공백일 수 없습니다.")
    String message
) {
}
