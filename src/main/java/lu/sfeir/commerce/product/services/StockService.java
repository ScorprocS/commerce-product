package lu.sfeir.commerce.product.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.github.resilience4j.retry.annotation.Retry;
import lu.sfeir.commerce.product.dto.StockDto;
@FeignClient(value = "stocks", url = "http://localhost:8087/api")
//@FeignClient(value = "stocks", url = "http://localhost:8087/api", fallback = StockServiceFallback.class)
public interface StockService {
	@Retry(name = "StockService-getStocks", fallbackMethod = "retryFallBack")
	@RequestMapping(method = RequestMethod.GET, value = "/stocks")
	List<StockDto> getStocks();
	
	//Could return empty array or last cached request
	default List<StockDto> retryFallBack(Throwable t) {
		return new ArrayList<>();
	}

}

@Component
class StockServiceFallback implements StockService {
	//Could return empty array or last cached request
	@Override
	public List<StockDto> getStocks() {
		return new ArrayList<>();
	}

}