package eu.happycoders.shop.adapter.in.rest.common;

import static eu.happycoders.shop.adapter.in.rest.common.ControllerCommons.clientErrorException;

import eu.happycoders.shop.model.product.ProductId;
import jakarta.ws.rs.core.Response.Status;

public final class ProductIdParser {

  private ProductIdParser() {}

  public static ProductId parseProductId(String string) {
    if (string == null) {
      throw clientErrorException(Status.BAD_REQUEST, "Missing 'productId'");
    }

    try {
      return new ProductId(string);
    } catch (IllegalArgumentException e) {
      throw clientErrorException(Status.BAD_REQUEST, "Invalid 'productId'");
    }
  }
}
