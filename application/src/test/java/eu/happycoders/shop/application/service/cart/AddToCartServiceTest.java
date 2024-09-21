package eu.happycoders.shop.application.service.cart;

import static eu.happycoders.shop.model.money.TestMoneyFactory.euros;
import static eu.happycoders.shop.model.product.TestProductFactory.createTestProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.application.port.out.persistence.ProductRepository;
import eu.happycoders.shop.application.port.in.cart.ProductNotFoundException;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.cart.NotEnoughItemsInStockException;
import eu.happycoders.shop.model.customer.CustomerId;
import eu.happycoders.shop.model.product.Product;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;

class AddToCartServiceTest {

  private static final CustomerId TEST_CUSTOMER_ID = new CustomerId(61157);
  private static final Product TEST_PRODUCT_1 = createTestProduct(euros(19,99));
  private static final Product TEST_PRODUCT_2 = createTestProduct(euros(25,99));

  private final CartRepository cartRepository = mock(CartRepository.class);
  private final ProductRepository productRepository = mock(ProductRepository.class);
  private final AddToCartService addToCartService =
      new AddToCartService(cartRepository, productRepository);

  @BeforeEach
  void initTestDoubles() {
    when(productRepository.findById(TEST_PRODUCT_1.id()))
        .thenReturn(Optional.of(TEST_PRODUCT_1));

    when(productRepository.findById(TEST_PRODUCT_2.id()))
        .thenReturn(Optional.of(TEST_PRODUCT_2));
  }

  void givenExistingCart_addToCart_cartWithAddedProductIsSavedAndReturned()
  throws NotEnoughItemsInStockException, ProductNotFoundException {
    // Arrange
    Cart persistedCart = new Cart(TEST_CUSTOMER_ID);
    persistedCart.addProduct(TEST_PRODUCT_1, 1);

    when(cartRepository.findByCustomerId(TEST_CUSTOMER_ID))
        .thenReturn(Optional.of(persistedCart));

    // Act
    Cart cart = addToCartService.addToCart(TEST_CUSTOMER_ID, TEST_PRODUCT_2.id(), 3);

    // Assert
    verify(cartRepository).save(cart);

    assertThat(cart.lineItems()).hasSize(2);
    assertThat(cart.lineItems().get(0).product()).isEqualTo(TEST_PRODUCT_1);
    assertThat(cart.lineItems().get(0).quantity()).isEqualTo(1);
    assertThat(cart.lineItems().get(1).product()).isEqualTo(TEST_PRODUCT_2);
    assertThat(cart.lineItems().get(1).quantity()).isEqualTo(3);

  }
}
