package lu.sfeir.commerce.product.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lu.sfeir.commerce.product.dto.StockDto;

@FeignClient(value = "stocks", url = "http://localhost:8087/api", fallback = StockServiceFallback.class)
public interface StockService {
	
	 @RequestMapping(method = RequestMethod.GET, value = "/stocks")
	 List<StockDto> getStocks();
	 
	 
}
@Component 
class StockServiceFallback implements StockService {

	@Override
	public List<StockDto> getStocks() {
		return new ArrayList<>();
	}

}