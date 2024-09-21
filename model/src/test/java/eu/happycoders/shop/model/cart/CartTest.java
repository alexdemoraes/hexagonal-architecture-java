package eu.happycoders.shop.model.cart;

import static eu.happycoders.shop.model.money.TestMoneyFactory.euros;
import static org.assertj.core.api.Assertions.assertThat;

import eu.happycoders.shop.model.product.Product;
import eu.happycoders.shop.model.product.TestProductFactory;
import org.junit.jupiter.api.Test;

class CartTest {

  @Test
  void givenEmptyCart_addTwoProducts_numberOfItemsAndSubTotalIsCalculatedCorrectly()
  throws NotEnoughItemsInStockException {
    Cart cart = TestCartFactory.emptyCartForRanomCustomer();

    Product product1 = TestProductFactory.createTestProduct(euros(12, 99));
    Product product2 = TestProductFactory.createTestProduct(euros(5, 97));

    cart.addProduct(product1, 3);
    cart.addProduct(product2, 5);

    assertThat(cart.numberOfItems()).isEqualTo(8);
    assertThat(cart.subTotal()).isEqualTo(euros(68, 82));
  }

}
