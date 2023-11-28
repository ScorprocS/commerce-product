package lu.sfeir.commerce.product.services;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lu.sfeir.commerce.product.dto.StockDto;

@FeignClient(value = "stocks", url = "http://localhost:8087/api")
public interface StockService {
	
	 @RequestMapping(method = RequestMethod.GET, value = "/stocks")
	 List<StockDto> getStocks();
}
