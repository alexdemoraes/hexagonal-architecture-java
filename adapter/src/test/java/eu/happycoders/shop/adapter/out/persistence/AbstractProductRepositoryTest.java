package eu.happycoders.shop.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import eu.happycoders.shop.adapter.out.persistence.inmemory.DemoProducts;
import eu.happycoders.shop.application.port.out.persistence.ProductRepository;
import eu.happycoders.shop.model.product.Product;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AbstractProductRepositoryTest {

  @Inject
  Instance<ProductRepository> productRepositoryInstance;

  private ProductRepository productRepository;

  @BeforeEach
  void initRepository() {
    productRepository = productRepositoryInstance.get();
  }

  @Test
  void givenTestProducts_findByNameOrDescription_returnsMatchingProducts() {
    String query = "monitor";

    List<Product> products = productRepository.findByNameOrDescription(query);

    assertThat(products)
        .containsExactlyInAnyOrder(DemoProducts.COMPUTER_MONITOR, DemoProducts.MONITOR_DESK_MOUNT);
  }


}
