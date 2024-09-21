package eu.happycoders.shop.adapter.out.persistence;

import static eu.happycoders.shop.model.money.TestMoneyFactory.euros;
import static eu.happycoders.shop.model.product.TestProductFactory.createTestProduct;

import static org.assertj.core.api.Assertions.assertThat;

import eu.happycoders.shop.adapter.out.persistence.inmemory.DemoProducts;
import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.application.port.out.persistence.ProductRepository;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.cart.NotEnoughItemsInStockException;
import eu.happycoders.shop.model.customer.CustomerId;
import eu.happycoders.shop.model.product.Product;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AbstractCartRepositoryTest {

  private static final Product TEST_PRODUCT_1 = createTestProduct(euros(19, 99));
  private static final Product TEST_PRODUCT_2 = createTestProduct(euros(1, 49));

  private static final AtomicInteger CUSTOMER_ID_SEQUENCE_GENERATOR = new AtomicInteger();

  @Inject Instance<CartRepository> cartRepositoryInstance;

  @Inject Instance<ProductRepository> productRepositoryInstance;

  private CartRepository cartRepository;

  @BeforeEach
  void initRepository() {
    cartRepository = cartRepositoryInstance.get();
    persistTestProducts();
  }

  private void persistTestProducts() {
    ProductRepository productRepository = productRepositoryInstance.get();
    productRepository.save(TEST_PRODUCT_1);
    productRepository.save(TEST_PRODUCT_2);
  }

  @Test
  void givenACustomerIdForWhichNoCartIsPersisted_findByCustomerId_returnsAnEmptyOptional() {
    CustomerId customerId = createUniqueCustomerId();

    var cart = cartRepository.findByCustomerId(customerId);
    assertThat(cart).isEmpty();
  }

  @Test
  void givenPersistedCartWithProduct_findByCustomerId_returnsTheAppropriateCart()
      throws NotEnoughItemsInStockException {
    CustomerId customerId = createUniqueCustomerId();

    Cart persistedCart = new Cart(customerId);
    persistedCart.addProduct(TEST_PRODUCT_1, 1);
    cartRepository.save(persistedCart);

    var cart = cartRepository.findByCustomerId(customerId);

    assertThat(cart).isNotEmpty();
    assertThat(cart.get().id()).isEqualTo(customerId);
    assertThat(cart.get().lineItems()).hasSize(1);
    assertThat(cart.get().lineItems().get(0).product()).isEqualTo(TEST_PRODUCT_1);
    assertThat(cart.get().lineItems().get(0).quantity()).isEqualTo(1);
  }

  private static CustomerId createUniqueCustomerId() {
    return new CustomerId(CUSTOMER_ID_SEQUENCE_GENERATOR.incrementAndGet());
  }
}
