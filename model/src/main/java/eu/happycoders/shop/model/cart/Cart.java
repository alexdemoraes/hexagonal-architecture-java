package eu.happycoders.shop.model.cart;

import eu.happycoders.shop.model.customer.CustomerId;
import eu.happycoders.shop.model.money.Money;
import eu.happycoders.shop.model.product.Product;
import eu.happycoders.shop.model.product.ProductId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A shopping cart of a particular customer, containing several line items.
 *
 * @author Sven Woltmann
 */
@Accessors(fluent = true)
@RequiredArgsConstructor
public class Cart {

  @Getter private final CustomerId id;

  private final Map<ProductId, CartLineItem> lineItems = new LinkedHashMap<>();

  public void addProduct(Product product, int quantity) throws NotEnoughItemsInStockException {
    lineItems
        .computeIfAbsent(product.id(), ignored -> new CartLineItem(product))
        .increaseQuantityBy(quantity, product.itemsInStock());
  }

  public List<CartLineItem> lineItems() {
    return List.copyOf(lineItems.values());
  }

  public int numberOfItems() {
    return lineItems.values().stream().mapToInt(CartLineItem::quantity).sum();
  }

  public Money subTotal() {
    return lineItems.values().stream().map(CartLineItem::subTotal).reduce(Money::add).orElse(null);
  }
}
