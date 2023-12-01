package lu.sfeir.commerce.product.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lu.sfeir.commerce.product.entity.Event;
import lu.sfeir.commerce.product.events.EventStatus;
import lu.sfeir.commerce.product.repository.EventRepository;

@RequiredArgsConstructor
@Service
public class EventService {

	private final EventRepository repository;

	@Transactional
	public Event createEvent(String type, String content) {
		return repository.save(Event.builder().type(type).eventStatus(EventStatus.PENDING).content(content).build());
	}

	@Transactional
	public Event updateEvent(Event event) {
		return repository.save(event);
	}

}
