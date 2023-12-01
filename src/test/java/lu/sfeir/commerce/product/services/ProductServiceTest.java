package lu.sfeir.commerce.product.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;
import lu.sfeir.commerce.product.AbstractIntegrationTest;
import lu.sfeir.commerce.product.dto.ProductDto;
import lu.sfeir.commerce.product.dto.StockDto;
import lu.sfeir.commerce.product.entity.Product;
import lu.sfeir.commerce.product.repository.ProductRepository;

class ProductServiceTest extends AbstractIntegrationTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductService productService;

    @DisplayName("All With Stock")
    @Nested
    class AllWithStock {

        @Test
        @Transactional
        void nonEmpty() {
            final Product iPhone = save(iphoneProductEntity());
            save(appleTvProductEntity()); // noise
            final StockDto iphoneStock = createStockDto(iPhone, 90);
            final StockDto pencilStock = new StockDto(99L, 87L, 99L);

            save(iphoneStock, pencilStock);

            final List<ProductDto> actual = productService.getAllWithStock();

            assertThat(actual).hasSize(2);

            final int iphoneIndex = findIndex(actual, "iPhone");
            final int appleTvIndex = findIndex(actual, "Apple TV");

            assertThat(actual).element(iphoneIndex).extracting(ProductDto::getNumberAvailable).isEqualTo(90L);
            assertThat(actual).element(iphoneIndex).extracting(ProductDto::getPrice).isEqualTo(BigDecimal.valueOf(1045.99));
            assertThat(actual).element(iphoneIndex).extracting(ProductDto::getName).isEqualTo("iPhone");

            assertThat(actual).element(appleTvIndex).extracting(ProductDto::getNumberAvailable).isEqualTo(0L);
            assertThat(actual).element(appleTvIndex).extracting(ProductDto::getPrice).isEqualTo(BigDecimal.valueOf(168.90));
            assertThat(actual).element(appleTvIndex).extracting(ProductDto::getName).isEqualTo("Apple TV");
        }
    }

    private void save(StockDto... stock) {
        when(stockService.getStocks())
                .thenReturn(asList(stock));
    }

    @SuppressWarnings("SameParameterValue")
    private StockDto createStockDto(Product product, int numberAvailable) {
        return new StockDto(product.getId() + 10, (long) numberAvailable, product.getId());
    }

    private Product save(Product product) {
        return repository.save(product);
    }

    private Product iphoneProductEntity() {
        final Product product = new Product();

        product.setName("iPhone");
        product.setPrice(BigDecimal.valueOf(1045.99));
        product.setBarCode("ABCD");

        return product;
    }

    private Product appleTvProductEntity() {
        final Product product = new Product();

        product.setName("Apple TV");
        product.setPrice(BigDecimal.valueOf(168.90));
        product.setBarCode("EFGH");

        return product;
    }

    private int findIndex(List<ProductDto> actual, String productName) {
        return IntStream.range(0, actual.size())
                .filter(i -> Objects.equals(productName, actual.get(i).getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no product %s.".formatted(productName)));
    }
}