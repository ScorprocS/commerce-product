package lu.sfeir.commerce.product.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lu.sfeir.commerce.product.dto.ProductDto;
import lu.sfeir.commerce.product.dto.StockDto;
import lu.sfeir.commerce.product.entity.Product;
import lu.sfeir.commerce.product.repository.ProductRepository;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final StockService stockService;
	private final ProductRepository productRepository;

	public List<ProductDto> getAllWithStock() {
		List<StockDto> stocks = stockService.getStocks();
		List<Product> products = productRepository.findAll();
		List<ProductDto> productDtos = new ArrayList<>();
		products.stream().forEach(p -> {
			productDtos
					.add(ProductDto.builder().id(p.getId()).name(p.getName()).price(p.getPrice())
							.numberAvailable(stocks.stream().filter(s -> s.getProductId().equals(p.getId())).findFirst()
									.orElse(StockDto.builder().numberAvailable(0l).build()).getNumberAvailable())
							.build());
		});

		return productDtos;
	}
	
	public List<ProductDto> getAll() {
		List<Product> products = productRepository.findAll();
		List<ProductDto> productDtos = new ArrayList<>();
		products.stream().forEach(p -> {
			productDtos
					.add(ProductDto.builder().id(p.getId()).name(p.getName()).price(p.getPrice()).build());
		});

		return productDtos;
	}

	
	public void updateProductStock(Long id, Long newNumber ) {
		Optional<Product> optional =productRepository.findById(id);
		if(optional.isPresent()) {
			optional.get().setQuantity(newNumber);
			productRepository.save(optional.get());
		}
	}
	
	

}
