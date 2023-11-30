package lu.sfeir.commerce.product;

import jakarta.annotation.PostConstruct;
import lu.sfeir.commerce.product.services.StockService;
import org.springframework.boot.test.mock.mockito.MockBean;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    @MockBean
    private StockService stockService;

    @PostConstruct
    void toto(){
        System.out.println("yeay");
    }
}
