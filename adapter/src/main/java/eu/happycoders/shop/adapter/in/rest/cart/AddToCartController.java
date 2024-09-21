package eu.happycoders.shop.adapter.in.rest.cart;

import static eu.happycoders.shop.adapter.in.rest.common.ControllerCommons.clientErrorException;
import static eu.happycoders.shop.adapter.in.rest.common.CustomerIdParser.parseCustomerId;
import static eu.happycoders.shop.adapter.in.rest.common.ProductIdParser.parseProductId;

import eu.happycoders.shop.application.port.in.cart.AddToCartUseCase;
import eu.happycoders.shop.application.port.in.cart.ProductNotFoundException;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.cart.NotEnoughItemsInStockException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
public class AddToCartController {

  private final AddToCartUseCase addToCartUseCase;

  public AddToCartController(AddToCartUseCase addToCartUseCase) {
    this.addToCartUseCase = addToCartUseCase;
  }

  @POST
  @Path("/{customerId}/line-items")
  public CartWebModel addLineItem(
      @PathParam("customerId") String customerIdString,
      @QueryParam("productId") String productIdString,
      @QueryParam("quantity") int quantity) {
    var customerId = parseCustomerId(customerIdString);
    var productId = parseProductId(productIdString);

    try {
      Cart cart = addToCartUseCase.addToCart(customerId, productId, quantity);
      return CartWebModel.fromDomainModel(cart);
    } catch (ProductNotFoundException e) {
      throw clientErrorException(Status.BAD_REQUEST, "The requested product does not exist");
    } catch (NotEnoughItemsInStockException e) {
      throw clientErrorException(
          Status.BAD_REQUEST, "Only %d items in stock".formatted(e.itemsInStock()));
    }
  }
}
