package lu.sfeir.commerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {
	private Long id;
	private Long numberAvailable;
	private Long productId;


}
