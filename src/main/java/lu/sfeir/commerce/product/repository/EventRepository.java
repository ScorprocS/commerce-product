package lu.sfeir.commerce.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import lu.sfeir.commerce.product.entity.Event;
import lu.sfeir.commerce.product.entity.Product;

public interface EventRepository  extends JpaRepository<Event,Long>{

}
