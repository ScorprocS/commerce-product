package lu.sfeir.commerce.product.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lu.sfeir.commerce.product.AbstractIntegrationTest;
import lu.sfeir.commerce.product.entity.Event;
import lu.sfeir.commerce.product.entity.Product;
import lu.sfeir.commerce.product.events.EventStatus;
import lu.sfeir.commerce.product.events.StockEventListenerService;
import lu.sfeir.commerce.product.events.StockUpdated;
import lu.sfeir.commerce.product.repository.EventRepository;
import lu.sfeir.commerce.product.repository.ProductRepository;

public class StockEventListenerServiceTest extends AbstractIntegrationTest{
	@Autowired
	private StockEventListenerService stockEventListenerService; 
	@Autowired
	private ProductRepository productRepository; 
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void listenStocksEventsInvalidJsonShouldFail() {
		//givent
		String content = "{\"ab\":1";
		//when
		boolean result = stockEventListenerService.listenStocksEvents(content);
		//then
		assertFalse(result);
		Optional<Event> e = eventRepository.findTopByTypeOrderByIdDesc(StockUpdated.class.getName());
		assertTrue(e.isPresent());
		assertEquals(content, e.get().getContent());
		assertEquals(EventStatus.ERROR, e.get().getEventStatus());
		assertTrue(StringUtils.isNotBlank(e.get().getErrorMessage()));
		assertEquals(StockUpdated.class.getName(), e.get().getType());

	}

	@Test
	void listenStocksEventsValidJsonShouldFailProductNotFound() throws JsonProcessingException {
		//given
		long newQuantity = 2000;
		String message = objectMapper.writeValueAsString(StockUpdated.builder()
				.productId(99999l)
				.oldNumberavailable(0l)
				.numberAvailable(newQuantity)
				.build());
		//when
		boolean result = stockEventListenerService.listenStocksEvents(message);
		
		assertFalse(result);
		Optional<Event> e = eventRepository.findTopByTypeOrderByIdDesc(StockUpdated.class.getName());
		assertTrue(e.isPresent());
		assertEquals(message, e.get().getContent());
		assertEquals(EventStatus.ERROR, e.get().getEventStatus());
		assertTrue(StringUtils.isNotBlank(e.get().getErrorMessage()));
		assertEquals(StockUpdated.class.getName(), e.get().getType());
	}
	
	@Test
	void listenStocksEventsValidJsonShouldOk() throws JsonProcessingException {
		//given
		Product p = createProduct();
		long newQuantity = 2000;
		String message = objectMapper.writeValueAsString(StockUpdated.builder()
				.productId(p.getId())
				.oldNumberavailable(p.getQuantity())
				.numberAvailable(newQuantity)
				.build());
		//when
		boolean result = stockEventListenerService.listenStocksEvents(message);
		
		assertTrue(result);
		Optional<Product> updatedProduct = productRepository.findById(p.getId());
		assertTrue(updatedProduct.isPresent());
		assertEquals(newQuantity, updatedProduct.get().getQuantity());
		Optional<Event> e = eventRepository.findTopByTypeOrderByIdDesc(StockUpdated.class.getName());
		assertTrue(e.isPresent());
		assertEquals(message, e.get().getContent());
		assertEquals(EventStatus.SUCCES, e.get().getEventStatus());
		assertTrue(StringUtils.isBlank(e.get().getErrorMessage()));
		assertEquals(StockUpdated.class.getName(), e.get().getType());
	}
	
	private Product createProduct() {
		
		return productRepository.save(Product.builder().name("PC Asus").price(BigDecimal.valueOf(600)).quantity(0l).build());
	}
}
