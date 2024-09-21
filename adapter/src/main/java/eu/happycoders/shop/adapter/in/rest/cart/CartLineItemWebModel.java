package eu.happycoders.shop.adapter.in.rest.cart;

import eu.happycoders.shop.model.cart.CartLineItem;
import eu.happycoders.shop.model.money.Money;

public record CartLineItemWebModel(
    String productId, String productName, Money price, int quantity) {

  static CartLineItemWebModel fromDomainModel(CartLineItem lineItem) {
    var product = lineItem.product();
    return new CartLineItemWebModel(
        product.id().value(), product.name(), product.price(), lineItem.quantity());
  }
}
