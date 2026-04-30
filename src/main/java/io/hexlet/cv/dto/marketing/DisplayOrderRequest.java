package io.hexlet.cv.dto.marketing;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisplayOrderRequest {
    @NotNull(message = "display_order must not be null")
    @Min(value = 0, message = "display_order must be >= 0")
    private Integer displayOrder;
}
