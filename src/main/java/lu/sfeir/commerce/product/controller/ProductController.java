package lu.sfeir.commerce.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lu.sfeir.commerce.product.dto.ProductDto;
import lu.sfeir.commerce.product.services.ProductService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;
	
	@GetMapping("")
	public ResponseEntity<List<ProductDto>> getAllProduct(){
		
		return ResponseEntity.ok(productService.getAll());
		
	}
	
	@GetMapping("/stocks")
	public ResponseEntity<List<ProductDto>> getAllProductWithStock(){
		
		return ResponseEntity.ok(productService.getAllWithStock());
		
	}

}
