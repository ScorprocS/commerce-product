package lu.sfeir.commerce.product.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductDto {
	private Long id;
	private String name; 
	private BigDecimal price; 
	private Long numberAvailable;


}
