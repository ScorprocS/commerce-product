package lu.sfeir.commerce.product.restrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import lu.sfeir.commerce.product.entity.Product;

public interface ProductRepository  extends JpaRepository<Product,Long>,PagingAndSortingRepository<Product,Long>{

}
