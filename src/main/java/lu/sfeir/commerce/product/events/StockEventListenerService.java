package lu.sfeir.commerce.product.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lu.sfeir.commerce.product.entity.Event;
import lu.sfeir.commerce.product.services.EventService;
import lu.sfeir.commerce.product.services.ProductService;

@RequiredArgsConstructor
@Service
public class StockEventListenerService {
	
	private final ProductService productService;
	private final EventService eventService;
	private final ObjectMapper objectMapper;
	
	@KafkaListener(topics = "stocks", groupId = "commerce")
	public void listenStocksEvents(String content) {
		System.out.println("Received Message in group commerce: " + content);
		Event event = this.eventService.createEvent(StockUpdated.class.getName(),content);
		try {
			final StockUpdated eventMessage = objectMapper.readValue(content, StockUpdated.class);
			productService.updateProductStock(eventMessage.getProductId(), eventMessage.getNumberAvailable());
			event.setEventStatus(EventStatus.SUCCES);
			eventService.updateEvent(event);
		}catch(Exception e) {
			System.out.println("Error "+e.getMessage());
			//save event with error
			event.setEventStatus(EventStatus.ERROR);
			event.setErrorMessage(e.getMessage());
			eventService.updateEvent(event);

		}

	}
}
